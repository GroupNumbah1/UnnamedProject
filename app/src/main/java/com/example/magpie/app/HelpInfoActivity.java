package com.example.magpie.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class HelpInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_info);
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
            Intent intent = new Intent(HelpInfoActivity.this, SettingsActivity.class);
            startActivity(intent);
            //return true;
        }

        else if(id == R.id.action_home){
            Intent intent = new Intent(HelpInfoActivity.this, MagpieVPN.class);
            startActivity(intent);

        }
        else if(id == R.id.action_about_us){
            Intent intent = new Intent(HelpInfoActivity.this, AboutUsActivity.class);
            startActivity(intent);

        }



        else if(id == R.id.action_Privacypolicy){
            Intent intent = new Intent(HelpInfoActivity.this, PrivacyPolicyActivity.class);
            startActivity(intent);

        }

        else if(id == R.id.action_contactus){
            Intent intent = new Intent(HelpInfoActivity.this, ContactUsInfoActivity.class);
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }

}
