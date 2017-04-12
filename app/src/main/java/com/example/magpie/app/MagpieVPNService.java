package com.example.magpie.app;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

/**
 * Created by mattpatera on 3/3/17.
 */

public class MagpieVPNService extends VpnService {

    //
    public static final String BROADCAST_VPN_STATE = "com.example.magpie.app.VPN_STATE";

    private static final String TAG = MagpieVPNService.class.getSimpleName();

    private static final String VPN_ADDRESS = "10.0.0.2";
    private static final int VPN_ADDRESS_PREFIX_LENGTH = 32;

    private static final String VPN_ROUTE = "0.0.0.0";
    private static final int VPN_ROUTE_PREFIX_LENGTH = 0;

    private static boolean isRunning = false;

    private ParcelFileDescriptor vpnInterface = null;

    private PendingIntent pendingIntent;

    @Override
    public void onCreate()
    {
        super.onCreate();
        isRunning = true;
        setupVPN();
        try {
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
            Log.i(TAG, "Setting up VPN (MAGPIEVPNService: 62)");
            Builder vpnBuilder = new Builder();
            vpnBuilder.addAddress(VPN_ADDRESS, VPN_ADDRESS_PREFIX_LENGTH);
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
}
