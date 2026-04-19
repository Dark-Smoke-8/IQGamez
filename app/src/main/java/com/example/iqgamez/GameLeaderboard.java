package com.example.iqgamez;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class GameLeaderboard extends AppCompatActivity {
    TextView scoreTextView;
    Button btnClear;
    PrefManager prefManager;
    Scoreboard score;
    String difficulty, scoreText, gameType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);
        scoreTextView = findViewById(R.id.leaderboardScores);
        btnClear = findViewById(R.id.buttonClear);

        prefManager = new PrefManager(this);
        gameType = getIntent().getStringExtra("gameType");
        difficulty = getIntent().getStringExtra("difficulty");
        score = prefManager.getScoreBoard(gameType, difficulty);
        scoreText = "";
        Log.d("DEBUG", "Score list size: " + score.getScores().size());

        for (int i = 0; i < score.getScores().size(); i++)
        {
            scoreText += (i + 1) + ".\t" + Integer.toString(score.getScores(i)) + "\n";
        }

        scoreTextView.setText(scoreText);

        btnClear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                prefManager.clearScore(gameType, difficulty);
                score = prefManager.getScoreBoard(gameType, difficulty);
                scoreText = "";
                for (int i = 0; i < score.getScores().size(); i++)
                {
                    scoreText += (i + 1) + ".\t" + Integer.toString(score.getScores(i)) + "\n";
                }

                scoreTextView.setText(scoreText);
            }
        });
    }
}
