package com.team1.employeemanage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class CreateAccountActivity extends AppCompatActivity {

    EditText firstname, lastname,
                email, password, repassword;

    Button signin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getSupportActionBar().hide();

        addControls();

        addEvents();
    }

    private void addEvents() {

    }

    private void addControls() {

    }
}