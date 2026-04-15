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
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    VideoView videoView;
    TextView tvResult;
    LinearLayout buttonLayout;

    MediaPlayer resultSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        videoView = findViewById(R.id.videoView);
        tvResult = findViewById(R.id.tvResult);
        buttonLayout = findViewById(R.id.buttonLayout);

        Button btnHome = findViewById(R.id.btnHome);
        Button btnPlayAgain = findViewById(R.id.btnPlayAgain);

        int score = getIntent().getIntExtra("score", 0);

        boolean isWin = (score == 10);

        String videoPath;

        if (isWin) {
            videoPath = "android.resource://" + getPackageName() + "/" + R.raw.win_anim;
            resultSound = MediaPlayer.create(this, R.raw.win_sound);
            tvResult.setText("YOU WON");
        } else {
            videoPath = "android.resource://" + getPackageName() + "/" + R.raw.lose_anim;
            resultSound = MediaPlayer.create(this, R.raw.lose_sound);
            tvResult.setText("YOU LOST");
        }

        videoView.setVideoPath(videoPath);
        videoView.start();
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);

            float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
            float screenRatio = videoView.getWidth() / (float) videoView.getHeight();
            float scale = videoRatio / screenRatio;

            if (scale >= 1f) {
                videoView.setScaleX(scale);
            } else {
                videoView.setScaleY(1f / scale);
            }
        });

        videoView.setOnCompletionListener(mp -> videoView.start());
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
