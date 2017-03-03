package com.example.magpie.app;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import static android.widget.Toast.*;
import static com.example.magpie.app.R.layout.magpie_vpn_start;

/**
 * Created by mattpatera on 3/3/17.
 */

public class MagpieVPN extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(magpie_vpn_start);

        // Grab switch element from view and cast it to a switch.
        final Switch vpn_toggle = (Switch)findViewById(R.id.vpn_toggle);

        // attach switch listener
        if (vpn_toggle != null) {
            vpn_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Toast.makeText(MagpieVPN.this, "The VPN is starting up", LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MagpieVPN.this, "The VPN is shutting down", LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void startVPN()
    {

    }

    private void stopVPN()
    {

    }
}
