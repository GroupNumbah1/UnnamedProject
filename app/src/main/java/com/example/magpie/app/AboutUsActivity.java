package com.example.magpie.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.view.Menu;
import android.view.MenuItem;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if(id == R.id.action_helpInfo){
            Intent intent = new Intent(AboutUsActivity.this, HelpInfoActivity.class);
            startActivity(intent);

        }

        else if(id == R.id.action_home){
            Intent intent = new Intent(AboutUsActivity.this, MagpieVPN.class);
            startActivity(intent);

        }

        else if(id == R.id.action_Privacypolicy){
            Intent intent = new Intent(AboutUsActivity.this, PrivacyPolicyActivity.class);
            startActivity(intent);

        }

        else if(id == R.id.action_homeMenuBar){
            Intent intent = new Intent(AboutUsActivity.this, MagpieVPN.class);
            startActivity(intent);

        }

        else if(id == R.id.action_startemail_client){
            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            //Intent emailIntent = getPackageManager().getLaunchIntentForPackage("com.android.email");
            //emailIntent.setData(Uri.parse("mailto:" + to));
            //SetType for email is message/rfc822
            emailIntent.setType("message/rfc822");

            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"youremail@xyz"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Magpie Application");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "File from Magpie Application");

            startActivity(Intent.createChooser(emailIntent, "Send mail..."));

        }







        return super.onOptionsItemSelected(item);
    }

}
