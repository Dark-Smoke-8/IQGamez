package com.example.iqgamez;

public class Riddle {

    public String question;
    public String[] options;
    public int correctIndex;

    public Riddle(String question, String[] options, int correctIndex) {
        this.question = question;
        this.options = options;
        this.correctIndex = correctIndex;
    }
}