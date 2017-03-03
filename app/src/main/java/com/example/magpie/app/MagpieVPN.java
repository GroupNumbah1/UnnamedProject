package com.example.magpie.app;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import static com.example.magpie.app.R.layout.magpie_vpn_start;

/**
 * Created by mattpatera on 3/3/17.
 */

public class MagpieVPN extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(magpie_vpn_start);

        // Grab switch element from view and cast it to a switch.
        final Switch vpn_toggle = (Switch)findViewById(R.id.vpn_toggle);

        // attach switch listener
        if (vpn_toggle != null) {
            vpn_toggle.setOnCheckedChangeListener(this);
        }
    }

    private void startVPN()
    {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Toast.makeText(this, "The VPN is " + (isChecked ? "starting up" : "shutting down"), Toast.LENGTH_SHORT).show();

        if (isChecked) {

        } else {

        }
    }
}
