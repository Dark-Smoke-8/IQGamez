package com.example.iqgamez;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class Game1Activity extends AppCompatActivity {

    // UI elements
    View rootLayout;
    TextView tvScore, tvTimer, tvQuestionNum, tvEquation, tvFeedback;
    EditText etAnswer;
    Button btnSubmit;
    ImageView btnBack;

    // Sound variables
    MediaPlayer mpBackground;
    MediaPlayer mpCorrect;
    MediaPlayer mpWrong;
    MediaPlayer mpTick;
    BroadcastReceiver batteryLowReceiver;

    // Game variables
    int score = 0;
    int questionIndex = 0;
    int correctAnswer = 0;
    int timePerQuestion = 20;
    String difficulty = "easy";
    CountDownTimer countDownTimer;
    Random random = new Random();
    ArrayList<String> usedEquations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1);

        // Connect Java variables to XML views
        rootLayout = findViewById(R.id.rootLayout);
        tvScore       = findViewById(R.id.tvScore);
        tvTimer       = findViewById(R.id.tvTimer);
        tvQuestionNum = findViewById(R.id.tvQuestionNum);
        tvEquation    = findViewById(R.id.tvEquation);
        tvFeedback    = findViewById(R.id.tvFeedback);
        etAnswer      = findViewById(R.id.etAnswer);
        btnSubmit     = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);

        // Get difficulty from previous screen
        difficulty = getIntent().getStringExtra("difficulty");
        if (difficulty == null) {
            difficulty = "easy";
        }

        // Set timer based on difficulty
        if (difficulty.equals("medium")) {
            timePerQuestion = 15;
        } else if (difficulty.equals("hard")) {
            timePerQuestion = 10;
        } else {
            timePerQuestion = 20;
        }

        // Load the first question
        loadNextQuestion();

        // Set up submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        //set up back button
        btnBack.setOnClickListener(v -> showExitDialog());

        // Handle phone back button
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitDialog();
            }
        });

        initSounds();
    }


    // Called after every question to move to the next one
    void loadNextQuestion() {
        if (questionIndex >= 10) {
            endGame();
            return;
        }

        // Reset background for next question
        stopFlashingEffect();

        // Clear screen for next question
        tvFeedback.setText("");
        etAnswer.setText("");
        tvQuestionNum.setText("Question " + (questionIndex + 1) + " / 10");

        // Generate a new equation and start the timer
        generateEquation();
        startTimer();
    }

    // Generates a random math equation based on difficulty
    void generateEquation() {
        String equation;
        int a, b;

        do {
            a = getRandomNumber();
            b = getRandomNumber();

            if (difficulty.equals("hard")) {
                int operation = random.nextInt(3);

                if (operation == 0) {
                    // e.g. 6 x ? = 48
                    correctAnswer = b;
                    equation = a + " x ? = " + (a * b);

                } else if (operation == 1) {
                    // e.g. 48 / 6 = ?
                    correctAnswer = a;
                    equation = (a * b) + " / " + b + " = ?";

                } else {
                    // e.g. ? x 6 = 48
                    correctAnswer = a;
                    equation = "? x " + b + " = " + (a * b);
                }

            } else if (difficulty.equals("medium")) {
                int operation = random.nextInt(2);

                if (operation == 0) {
                    // e.g. 6 x ? = 48
                    correctAnswer = b;
                    equation = a + " x ? = " + (a * b);

                } else {
                    // e.g. 48 / 6 = ?
                    correctAnswer = a;
                    equation = (a * b) + " / " + b + " = ?";
                }

            } else {
                // Easy: multiplication only
                // e.g. 6 x ? = 48
                correctAnswer = b;
                equation = a + " x ? = " + (a * b);
            }

        } while (usedEquations.contains(equation));

        usedEquations.add(equation);
        tvEquation.setText(equation);
    }

    // Returns a random number based on difficulty range
    int getRandomNumber() {
        if (difficulty.equals("hard")) {
            return random.nextInt(50) + 1; // 1 to 50
        } else if (difficulty.equals("medium")) {
            return random.nextInt(20) + 1; // 1 to 20
        } else {
            return random.nextInt(10) + 1; // 1 to 10
        }
    }

    // Starts the countdown timer for each question
    void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(timePerQuestion * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000);
                tvTimer.setText(secondsLeft + "s");

                // Flash when time is low
                if (secondsLeft <= 5) {
                    startFlashingEffect();
                    // Play tick sound when time is running out
                    if (mpTick != null) {
                        mpTick.seekTo(0);
                        mpTick.start();
                    }
                }
            }

            @Override
            public void onFinish() {
                stopFlashingEffect();

                // Time ran out
                // Only deduct points if score is above 0
                if (score > 0) {
                    score = score - 5;
                }
                tvScore.setText("Score: " + score);

                tvFeedback.setText("Time up! Answer was " + correctAnswer);
                tvFeedback.setTextColor(getResources().getColor(android.R.color.holo_red_light));

                questionIndex = questionIndex + 1;

                // Wait 1.5 seconds then load next question
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextQuestion();
                    }
                }, 1500);
            }

        }.start();
    }

    // Checks if the player's answer is correct
    void checkAnswer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        stopFlashingEffect();

        String input = etAnswer.getText().toString().trim();

        if (input.isEmpty()) {
            tvFeedback.setText("Please enter an answer!");
            tvFeedback.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
            startTimer();
            return;
        }

        int playerAnswer = Integer.parseInt(input);

        if (playerAnswer == correctAnswer) {
            score = score + 10;
            tvFeedback.setText("Correct!");
            tvFeedback.setTextColor(getResources().getColor(android.R.color.holo_green_light));
            if (mpCorrect != null) {
                mpCorrect.seekTo(0);
                mpCorrect.start();
            }
        } else {
            // Only deduct points if score is above 0
            if (score > 0) {
                score = score - 5;
            }
            tvFeedback.setText("Wrong! Answer was " + correctAnswer);
            tvFeedback.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            if (mpWrong != null) {
                mpWrong.seekTo(0);
                mpWrong.start();
            }
        }

        tvScore.setText("Score: " + score);
        questionIndex = questionIndex + 1;

        // Wait 1.5 seconds then load next question
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadNextQuestion();
            }
        }, 1500);
    }

    // Makes the screen flash red when time is low
    void startFlashingEffect() {
        rootLayout.setBackgroundColor(Color.RED);

        rootLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                rootLayout.setBackgroundColor(Color.parseColor("#1a1a2e"));
            }
        }, 300);
    }

    // Resets the background color
    void stopFlashingEffect() {
        rootLayout.setBackgroundColor(Color.parseColor("#1a1a2e"));
    }

    // Shows a message when the user wants to quit the game
    void showExitDialog() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        new AlertDialog.Builder(this)
                .setTitle("Quit Game")
                .setMessage("Are you sure you want to quit?")
                .setPositiveButton("Quit", (dialog, which) -> finish())
                .setNegativeButton("Cancel", (dialog, which) -> startTimer())
                .show();
    }

    void initSounds() {
        mpBackground = MediaPlayer.create(this, R.raw.gamemulti_musicbg);
        mpBackground.setLooping(true);
        mpBackground.setVolume(0.5f, 0.5f);
        mpBackground.start();

        mpCorrect = MediaPlayer.create(this, R.raw.multi_correct_sound);
        mpWrong = MediaPlayer.create(this, R.raw.multi_wrong_sound);
        mpTick = MediaPlayer.create(this, R.raw.multi_clocktimer_sound);
    }

    // This method saves the high score for the current difficulty
    void saveHighScore() {
        // Save high score to SharedPreferences
        SharedPreferences prefs = getSharedPreferences("BrainZonePrefs", MODE_PRIVATE);

        // Example keys for my understanding
        // score_multiplication_easy
        // score_multiplication_medium
        // score_multiplication_hard
        SharedPreferences.Editor editor = prefs.edit();

        // Get the current saved high score for this difficulty
        String key = "score_multiplication_" + difficulty;
        int savedHighScore = prefs.getInt(key, 0);

        // Only save if current score is higher
        if (score > savedHighScore) {
            editor.putInt(key, score);
            editor.apply();
        }
    }

    // Called when all 10 questions are done
    void endGame() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        saveHighScore();

        // Send score to ResultActivity
        Intent intent = new Intent(Game1Activity.this, ResultActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("gameName", "Multiplication Puzzle");
        intent.putExtra("isWin", score >= 50);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause timer when leaving the screen
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        // Pause background music
        if (mpBackground != null && mpBackground.isPlaying()) {
            mpBackground.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume background music when coming back to the game
        if (mpBackground != null && !mpBackground.isPlaying()) {
            mpBackground.start();
        }

        // Register battery low receiver
        batteryLowReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // When battery is low, pause the game and warn the player
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }

                // Stop background music to save battery
                if (mpBackground != null && mpBackground.isPlaying()) {
                    mpBackground.pause();
                }

                // Show a warning dialog to the player
                new AlertDialog.Builder(Game1Activity.this)
                        .setTitle("Low Battery!")
                        .setMessage("Your battery is low. The game has been paused. Please charge your device!")
                        .setPositiveButton("Resume", (dialog, which) -> {
                            // Resume music and restart timer
                            if (mpBackground != null) {
                                mpBackground.start();
                            }
                            startTimer();
                        })
                        .setNegativeButton("Quit", (dialog, which) -> finish())
                        .show();
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_LOW);
        registerReceiver(batteryLowReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Unregister battery receiver to avoid memory leaks
        if (batteryLowReceiver != null) {
            unregisterReceiver(batteryLowReceiver);
        }

        // Release all sounds to free up memory
        if (mpBackground != null) {
            mpBackground.stop();
            mpBackground.release();
            mpBackground = null;
        }
        if (mpCorrect != null) {
            mpCorrect.release();
            mpCorrect = null;
        }
        if (mpWrong != null) {
            mpWrong.release();
            mpWrong = null;
        }
        if (mpTick != null) {
            mpTick.release();
            mpTick = null;
        }
    }
}