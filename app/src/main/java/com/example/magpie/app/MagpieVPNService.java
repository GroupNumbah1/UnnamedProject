package com.example.magpie.app;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Selector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mattpatera on 3/3/17.
 */

public class MagpieVPNService extends VpnService {

    //
    public static final String BROADCAST_VPN_STATE = "com.example.magpie.app.VPN_STATE";

    private static final String TAG = MagpieVPNService.class.getSimpleName();

    // default port that handles IPv4 connections
    private static final String VPN_ADDRESS = "10.0.0.2";
    private static final int VPN_ADDRESS_PREFIX_LENGTH = 32;

    private static final String VPN_ROUTE = "0.0.0.0";
    private static final int VPN_ROUTE_PREFIX_LENGTH = 0;

    private static boolean isRunning = false;

    private ConcurrentLinkedQueue<UdpPacket> deviceToNetworkUDPQueue;
    private ConcurrentLinkedQueue<ByteBuffer> networkToDeviceQueue;
    private ExecutorService executorService;
    private Selector udpSelector;

    private ParcelFileDescriptor vpnInterface = null;

    private PendingIntent pendingIntent;

    @Override
    public void onCreate()
    {
        super.onCreate();
        isRunning = true;
        setupVPN();
        try {
            udpSelector = Selector.open();
            deviceToNetworkUDPQueue = new ConcurrentLinkedQueue<UdpPacket>(); // may need to be untyped
            networkToDeviceQueue = new ConcurrentLinkedQueue<ByteBuffer>();

            executorService = Executors.newFixedThreadPool(3);
            executorService.submit(new PacketIn(networkToDeviceQueue, udpSelector));
            executorService.submit(new PacketOut(deviceToNetworkUDPQueue, udpSelector, this));
            executorService.submit(new VPNRunnable(vpnInterface.getFileDescriptor(), deviceToNetworkUDPQueue, networkToDeviceQueue));

            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BROADCAST_VPN_STATE).putExtra("running", true));
            Log.i(TAG, "Broadcasting service intent(MAGPIEVPNService: 47)");
        } catch (Exception e) {
            Log.e(TAG, "ERROR starting device (MAGPIEVPNService: 49)", e);

        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    private void setupVPN() {
        if (vpnInterface == null) {
            Log.i(TAG, "Setting up VPN Builder (MAGPIEVPNService: 62)");
            Builder vpnBuilder = new Builder();
            // Add address on which VPN listens
            vpnBuilder.addAddress(VPN_ADDRESS, VPN_ADDRESS_PREFIX_LENGTH);
            // Route VPN traffic to desired internal location
            vpnBuilder.addRoute(VPN_ROUTE, VPN_ROUTE_PREFIX_LENGTH);
            vpnInterface = vpnBuilder.setSession("Magpie VPN").setConfigureIntent(pendingIntent).establish();
        } else {
            Log.i(TAG, "An instance of Magpie VPN is already running. (MAGPIEVPNService: 68)");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY;
    }

    public static boolean isRunning() { return isRunning; }

    private static void closeResources(Closeable... resources)
    {
        for (Closeable resource : resources)
        {
            try
            {
                resource.close();
            }
            catch (IOException e)
            {
                // Ignore
            }
        }
    }

    private static class VPNRunnable implements Runnable
    {
        private static final String TAG = VPNRunnable.class.getSimpleName();

        private FileDescriptor vpnFileDescriptor;

        private ConcurrentLinkedQueue<UdpPacket> deviceToNetworkUDPQueue;
        private ConcurrentLinkedQueue<ByteBuffer> networkToDeviceQueue;

        public VPNRunnable(FileDescriptor vpnFileDescriptor,
                           ConcurrentLinkedQueue<UdpPacket> deviceToNetworkUDPQueue,
                           ConcurrentLinkedQueue<ByteBuffer> networkToDeviceQueue)
        {
            this.vpnFileDescriptor = vpnFileDescriptor;
            this.deviceToNetworkUDPQueue = deviceToNetworkUDPQueue;
            this.networkToDeviceQueue = networkToDeviceQueue;
        }

        @Override
        public void run() {
            Log.i(TAG, "Started");

            FileChannel vpnInput = new FileInputStream(vpnFileDescriptor).getChannel();
            FileChannel vpnOutput = new FileOutputStream(vpnFileDescriptor).getChannel();

            try {
                ByteBuffer bufferToNetwork = null;
                boolean dataSent = true;
                boolean dataReceived;
                while (!Thread.interrupted()) {
                    if (dataSent)
                        bufferToNetwork = ByteBufferPool.acquire();
                    else
                        bufferToNetwork.clear();

                    // TODO: Block when not connected
                    int readBytes = vpnInput.read(bufferToNetwork);
                    if (readBytes > 0) {
                        dataSent = true;
                        bufferToNetwork.flip();
                        //Log.i(TAG, bufferToNetwork.toString());
                        UdpPacket packet = new UdpPacket(bufferToNetwork);
                        if (packet.isUDP) {
                            deviceToNetworkUDPQueue.offer(packet);
                        } else {
                            Log.w(TAG, "Unknown packet type");
                            Log.w(TAG, packet.ip4Header.toString());
                            dataSent = false;
                        }
                    } else {
                        dataSent = false;
                    }

                    ByteBuffer bufferFromNetwork = networkToDeviceQueue.poll();
                    if (bufferFromNetwork != null) {
                        bufferFromNetwork.flip();
                        //Log.i(TAG, bufferFromNetwork.toString()); // HERE
                        while (bufferFromNetwork.hasRemaining())
                            Log.i(TAG, "" + bufferFromNetwork.get());
                        vpnOutput.write(bufferFromNetwork);
                        dataReceived = true;

                        ByteBufferPool.release(bufferFromNetwork);
                    }
                    else {
                        dataReceived = false;
                    }

                    // TODO: Sleep-looping is not very battery-friendly, consider blocking instead
                    // Confirm if throughput with ConcurrentQueue is really higher compared to BlockingQueue
                    if (!dataSent && !dataReceived)
                        Thread.sleep(10);
                }
            }
            catch (InterruptedException e)
            {
                Log.i(TAG, "Stopping");
            }
            catch (IOException e)
            {
                Log.w(TAG, e.toString(), e);
            }
            finally
            {
                closeResources(vpnInput, vpnOutput);
            }
        }
    }
}
