package com.example.grouptunes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {
    Switch simpleSwitch1;
    Button submit;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    WebServerActivity server = null;

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
                final InputStream inputStream = getResources().openRawResource(R.raw.index);
                final InputStream inputStream2 = getResources().openRawResource(R.raw.style);
                final InputStream inputStream3 = getResources().openRawResource(R.raw.play);
                try {
                    server = new WebServerActivity(inputStream, inputStream2, inputStream3);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                statusSwitch1 = null;
                if (simpleSwitch1.isChecked()) {
                    statusSwitch1 = simpleSwitch1.getTextOn().toString();
                    try {

                        server.start();
                        server.serve(null);
                        Log.d("myTag", "Error");
                    } catch (Exception e) {
                        Log.d("myTag", "Error");
                    }
                }
                else if (!simpleSwitch1.isChecked()) {
                    statusSwitch1 = simpleSwitch1.getTextOff().toString();
                    server.stop();
                }

                Toast.makeText(getApplicationContext(), "Connection:" + statusSwitch1 + "\n", Toast.LENGTH_LONG).show();

            }
        });

    }


}

