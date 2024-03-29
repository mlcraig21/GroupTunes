package com.example.grouptunes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import android.media.MediaPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {
    Switch simpleSwitch1;
    Button submit;
    MediaPlayer mediaPlayer;
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
                    server = new WebServerActivity(MainActivity.this,inputStream, inputStream2, inputStream3);
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
        });}
    /** Called when the user taps the Play button */
    public void songPlay(View view) {
        // Do something in response to button
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.sound_file_1);
        }
        mediaPlayer.start();
        mediaPlayer
                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {

                        String filePath2 = null;
                        File file = null;
                        FileInputStream inputStream = null;

                        //set the filePath
                        try {
                            filePath2 = MainActivity.this.getFilesDir() + File.separator + file.getName();
                            file = new File(filePath2);
                            if (file.exists()) {
                                inputStream = new FileInputStream(file);
                                if (inputStream.getFD().valid()) {
                                    System.out.println("Valid!");
                                }
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            System.exit(-1);
                        }

                        //set Mediaplayer's datasource
                        if (file.exists()) {
                            try {
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.setDataSource(inputStream.getFD());
                                inputStream.close();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                                System.exit(-1);
                            }

                            try {
                                mediaPlayer.prepare();
                            } catch (IllegalStateException e) {

                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        //if the player is not running
                        if (!mediaPlayer.isPlaying()) {
                            //start the player
                            mediaPlayer.start();
                            Toast.makeText(MainActivity.this,
                                    "mediaPlayer.start()", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });


    }


    /** Called when the user taps the Pause button */
    public void songPause(View view) {
        // Do something in response to button
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }
    /** Called when the user taps the Stop button */
    public void songStop(View view) {
        // Do something in response to button
        stopPlayer();
    }

    private void stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;

            Toast.makeText(MainActivity.this, "MediaPlayer released", Toast.LENGTH_SHORT).show();
        }
    }

}