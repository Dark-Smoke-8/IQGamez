package com.example.iqgamez;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class RiddleGameActivity extends AppCompatActivity {

    TextView tvQuestion, tvScore, tvTimer;
    Button opt1, opt2, opt3, opt4;
    ImageView btnBack;

    int score = 0;
    int currentQuestion = 0;
    CountDownTimer timer;

    List<Riddle> riddles = new ArrayList<>();
    String difficulty;
    MediaPlayer gamesound;
    View rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riddle_game);

        // GET DIFFICULTY
        difficulty = getIntent().getStringExtra("difficulty");

        initViews();
        loadRiddles();
        showQuestion();
        startTimer();
        rootLayout = findViewById(R.id.rootLayout);

// background ticking sound
        gamesound = MediaPlayer.create(this, R.raw.game_sound);
        gamesound.setLooping(true);
        gamesound.start();
    }

    private void initViews() {
        tvQuestion = findViewById(R.id.tvQuestion);
        tvScore = findViewById(R.id.tvScore);
        tvTimer = findViewById(R.id.tvTimer);

        opt1 = findViewById(R.id.opt1);
        opt2 = findViewById(R.id.opt2);
        opt3 = findViewById(R.id.opt3);
        opt4 = findViewById(R.id.opt4);

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> showExitDialog());
    }

    private void loadRiddles() {
        riddles.clear();

        if (difficulty.equals("easy")) {
            riddles.add(new Riddle("What has keys but can't open locks?",
                    new String[]{"Piano", "Door", "Car", "Box"}, 0));
        }

        // Add 10 per difficulty
    }

    private void showQuestion() {
        if (currentQuestion >= 10) {
            endGame();
            return;
        }

        Riddle r = riddles.get(currentQuestion);

        tvQuestion.setText(r.question);
        opt1.setText(r.options[0]);
        opt2.setText(r.options[1]);
        opt3.setText(r.options[2]);
        opt4.setText(r.options[3]);

        setListeners(r);
    }

    private void setListeners(Riddle r) {

        View.OnClickListener listener = v -> {
            int selected = -1;

            if (v == opt1) selected = 0;
            if (v == opt2) selected = 1;
            if (v == opt3) selected = 2;
            if (v == opt4) selected = 3;

            if (selected == r.correctIndex) {
                score++;
                tvScore.setText("Score: " + score);
            }

            currentQuestion++;
            resetTimer();
            showQuestion();
        };

        opt1.setOnClickListener(listener);
        opt2.setOnClickListener(listener);
        opt3.setOnClickListener(listener);
        opt4.setOnClickListener(listener);
    }

    private void startTimer() {
        timer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000);
                tvTimer.setText("" + seconds);

                if (seconds <= 5) {
                    startFlashingEffect();
                }
            }

            public void onFinish() {
                stopFlashingEffect();
                currentQuestion++;
                showQuestion();
            }
        }.start();
    }

    private void startFlashingEffect() {
        rootLayout.animate()
                .alpha(0.5f)
                .setDuration(300)
                .withEndAction(() -> rootLayout.animate().alpha(1f).setDuration(300))
                .start();
    }

    private void stopFlashingEffect() {
        rootLayout.setAlpha(1f);
    }

    private void resetTimer() {
        timer.cancel();
        startTimer();
    }

    private void showExitDialog() {
        timer.cancel();
        if (gamesound != null) {
            gamesound.stop();
            gamesound.release();
        }
        new AlertDialog.Builder(this)
                .setMessage("You will lose your progress. Quit?")
                .setPositiveButton("Quit", (d, i) -> finish())
                .setNegativeButton("Continue", (d, i) -> startTimer())
                .show();
    }

    private void endGame() {
        Intent intent = new Intent(this, ResultAcitivity.class);
        intent.putExtra("score", score);
        startActivity(intent);
        if (gamesound != null) {
            gamesound.stop();
            gamesound.release();
        }
        finish();
    }
}