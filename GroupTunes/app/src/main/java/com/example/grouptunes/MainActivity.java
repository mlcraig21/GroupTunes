package com.example.grouptunes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    Switch simpleSwitch1;
    Button submit;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initiate view's
        simpleSwitch1 = (Switch) findViewById(R.id.connection);
        submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String statusSwitch1;
                WebServerActivity server = null;
                try {
                    server = new WebServerActivity();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (simpleSwitch1.isChecked()) {
                    statusSwitch1 = simpleSwitch1.getTextOn().toString();
                    try {

                        server.start();
                        server.serve(null);
                        Thread.sleep(1000000000);
                        Log.d("myTag", "Error");
                    } catch (Exception e) {
                        Log.d("myTag", "Error");
                    }
                }
                else
                    statusSwitch1 = simpleSwitch1.getTextOff().toString();
                   // server.end();

                Toast.makeText(getApplicationContext(), "Connection:" + statusSwitch1 + "\n", Toast.LENGTH_LONG).show();

            }
        });

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

