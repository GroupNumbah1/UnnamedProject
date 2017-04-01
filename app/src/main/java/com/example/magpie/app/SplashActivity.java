package com.example.magpie.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Thread will allow for splashscreen to displayed 'x' seconds
        Thread splashScreen = new Thread(){

            public void run(){

                //try/catch allows for delay in pic and catch and exception
                try{

                    sleep(4000);

                }catch(Exception e){
                    e.printStackTrace();
                }finally{

                    
                    startActivity(new Intent(getApplicationContext(),MagpieVPN.class));
                    finish();
                }

            }

        };//thread

        //start
        splashScreen.start();
    }
}
