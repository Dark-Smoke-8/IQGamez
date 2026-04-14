package com.example.iqgamez;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    ImageView ivAnimation;
    TextView tvResult;
    LinearLayout buttonLayout;

    MediaPlayer resultSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        ivAnimation = findViewById(R.id.ivAnimation);
        tvResult = findViewById(R.id.tvResult);
        buttonLayout = findViewById(R.id.buttonLayout);

        Button btnHome = findViewById(R.id.btnHome);
        Button btnPlayAgain = findViewById(R.id.btnPlayAgain);

        int score = getIntent().getIntExtra("score", 0);

        boolean isWin = (score == 10);

        if (isWin) {
            ivAnimation.setImageResource(R.raw.win_anim);
            resultSound = MediaPlayer.create(this, R.raw.win_sound);
            tvResult.setText("YOU WON");
        } else {
            ivAnimation.setImageResource(R.raw.lose_anim);
            resultSound = MediaPlayer.create(this, R.raw.lose_sound);
            tvResult.setText("YOU LOST");
        }

        resultSound.setLooping(true);
        resultSound.start();

        new Handler().postDelayed(() -> {
            tvResult.setVisibility(View.VISIBLE);
            buttonLayout.setVisibility(View.VISIBLE);
        }, 5000);

        btnHome.setOnClickListener(v -> {
            stopSound();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        btnPlayAgain.setOnClickListener(v -> {
            stopSound();
            startActivity(new Intent(this, DifficultyActivity.class));
            finish();
        });
    }

    private void stopSound() {
        if (resultSound != null) {
            resultSound.stop();
            resultSound.release();
        }
    }
}
