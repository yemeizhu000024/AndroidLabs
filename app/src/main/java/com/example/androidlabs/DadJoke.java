package com.example.androidlabs;


import android.os.Bundle;

public class DadJoke extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dad_joke);
        setupToolbarAndDrawer();
    }
}
