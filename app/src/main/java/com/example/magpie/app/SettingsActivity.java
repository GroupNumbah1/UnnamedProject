package com.example.magpie.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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


        //If else statements decide which menu item was seledted
        //and start that activity
        if(id == R.id.action_about_us){
            Intent intent = new Intent(SettingsActivity.this, AboutUsActivity.class);
            startActivity(intent);

        }

        else if(id == R.id.action_helpInfo){
            Intent intent = new Intent(SettingsActivity.this, HelpInfoActivity.class);
            startActivity(intent);

        }

        else if(id == R.id.action_home){
            Intent intent = new Intent(SettingsActivity.this, MagpieVPN.class);
            startActivity(intent);

        }

        else if(id == R.id.action_Privacypolicy){
            Intent intent = new Intent(SettingsActivity.this, PrivacyPolicyActivity.class);
            startActivity(intent);

        }

        else if(id == R.id.action_contactus){
            Intent intent = new Intent(SettingsActivity.this, ContactUsInfoActivity.class);
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }

}
