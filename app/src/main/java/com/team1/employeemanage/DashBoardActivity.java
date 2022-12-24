package com.team1.employeemanage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;

public class DashBoardActivity extends AppCompatActivity {

     RelativeLayout btn_Task, btn_Employee, btn_Chat, btn_Profile, btn_TK, btn_Setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        getSupportActionBar().hide();

        addControls();

        addEvents();
    }

    private void addEvents() {
        btn_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });

        btn_Task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, TaskActivity.class);
                startActivity(intent);
            }
        });

        btn_Employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, EmployeeActivity.class);
                startActivity(intent);
            }
        });

        btn_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        btn_TK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, TimeKeepingActivity.class);
                startActivity(intent);

            }
        });

        btn_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        btn_Chat = this.<RelativeLayout>findViewById(R.id.button_Chat);
        btn_Employee = this.<RelativeLayout>findViewById(R.id.button_Employee);
        btn_Setting = this.<RelativeLayout>findViewById(R.id.button_Setting);
        btn_Profile = this.<RelativeLayout>findViewById(R.id.button_Profile);
        btn_Task = this.<RelativeLayout>findViewById(R.id.button_Task);
        btn_TK = this.<RelativeLayout>findViewById(R.id.button_TimeKeeping);

    }
}