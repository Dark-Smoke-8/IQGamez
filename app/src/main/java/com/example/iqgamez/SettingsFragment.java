package com.example.iqgamez;

import static android.app.ProgressDialog.show;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

    PrefManager prefManager;
    Switch musicSwitch, sfxSwitch;
    boolean musicCheck, sfxCheck;
    Button clearLeaderboard;
    String riddle, card, mult, easy, medium, hard, gameType, difficulty;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        prefManager = new PrefManager(this.getContext());
        musicSwitch = view.findViewById(R.id.musicSwitch);
        sfxSwitch = view.findViewById(R.id.sfxSwitch);
        clearLeaderboard = view.findViewById(R.id.btnLeaderboard);
        musicCheck = prefManager.getMusic();
        sfxCheck = prefManager.getSfx();

        musicSwitch.setChecked(musicCheck);
        sfxSwitch.setChecked(sfxCheck);


        riddle = "Riddle Solver";
        card = "Card Game";
        mult = "Multiplication Puzzle";
        easy = "easy";
        medium = "medium";
        hard = "hard";

        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                musicCheck = isChecked;
                prefManager.setMusic(musicCheck);
            }
        });

        sfxSwitch.setOnCheckedChangeListener((new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                sfxCheck = isChecked;
                prefManager.setSfx(sfxCheck);
            }
        }));

        clearLeaderboard.setOnClickListener(v -> {
            new AlertDialog.Builder(this.getContext())
                    .setMessage("This will delete ALL Leaderboard Scores! Continue?")
                    .setPositiveButton("Continue", (d, i) -> clearAll())
                    .setNegativeButton("Quit", (d, i) -> d.dismiss())
                    .show();
        });

        return view;
    }

    public void clearAll()
    {
        for (int i=0; i<3; i++)
        {
            if (i == 0)
            {
                gameType = riddle;
            }
            else if (i == 1)
            {
                gameType = mult;
            }
            else
            {
                gameType = card;
            }

            for (int j=0; j<3; j++)
            {
                if (j == 0)
                {
                    difficulty = easy;
                }
                else if (j == 1)
                {
                    difficulty = medium;
                }
                else
                {
                    difficulty = hard;
                }
                prefManager.clearScore(gameType, difficulty);
            }
        }
    }
}
