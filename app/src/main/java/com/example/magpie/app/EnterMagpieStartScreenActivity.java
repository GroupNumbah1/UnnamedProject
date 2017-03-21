package com.example.magpie.app;

import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EnterMagpieStartScreenActivity extends AppCompatActivity implements View.OnClickListener {

    Button beginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_magpie_start_screen);

        beginBtn = (Button)findViewById(R.id.BeginMagPieButton);
        beginBtn.setOnClickListener(this);


    }


    @Override
    public void onClick(View v){
        if(v.getId() == R.id.BeginMagPieButton){
            Intent intent = new Intent(EnterMagpieStartScreenActivity.this, RunScreenActivity.class);
            startActivity(intent);
        }

    }
}
