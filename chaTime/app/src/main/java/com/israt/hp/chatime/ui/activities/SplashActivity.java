package com.israt.hp.chatime.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.israt.hp.chatime.R;
import com.israt.hp.chatime.utils.NetworkConnectionUtil;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_MS = 1500;
    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandler = new Handler();

        mRunnable = new Runnable() {
            @Override
            public void run() {
                // check if user is already logged in or not
                if(NetworkConnectionUtil.isConnectedToInternet(SplashActivity.this)) {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        // if logged in redirect the user to user listing activity
                        UserListingActivity.startActivity(SplashActivity.this);
                    } else {
                        // otherwise redirect the user to login activity
                        LoginActivity.startIntent(SplashActivity.this);
                    }
                    finish();
                }
                else
                {
                    Toast.makeText(SplashActivity.this,"Please activate internet",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        };

        mHandler.postDelayed(mRunnable, SPLASH_TIME_MS);
    }

    /*@Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, SPLASH_TIME_MS);
    }*/
}
