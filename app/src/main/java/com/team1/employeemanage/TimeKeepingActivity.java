package com.team1.employeemanage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TimeKeepingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_keeping);

        getSupportActionBar().hide();
    }
}