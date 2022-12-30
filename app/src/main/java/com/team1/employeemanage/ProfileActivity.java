package com.team1.employeemanage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileActivity extends AppCompatActivity {

    private String company;
    private String email;
    private String firstname;
    private String id;
    private String lastname;
    private String level;
    private String password;
    private static FirebaseFirestore mAuth;
    private TextView textView;

    private void initFireStore() {
        FirebaseApp.initializeApp(ProfileActivity.this);
        mAuth = FirebaseFirestore.getInstance();
    }

    private void getTaskDetail() {
        DocumentReference docRef = mAuth.collection("Users").document(LoginActivity.UserID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        company = "Company: " + document.get("company",String.class);
                        id = "Id: " + document.get("id",String.class);
                        level = "Level: " + document.get("level",String.class);
                        firstname = "Firstname: " + document.get("firstname",String.class);
                        lastname = "Lastname: " + document.get("lastname",String.class);
                        email = "Email: " + document.get("email",String.class);
                        password = "Password: " + document.get("password",String.class);
                    }
                }
            }
        });
    }

    private void showProfile() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView = (TextView) findViewById(R.id.text_Profile_Id);
                textView.setText(id);
                textView = (TextView) findViewById(R.id.text_Profile_Company);
                textView.setText(company);
                textView = (TextView) findViewById(R.id.text_Profile_Firstname);
                textView.setText(firstname);
                textView = (TextView) findViewById(R.id.text_Profile_Lastname);
                textView.setText(lastname);
                textView = (TextView) findViewById(R.id.text_Profile_Level);
                textView.setText(level);
                textView = (TextView) findViewById(R.id.text_Profile_Email);
                textView.setText(email);
                textView = (TextView) findViewById(R.id.text_Profile_Password);
                textView.setText(password);
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initFireStore();
        getTaskDetail();
        showProfile();
        getSupportActionBar().hide();

    }
}