package com.example.magpie.app;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.VpnService;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class MagpieVPNService extends VpnService implements Runnable {

    public static final String BROADCAST_VPN_STATE = "com.example.magpie.app.VPN_STATE";

    private static final String TAG = MagpieVPNService.class.getSimpleName();

    private static final String VPN_ADDRESS = "10.0.0.2";
    private static final int VPN_ADDRESS_PREFIX_LENGTH = 32;

    private static final String VPN_ROUTE = "0.0.0.0";
    private static final int VPN_ROUTE_PREFIX_LENGTH = 0;

    private static boolean isRunning = false;
    private Thread thread;
    private ParcelFileDescriptor vpnInterface = null;

    private PendingIntent pendingIntent;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        /*if(thread != null){
            thread.interrupt();
        }

        thread = new Thread(this, "MagpieThread");
        thread.start();
        */
        return START_STICKY;
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
        isRunning = true;
        setupVPN();
        try {
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BROADCAST_VPN_STATE).putExtra("running", true));
            Log.i(TAG, "Started MagpieVPNService");
        } catch (Exception e) {
            Log.e(TAG, "ERROR starting device", e);

        }
    }

    @Override
    public void onDestroy()
    {
        /*if(thread != null){
            thread.interrupt();
        }*/
        super.onDestroy();
    }

    private void setupVPN() {
        if (vpnInterface == null) {
            Builder vpnBuilder = new Builder();
            vpnBuilder.addAddress(VPN_ADDRESS, VPN_ADDRESS_PREFIX_LENGTH);
            vpnBuilder.addRoute(VPN_ROUTE, VPN_ROUTE_PREFIX_LENGTH);
            vpnInterface = vpnBuilder.setSession("Magpie VPN").setConfigureIntent(pendingIntent).establish();
        } else {
            Log.i(TAG, "An instance of Magpie VPN is already running.");
        }
        run();
    }

    private boolean run() throws Exception {
        DatagramChannel tunnel = null;
        boolean connected = false;

        tunnel = DatagramChannel.open();
        //Using localhost to read and write to self
        tunnel.connect(new InetSocketAddress("127.0.0.1", 8087));
        protect(tunnel.socket());
        tunnel.configureBlocking(false);
        FileInputStream in = new FileInputStream(vpnInterface.getFileDescriptor());
        FileOutputStream out = new FileOutputStream(vpnInterface.getFileDescriptor());
        ByteBuffer packet = ByteBuffer.allocate(32767);

        while (true) {
            boolean idle = true;
            int length = in.read(packet.array());
            if (length > 0) {
                packet.limit(length);
                tunnel.write(packet);
                packet.clear();
            }

            length = tunnel.read(packet);
            if (length > 0) {
                if (packet.get(0) != 0) {
                    out.write(packet.array(), 0, length);
                }
                packet.clear();
            }
        }
        try {
            tunnel.close();
        } catch (Exception e) {
        }
        return connected;
        }
    }

    public static boolean isRunning()
    {
        return isRunning;
    }
}
