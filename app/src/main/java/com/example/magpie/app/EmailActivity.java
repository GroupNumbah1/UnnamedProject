package com.example.magpie.app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EmailActivity extends AppCompatActivity implements View.OnClickListener {

    Button send_Btn;
    Button attachmnt_Btn;
    EditText email_ETxt, subject_Etxt, message_Etxt;
    String to;
    String subject;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        //link and attach onclick listener
        send_Btn = (Button)findViewById(R.id.send_button);
        attachmnt_Btn = (Button)findViewById(R.id.attachment_button);
        send_Btn.setOnClickListener(this);
        attachmnt_Btn.setOnClickListener(this);

        //link edittext from xml layout file
        email_ETxt = (EditText)findViewById(R.id.e_address_editTxt);
        subject_Etxt = (EditText)findViewById(R.id.subject_editTxt);
        message_Etxt = (EditText)findViewById(R.id.message_editTxt);


    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.send_button){

            to = email_ETxt.getText().toString();
            subject  = subject_Etxt.getText().toString();
            message = message_Etxt.getText().toString();

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            //Intent emailIntent = getPackageManager().getLaunchIntentForPackage("com.android.email");
            //emailIntent.setData(Uri.parse("mailto:" + to));

            //SetType for email is message/rfc822
            emailIntent.setType("message/rfc822");

            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));

        }

        else if(view.getId() == R.id.attachment_button){

        }
    }
}
