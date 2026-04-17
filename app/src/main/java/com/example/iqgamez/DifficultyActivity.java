package com.example.iqgamez;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DifficultyActivity extends AppCompatActivity {

    Button btnEasy, btnMedium, btnHard;
    ImageView btnBack;
    boolean isLeaderboard;

    String gameType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        btnEasy = findViewById(R.id.btnEasy);
        btnMedium = findViewById(R.id.btnMedium);
        btnHard = findViewById(R.id.btnHard);
        btnBack = findViewById(R.id.btnBack);
        isLeaderboard = getIntent().getBooleanExtra("isLeaderboard", false);
        gameType = getIntent().getStringExtra("gameType");

        btnBack.setOnClickListener(v -> finish());
        btnEasy.setOnClickListener(v -> startGame("easy"));
        btnMedium.setOnClickListener(v -> startGame("medium"));
        btnHard.setOnClickListener(v -> startGame("hard"));
    }

    private void startGame(String difficulty) {
        Intent intent;

        // If this screen was opened for leaderboard, keep old behavior
        if (isLeaderboard) {
            intent = new Intent(this, RiddleGameLeaderboard.class);
        } else {
            // Open different game based on gameType
            if ("multiplication".equals(gameType)) {
                intent = new Intent(this, Game1Activity.class);
            } else {
                // Default to riddle game
                intent = new Intent(this, RiddleGameActivity.class);
            }
        }

        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
    }
}
