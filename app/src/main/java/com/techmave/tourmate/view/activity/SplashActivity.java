package com.techmave.tourmate.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.techmave.tourmate.R;
import com.techmave.tourmate.utils.SharedPrefs;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPrefs prefs = new SharedPrefs(this);

        new Handler().postDelayed(() -> {

            Intent intent = prefs.getInstalled() ? new Intent(SplashActivity.this, DashboardActivity.class)
                    : new Intent(SplashActivity.this, LoginActivity.class);

            startActivity(intent);

        }, 1200);
    }
}
