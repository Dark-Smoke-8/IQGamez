package com.example.iqgamez;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class RiddleGameLeaderboard extends AppCompatActivity {
    TextView scoreTextView;
    PrefManager prefManager;
    Scoreboard riddleScore;
    String difficulty, scoreText, game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_riddle_game);
        scoreTextView = findViewById(R.id.leaderboardRiddleScores);

        prefManager = new PrefManager(this);
        game = "Riddle Solver";
        difficulty = getIntent().getStringExtra("difficulty");
        riddleScore = new Scoreboard(game, difficulty);

        for (int i = 0; i < 5; i++)
        {
            scoreText += i + ".\t" + Integer.toString(riddleScore.getScores(i)) + "\n";
        }

        scoreTextView.setText(scoreText);
    }
}
