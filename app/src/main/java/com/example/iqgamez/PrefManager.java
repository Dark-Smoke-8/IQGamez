package com.example.iqgamez;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

public class PrefManager {

    private static final String PREF_NAME = "BrainZonePrefs";
    private static final String KEY_FIRST_TIME = "isFirstTime";
    private static final String KEY_NAME = "playerName";
    private static final String KEY_AGE = "playerAge";
    private static final String KEY_MUSIC = "musicToggle";
    private static final String KEY_SFX = "sfxToggle";
    private static final String KEY_RIDDLE = "riddleSolverScore";
    private static final String KEY_CARD = "cardGameScore";
    private static final String KEY_MULT = "multiplicationPuzzleScore";

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public PrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setFirstTime(boolean isFirstTime) {
        editor.putBoolean(KEY_FIRST_TIME, isFirstTime);
        editor.apply();
    }

    public boolean isFirstTime() {
        return pref.getBoolean(KEY_FIRST_TIME, true);
    }

    public void setUser(String name, int age) {
        editor.putString(KEY_NAME, name);
        editor.putInt(KEY_AGE, age);
        editor.apply();
    }

    public String getName() {
        return pref.getString(KEY_NAME, "Player");
    }

    public void setMusic(boolean music){
        editor.putBoolean(KEY_MUSIC, music);
        editor.apply();
    }

    public boolean getMusic(){
        return  pref.getBoolean(KEY_MUSIC, true);
    }

    public void setSfx(boolean sfx){
        editor.putBoolean(KEY_SFX, sfx);
        editor.apply();
    }

    public boolean getSfx(){
        return  pref.getBoolean(KEY_SFX, true);
    }

    public int getAge() {
        return pref.getInt(KEY_AGE, 0);
    }

    public void setScores(Scoreboard scoreBoard)
    {
        ArrayList<Integer> newScores = scoreBoard.getScores();

        if (scoreBoard.getGame().equals("Riddle Solver"))
        {
            String name;
            for (int i = 0; i < newScores.size(); i++)
            {
                name = KEY_RIDDLE + scoreBoard.getDifficulty() + i;
                editor.putInt(name, newScores.get(i));
            }
            editor.apply();
        }

        else if (scoreBoard.getGame().equals("Card Game"))
        {
            String name;
            for (int i = 0; i < newScores.size(); i++)
            {
                name = KEY_CARD + scoreBoard.getDifficulty() + i;
                editor.putInt(name, newScores.get(i));
            }
            editor.apply();
        }

        else if (scoreBoard.getGame().equals("Multiplication Puzzle"))
        {
            String name;
            for (int i = 0; i < newScores.size(); i++)
            {
                name = KEY_MULT + scoreBoard.getDifficulty() + i;
                editor.putInt(name, newScores.get(i));
            }
            editor.apply();
        }

        else
        {
            Log.v("PrefManager", "Couldn't recognize game called: " + scoreBoard.getGame());
        }
    }

    public void addScore(String game, String difficulty, int newScore)
    {
        Scoreboard tempBoard = getScoreBoard(game, difficulty);
        tempBoard.addScore(newScore);
        setScores(tempBoard);
    }

    public Scoreboard getScoreBoard(String game, String difficulty)
    {
        Scoreboard scoreBoard = new Scoreboard(game, difficulty);
        if (game.equals("Riddle Solver"))
        {
            String name;
            for (int i = 0; i < 5; i++)
            {
                name = KEY_RIDDLE + difficulty + i;
                scoreBoard.addScore(pref.getInt(name, 0));
            }
        }

        else if (game.equals("Card Game"))
        {
            String name;
            for (int i = 0; i < 5; i++)
            {
                name = KEY_CARD + difficulty + i;
                scoreBoard.addScore(pref.getInt(name, 0));
            }
        }

        else if (game.equals("Multiplication Puzzle"))
        {
            String name;
            for (int i = 0; i < 5; i++)
            {
                name = KEY_MULT + difficulty + i;
                scoreBoard.addScore(pref.getInt(name, 0));
            }
        }

        else
        {
            Log.v("PrefManager", "Couldn't recognize game called: " + game);
        }

        return scoreBoard;
    }

    public void clearScore(String game, String difficulty)
    {
        Scoreboard tempBoard = new Scoreboard(game, difficulty, 0, 0, 0, 0, 0);
        setScores(tempBoard);
    }
}