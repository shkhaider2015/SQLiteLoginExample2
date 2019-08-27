package com.example.sqliteloginexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ProgressBar mProgressBar;
    private TextView mTextView;
    private boolean isLoggedIn = false;
    private int progressStatus = 0;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        progressTime();


    }

    private void init()
    {

        mProgressBar = findViewById(R.id.main_progress);
        mTextView = findViewById(R.id.main_text);
    }

    private void progressTime()
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {


            }
        }).start();

    }

    private void nextPage(boolean isLoggedIn)
    {
        if(isLoggedIn)
        {
            Fragment homeFragment = FragmentUtility.getFragmentByTagName(fragmentManager, "HomeFragment");
            if(homeFragment == null)
            {
                homeFragment = new HomeFragment();
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout_fragment, homeFragment, "HomeFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            FragmentUtility.printActivityFragmentList(fragmentManager);
        }
        else
        {
            Fragment loginFragment = FragmentUtility.getFragmentByTagName(fragmentManager, "LoginFragment");
            if(loginFragment == null)
            {
                loginFragment = new LoginFragment();
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout_fragment, loginFragment, "LoginFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            FragmentUtility.printActivityFragmentList(fragmentManager);
        }



    }

    private void operation()
    {
        SessionManager sessionManager = new SessionManager(this);
        String email = sessionManager.getEmail();
        if(email == null || email.isEmpty())
        {
            isLoggedIn = false;
        }
        else
        {
            isLoggedIn = true;
        }
    }
}
