package com.example.magpie.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button aboutUsBtn;
    ImageButton startMagpieBtn;
    ImageButton helpInfoBtn;
    Button privacyPolicyBtn;
    //Button runMagpie;
    ImageButton settingsBtn;
    ImageButton contactUsBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        aboutUsBtn = (Button)findViewById(R.id.AboutUsButton);
        aboutUsBtn.setOnClickListener(this);

        privacyPolicyBtn = (Button)findViewById(R.id.PrivacyPolicyButton);
        privacyPolicyBtn.setOnClickListener(this);

        helpInfoBtn = (ImageButton)findViewById(R.id.HelpInfoButton);
        helpInfoBtn.setOnClickListener(this);

        settingsBtn = (ImageButton)findViewById(R.id.SettingsButton);
        settingsBtn.setOnClickListener(this);

        contactUsBtn = (ImageButton)findViewById(R.id.ContactUsButton);
        contactUsBtn.setOnClickListener(this);

        startMagpieBtn = (ImageButton)findViewById(R.id.RunMagpieButton);
        startMagpieBtn.setOnClickListener(this);





        //Button Press
        //SetUpButtonPress();
    }//Oncreate



    @Override
    public void onClick(View view){
        /*if(view.getId() == R.id.AboutUsButton){
            Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
            startActivity(intent);
        }
        else*/ if(view.getId() == R.id.HelpInfoButton){
            Intent intent = new Intent(MainActivity.this, HelpInfoActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.ContactUsButton){
            Intent intent = new Intent(MainActivity.this, ContactUsInfoActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.PrivacyPolicyButton){
            Intent intent = new Intent(MainActivity.this, PrivacyPolicyActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.SettingsButton){
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        else if(view.getId() == R.id.RunMagpieButton){
            Intent intent = new Intent(MainActivity.this, EnterMagpieStartScreenActivity.class);
            startActivity(intent);
        }


    }

    //Function for button press
/*   private void SetUpButtonPress(){

        //about us btn will take us to aboutUs activity screen
        aboutUsBtn = (ImageButton)findViewById(R.id.HelpInfoButton);
        aboutUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HelpInfoActivity.class);
                startActivity(intent);

            }
        });
   }//SetUpButtonPress*/

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}//class
