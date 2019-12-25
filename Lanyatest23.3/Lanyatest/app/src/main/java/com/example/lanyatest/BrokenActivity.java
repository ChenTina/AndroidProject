package com.example.lanyatest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class BrokenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);
    }
}
