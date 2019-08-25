package com.example.sqliteloginexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void init()
    {

        mProgressBar = findViewById(R.id.main_progress);
    }

    private void progressTime()
    {

        CountDownTimer countDownTimer = new CountDownTimer(50000, 500) {
            int total = 0;
            @Override
            public void onTick(long var)
            {
                total += var;
                mProgressBar.setProgress(total);

            }

            @Override
            public void onFinish()
            {
                //do something

            }
        }.start();
    }
}
