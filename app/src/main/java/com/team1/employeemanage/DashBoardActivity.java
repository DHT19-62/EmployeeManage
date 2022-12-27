package com.team1.employeemanage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashBoardActivity extends AppCompatActivity {

     RelativeLayout btn_Task, btn_Employee, btn_Chat, btn_Profile, btn_TK, btn_Setting;

     private static FirebaseFirestore db;
     public static String CompanyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        getSupportActionBar().hide();

        addControls();
        addEvents();

        initFireStore();
        getCompanyID();
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

    private void initFireStore() {
        FirebaseApp.initializeApp(DashBoardActivity.this);
        db = FirebaseFirestore.getInstance();
    }

    private void getCompanyID() {
        DocumentReference documentReference = db.collection("Users").document(LoginActivity.UserID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    CompanyID = document.get("companyId",String.class);
                    Log.d("CompanyID",CompanyID);
                }
            }
        });
    }
}