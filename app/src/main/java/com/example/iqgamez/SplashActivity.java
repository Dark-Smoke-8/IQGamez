package com.example.iqgamez;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    PrefManager prefManager;
    TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appName = findViewById(R.id.appName);
        prefManager = new PrefManager(this);

        // Fade In
        appName.animate()
                .alpha(1f)
                .setDuration(1200)
                .withEndAction(() -> {

                    // Fade Out
                    appName.animate()
                            .alpha(0f)
                            .setDuration(800)
                            .withEndAction(() -> {
                                navigateNext();
                            });

                });
    }

    private void navigateNext() {
        if (prefManager.isFirstTime()) {
            startActivity(new Intent(this, SetupActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }
}