package com.example.androidlabs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class NameActivity extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        TextView textViewWelcome = findViewById(R.id.textViewWelcome);
        String name = getIntent().getStringExtra("name");
        textViewWelcome.setText("Welcome " + name + "!");

        Button buttonThankYou = findViewById(R.id.buttonThankYou);
        buttonThankYou.setOnClickListener(v -> {
            setResult(1);
            finish();
        });

        Button buttonNeverCallMe = findViewById(R.id.buttonNeverCallMe);
        buttonNeverCallMe.setOnClickListener(v -> {
            setResult(0);
            finish();
        });
    }
}
