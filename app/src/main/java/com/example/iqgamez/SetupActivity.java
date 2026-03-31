package com.example.iqgamez;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SetupActivity extends AppCompatActivity {

    EditText etName, etAge;
    Button btnStart;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        btnStart = findViewById(R.id.btnStart);

        prefManager = new PrefManager(this);

        btnStart.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String ageStr = etAge.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(ageStr)) {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Enter a valid age", Toast.LENGTH_SHORT).show();
                return;
            }

            prefManager.setUser(name, age);
            prefManager.setFirstTime(false);

            startActivity(new Intent(SetupActivity.this, MainActivity.class));
            finish();
        });
    }
}