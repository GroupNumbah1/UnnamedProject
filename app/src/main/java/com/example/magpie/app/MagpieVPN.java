package com.example.magpie.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.magpie.app.R.layout.temp_vpn_start;

/**
 * Created by mattpatera on 3/3/17.
 */

public class MagpieVPN extends AppCompatActivity {

    private static final String TAG = MagpieVPN.class.getSimpleName();

    // Hexadecimal representation of a byte -> 00001111
    private static final int VPN_REQUEST_CODE = 0x0F;

    private boolean isWaitingForVPN;

    // Receives broadcast from MagpieVPNService (line )
    private BroadcastReceiver vpnStateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // if the received broadcast is from
            if (MagpieVPNService.BROADCAST_VPN_STATE.equals(intent.getAction())) {
                if (intent.getBooleanExtra("running", false))
                    isWaitingForVPN = false;
                Log.i(TAG, "Broadcast Recieved");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(temp_vpn_start);

        // Grab switch element from view and cast it to a switch.
        final Switch vpn_toggle = (Switch)findViewById(R.id.vpn_toggle);


        // attach switch listener
        if (vpn_toggle != null) {
            vpn_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        startVPN();
                        Toast.makeText(MagpieVPN.this, "The VPN is starting up", LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MagpieVPN.this, "The VPN is shutting down", LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            //Intent links .this to 'X'.class
            Intent intent = new Intent(MagpieVPN.this, SettingsActivity.class);
            startActivity(intent);
            //return true;
        }
        else if(id == R.id.action_about_us){
            Intent intent = new Intent(MagpieVPN.this, AboutUsActivity.class);
            startActivity(intent);

        }

        else if(id == R.id.action_helpInfo){
            Intent intent = new Intent(MagpieVPN.this, HelpInfoActivity.class);
            startActivity(intent);

        }

        else if(id == R.id.action_Privacypolicy){
            Intent intent = new Intent(MagpieVPN.this, PrivacyPolicyActivity.class);
            startActivity(intent);

        }

        else if(id == R.id.action_contactus){
            Intent intent = new Intent(MagpieVPN.this, ContactUsInfoActivity.class);
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }

    private void startVPN() {
        Log.i(TAG, "Starting MagpieVPN");
        Intent magpieVpnIntent = VpnService.prepare(this);
        if (magpieVpnIntent != null) {
            startActivityForResult(magpieVpnIntent, VPN_REQUEST_CODE);
        } else {
            onActivityResult(VPN_REQUEST_CODE, RESULT_OK, null);
            isWaitingForVPN = true;
            startService(new Intent(this, MagpieVPNService.class));
        }
    }

    private void stopVPN() {

    }
}
