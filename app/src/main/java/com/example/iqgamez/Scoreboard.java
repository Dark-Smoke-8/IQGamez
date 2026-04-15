package com.example.iqgamez;

import java.util.ArrayList;
import java.util.Collections;

public class Scoreboard
{
    public String game;
    public ArrayList<Integer> scoreList;
    public String difficulty;

    public Scoreboard(String game, String difficulty)
    {
        this.game = game;
        scoreList = new ArrayList<Integer>(5);
        this.difficulty = difficulty;
    }

    public Scoreboard(String game, String difficulty, int score0, int score1, int score2, int score3, int score4)
    {
        this.game = game;
        scoreList = new ArrayList<Integer>(5);
        scoreList.add(score0);
        scoreList.add(score1);
        scoreList.add(score2);
        scoreList.add(score3);
        scoreList.add(score4);
        scoreList.sort(Collections.reverseOrder());
        this.difficulty = difficulty;
    }

    public void addScore(int newScore)
    {
        scoreList.add(newScore);
        scoreList = sortScores(scoreList);
    }

    public ArrayList<Integer> sortScores(ArrayList<Integer> scoreList)
    {
        scoreList.sort(Collections.reverseOrder());
        if (scoreList.size() > 5) {
            scoreList.subList(5, scoreList.size()).clear();
        }
        return scoreList;
    }

    public ArrayList<Integer> getScores()
    {
        return scoreList;
    }

    public String getGame()
    {
        return game;
    }
    public String getDifficulty()
    {
        return difficulty;
    }
}
