package com.example.magpie.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayFileContentsActivity extends AppCompatActivity {

    TextView filecontext_Txtview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_file_contents);

        MagpieVPN mgVpn_str = new MagpieVPN();

        //display file contentese
        filecontext_Txtview = (TextView)findViewById(R.id.txtview_filecontents);
        filecontext_Txtview.setText(mgVpn_str.GetString());

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
        if(id == R.id.action_about_us){
            Intent intent = new Intent(DisplayFileContentsActivity.this, AboutUsActivity.class);
            startActivity(intent);

        }

        else if(id == R.id.action_home){
            Intent intent = new Intent(DisplayFileContentsActivity.this, MagpieVPN.class);
            startActivity(intent);

        }

        else if(id == R.id.action_helpInfo){
            Intent intent = new Intent(DisplayFileContentsActivity.this, HelpInfoActivity.class);
            startActivity(intent);

        }

        else if(id == R.id.action_homeMenuBar){
            Intent intent = new Intent(DisplayFileContentsActivity.this, MagpieVPN.class);
            startActivity(intent);

        }

        //Invokes email client list for email services
        else if(id == R.id.action_startemail_client) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            //Intent emailIntent = getPackageManager().getLaunchIntentForPackage("com.android.email");
            //emailIntent.setData(Uri.parse("mailto:" + to));

            //SetType for email is message/rfc822
            emailIntent.setType("message/rfc822");

            //Sets up email already with subject and text message
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"youremail@xyz"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Magpie Application");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "File from Magpie Application");
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        }

        return super.onOptionsItemSelected(item);
    }
}
