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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        btnEasy = findViewById(R.id.btnEasy);
        btnMedium = findViewById(R.id.btnMedium);
        btnHard = findViewById(R.id.btnHard);
        btnBack = findViewById(R.id.btnBack);
        isLeaderboard = getIntent().getBooleanExtra("isLeaderboard", false);

        btnBack.setOnClickListener(v -> finish());
        btnEasy.setOnClickListener(v -> startGame("easy"));
        btnMedium.setOnClickListener(v -> startGame("medium"));
        btnHard.setOnClickListener(v -> startGame("hard"));
    }

    private void startGame(String difficulty) {
        Intent intent;

        if (isLeaderboard)
        {intent = new Intent(this, RiddleGameLeaderboard.class);}
        else
        {intent = new Intent(this, RiddleGameActivity.class);}

        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
    }
}
