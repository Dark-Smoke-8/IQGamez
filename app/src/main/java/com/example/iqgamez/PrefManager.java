package com.example.iqgamez;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    private static final String PREF_NAME = "BrainZonePrefs";

    private static final String KEY_FIRST_TIME = "isFirstTime";
    private static final String KEY_NAME = "playerName";
    private static final String KEY_AGE = "playerAge";

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

    public int getAge() {
        return pref.getInt(KEY_AGE, 0);
    }
}