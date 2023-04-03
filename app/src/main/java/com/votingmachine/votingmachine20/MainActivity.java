package com.votingmachine.votingmachine20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private static int SPLASH_SCREEN_TIME_OUT=3200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(MainActivity.this,DashBoard.class);
                startActivity(i);
                finish();

            }
        }, SPLASH_SCREEN_TIME_OUT);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}