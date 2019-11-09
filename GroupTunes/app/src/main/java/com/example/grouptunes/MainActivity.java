package com.example.grouptunes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the usertaps the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
        try {
            WebServerActivity server = new WebServerActivity();
            server.start();
            server.serve(null);
            Thread.sleep(1000000000);
            Log.d("myTag", "Error");
        } catch(Exception e) {
            Log.d("myTag", "Error");
        }


        //Intent intent = new Intent(this, WebServerActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
       // String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE.toString(), message);
       // startActivity(intent);
    }
}

