package com.example.iqgamez;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class GameLeaderboard extends AppCompatActivity {
    TextView scoreTextView;
    PrefManager prefManager;
    Scoreboard score;
    String difficulty, scoreText, gameType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);
        scoreTextView = findViewById(R.id.leaderboardScores);

        prefManager = new PrefManager(this);
        gameType = getIntent().getStringExtra("gameType");
        difficulty = getIntent().getStringExtra("difficulty");
        score = new Scoreboard(gameType, difficulty);
        scoreText = "";

        for (int i = 0; i < score.getScores().size(); i++)
        {
            scoreText += (i + 1) + ".\t" + Integer.toString(score.getScores(i)) + "\n";
        }

        scoreTextView.setText(scoreText);
    }
}
