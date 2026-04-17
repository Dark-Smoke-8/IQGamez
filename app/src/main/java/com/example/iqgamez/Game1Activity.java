package com.example.iqgamez;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Game1Activity extends AppCompatActivity {

    // UI elements
    TextView tvScore, tvTimer, tvQuestionNum, tvEquation, tvFeedback;
    EditText etAnswer;
    Button btnSubmit;

    // Game variables
    int score = 0;
    int questionIndex = 0;
    int correctAnswer = 0;
    int timePerQuestion = 20; // default Easy
    CountDownTimer countDownTimer;

    // To avoid duplicate questions
    ArrayList<String> usedEquations = new ArrayList<>();
    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1);

        // Link UI
        tvScore       = findViewById(R.id.tvScore);
        tvTimer       = findViewById(R.id.tvTimer);
        tvQuestionNum = findViewById(R.id.tvQuestionNum);
        tvEquation    = findViewById(R.id.tvEquation);
        tvFeedback    = findViewById(R.id.tvFeedback);
        etAnswer      = findViewById(R.id.etAnswer);
        btnSubmit     = findViewById(R.id.btnSubmit);

        // Get difficulty from Intent (sent by MainActivity)
        String difficulty = getIntent().getStringExtra("difficulty");
        if (difficulty == null) difficulty = "easy";

        // Set timer based on difficulty
        if (difficulty.equals("medium")) timePerQuestion = 12;
        else if (difficulty.equals("hard")) timePerQuestion = 7;
        else timePerQuestion = 20;

        loadNextQuestion();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });
    }

    void loadNextQuestion() {
        if (questionIndex >= 10) {
            endGame();
            return;
        }

        // Clear previous feedback and input
        tvFeedback.setText("");
        etAnswer.setText("");
        tvQuestionNum.setText("Question " + (questionIndex + 1) + " / 10");

        // Generate a unique equation
        generateEquation();

        // Start countdown
        startTimer();
    }

    void generateEquation() {
        int a, b;
        String equation;

        // Keep generating until we get one not used before
        do {
            a = random.nextInt(10) + 1; // 1 to 10
            b = random.nextInt(10) + 1;
            int product = a * b;
            correctAnswer = b;
            equation = a + " × ? = " + product;
        } while (usedEquations.contains(equation));

        usedEquations.add(equation);
        tvEquation.setText(equation);
    }

    void startTimer() {
        if (countDownTimer != null) countDownTimer.cancel();

        countDownTimer = new CountDownTimer(timePerQuestion * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("⏱ " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                // Time ran out — minus 5 points
                score -= 5;
                tvScore.setText("Score: " + score);
                tvFeedback.setText("Time up! Answer was " + correctAnswer);
                tvFeedback.setTextColor(getResources().getColor(android.R.color.holo_red_light));

                questionIndex++;
                // Wait 1.5 seconds then load next question
                tvEquation.postDelayed(() -> loadNextQuestion(), 1500);
            }
        }.start();
    }

    void checkAnswer() {
        if (countDownTimer != null) countDownTimer.cancel();

        String input = etAnswer.getText().toString().trim();

        if (input.isEmpty()) {
            tvFeedback.setText("Please enter an answer!");
            tvFeedback.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
            startTimer(); // restart timer
            return;
        }

        int playerAnswer = Integer.parseInt(input);

        if (playerAnswer == correctAnswer) {
            score += 10;
            tvFeedback.setText("Correct!");
            tvFeedback.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            tvFeedback.setText("Wrong! Answer was " + correctAnswer);
            tvFeedback.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }

        tvScore.setText("Score: " + score);
        questionIndex++;

        // Wait 1.5 seconds then load next question
        tvEquation.postDelayed(() -> loadNextQuestion(), 1500);
    }

    void endGame() {

        if (countDownTimer != null) countDownTimer.cancel();

        // TODO: uncomment when teammate creates ResultActivity
        // Intent intent = new Intent(this, ResultActivity.class);
        // intent.putExtra("score", score);
        // intent.putExtra("gameName", "Multiplication Puzzle");
        // startActivity(intent);
        // finish();

        // Temporary - just show final score as feedback
        tvFeedback.setText("Game Over! Final Score: " + score);
        tvFeedback.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
        btnSubmit.setEnabled(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (countDownTimer != null) countDownTimer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}