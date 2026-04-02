package com.example.iqgamez;

import androidx.appcompat.app.AppCompatActivity;

public class Riddle extends AppCompatActivity {
        String question;
        String[] options;
        int correctIndex;

        public Riddle(String q, String[] opt, int correct) {
            question = q;
            options = opt;
            correctIndex = correct;
        }

}
