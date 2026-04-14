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
import java.util.Collections;
import java.util.List;

public class RiddleGameActivity extends AppCompatActivity {

    TextView tvQuestion, tvScore, tvTimer;
    Button opt1, opt2, opt3, opt4;
    ImageView btnBack;

    int score = 0;
    int currentQuestion = 0;
    CountDownTimer timer;
    TextView tvCorrectAnswer;
    boolean answered = false;
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
        tvCorrectAnswer = findViewById(R.id.tvCorrectAnswer);

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

            riddles.add(new Riddle("What has to be broken before you can use it?",
                    new String[]{"Coconut", "Egg", "Glass", "Seal"}, 1));

            riddles.add(new Riddle("What is always in front of you but can’t be seen?",
                    new String[]{"Air", "The future", "Light", "Your shadow"}, 1));

            riddles.add(new Riddle("What has a face and two hands but no arms or legs?",
                    new String[]{"Clock", "Watch", "Statue", "Robot"}, 0));

            riddles.add(new Riddle("What goes up but never comes down?",
                    new String[]{"Smoke", "Balloon", "Age", "Temperature"}, 2));

            riddles.add(new Riddle("What has many teeth but cannot bite?",
                    new String[]{"Saw", "Zipper", "Comb", "Gear"}, 2));

            riddles.add(new Riddle("What gets wetter the more it dries?",
                    new String[]{"Cloth", "Sponge", "Towel", "Mop"}, 2));

            riddles.add(new Riddle("What has legs but doesn’t walk?",
                    new String[]{"Table", "Chair", "Bed", "Cabinet"}, 0));

            riddles.add(new Riddle("What kind of tree can you carry in your hand?",
                    new String[]{"Palm", "Pine", "Oak", "Maple"}, 0));

            riddles.add(new Riddle("What has one eye but cannot see?",
                    new String[]{"Storm", "Needle", "Cyclone", "Camera"}, 1));

            riddles.add(new Riddle("What is full of holes but still holds water?",
                    new String[]{"Net", "Sponge", "Basket", "Cloth"}, 1));
        }

        else if (difficulty.equals("medium")) {

            riddles.add(new Riddle("I speak without a mouth and hear without ears. What am I?",
                    new String[]{"Wind", "Radio", "Echo", "Thought"}, 2));

            riddles.add(new Riddle("What can travel around the world while staying in the same corner?",
                    new String[]{"Satellite", "Stamp", "Compass", "Internet"}, 1));

            riddles.add(new Riddle("The more you take, the more you leave behind. What are they?",
                    new String[]{"Memories", "Footsteps", "Marks", "Time"}, 1));

            riddles.add(new Riddle("What has a neck but no head and two arms but no hands?",
                    new String[]{"Coat", "Sweater", "Shirt", "Jacket"}, 2));

            riddles.add(new Riddle("What runs but never walks, has a mouth but never talks?",
                    new String[]{"Stream", "River", "Clock", "Engine"}, 1));

            riddles.add(new Riddle("What can fill a room but takes up no space?",
                    new String[]{"Air", "Light", "Heat", "Sound"}, 1));

            riddles.add(new Riddle("What has a heart that doesn’t beat?",
                    new String[]{"Rock", "Artichoke", "Statue", "Robot"}, 1));

            riddles.add(new Riddle("What belongs to you but is used more by others?",
                    new String[]{"Phone", "Name", "House", "Car"}, 1));

            riddles.add(new Riddle("What has keys but can’t open locks?",
                    new String[]{"Piano", "Keyboard", "Map", "Calculator"}, 0));

            riddles.add(new Riddle("The more of this there is, the less you see. What is it?",
                    new String[]{"Fog", "Darkness", "Smoke", "Mist"}, 1));
        }

        else if (difficulty.equals("hard")) {

            riddles.add(new Riddle("I have cities, but no houses. I have rivers, but no water. What am I?",
                    new String[]{"Globe", "Atlas", "Map", "Geography book"}, 2));

            riddles.add(new Riddle("The person who makes it doesn’t need it. The person who buys it doesn’t use it. The person who uses it doesn’t know they’re using it. What is it?",
                    new String[]{"Coffin", "Life insurance", "Grave", "Funeral cloth"}, 0));

            riddles.add(new Riddle("What has one eye but cannot see?",
                    new String[]{"Hurricane", "Storm", "Needle", "Cyclone"}, 2));

            riddles.add(new Riddle("I’m light as a feather, yet the strongest person can’t hold me for very long. What am I?",
                    new String[]{"Air", "Breath", "Thought", "Smoke"}, 1));

            riddles.add(new Riddle("What can fill a room but takes up no space?",
                    new String[]{"Light", "Sound", "Heat", "Air"}, 0));

            riddles.add(new Riddle("What has a neck but no head, two arms but no hands?",
                    new String[]{"Jacket", "Shirt", "Sweater", "Coat"}, 1));

            riddles.add(new Riddle("What runs but never walks, has a mouth but never talks?",
                    new String[]{"River", "Clock", "Engine", "Stream"}, 0));

            riddles.add(new Riddle("The more you remove from it, the bigger it becomes. What is it?",
                    new String[]{"Tunnel", "Gap", "Hole", "Pit"}, 2));

            riddles.add(new Riddle("I have branches, but no fruit, trunk, or leaves. What am I?",
                    new String[]{"Company", "Government", "Bank", "Organization"}, 2));

            riddles.add(new Riddle("Forward I am heavy, but backward I am not. What am I?",
                    new String[]{"Iron", "Lead", "Ton", "Rock"}, 2));
        }

        Collections.shuffle(riddles);
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
    private void highlightAnswers(int selected, int correct) {

        Button[] buttons = {opt1, opt2, opt3, opt4};

        for (int i = 0; i < buttons.length; i++) {
            if (i == correct) {
                buttons[i].setBackgroundColor(Color.GREEN);
            } else if (i == selected) {
                buttons[i].setBackgroundColor(Color.RED);
            }
        }
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
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("score", score);
        startActivity(intent);
        if (gamesound != null) {
            gamesound.stop();
            gamesound.release();
        }
        finish();
    }
}