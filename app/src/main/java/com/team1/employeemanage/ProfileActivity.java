package com.team1.employeemanage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team1.employeemanage.Task.TaskDetailActivity;


public class ProfileActivity extends AppCompatActivity {

    private String company;
    private String email;
    private String id;
    private String fullname;
    private String level;
    private String password ="******";
    private static FirebaseFirestore mAuth;
    private TextView textView;
    private EditText editText;

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
                        company = document.get("company",String.class);
                        id = LoginActivity.UserID;
                        level = document.get("level",String.class);
                        fullname = document.get("lastname",String.class) + " " + document.get("firstname",String.class);
                        email = document.get("email",String.class);
                    }
                }
            }
        });
    }

    private void getCompanyNamefromID() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!company.equals("")) {
                    DocumentReference docRef = mAuth.collection("Companys").document(company);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                company = documentSnapshot.get("name", String.class);
                            }
                        }
                    });
                }
            }
        }, 2000);
    }

    private void showProfile() {
        getCompanyNamefromID();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView = (TextView) findViewById(R.id.text_Profile_ID);
                textView.setText(id);
                editText = (EditText) findViewById(R.id.text_Profile_Company);
                editText.setText(company); editText.setEnabled(false);
                textView = (TextView) findViewById(R.id.text_Profile_Fullname);
                textView.setText(fullname);
                textView = (TextView) findViewById(R.id.text_Profile_Level);
                textView.setText(level);
                textView = (TextView) findViewById(R.id.text_Profile_Email);
                textView.setText(email);
                textView = (TextView) findViewById(R.id.text_Profile_Password);
                textView.setText(password);
                if (company == "") {
                    editText.setEnabled(true);  editText.setHint("Enter your Company ID");
                    Button button = (Button) findViewById(R.id.Button_Profile_Join);
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            updateCompany();
                        }
                    });
                }
            }
        }, 3000);
    }

    private boolean checkCompany = false;
    private void updateCompany() {
        String ID = editText.getText().toString();
        mAuth.collection("Companys").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (ID.equals(document.getId())) {
                            checkCompany = true;
                        }
                    }
                }
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkCompany) {
                    mAuth.collection("Users").document(LoginActivity.UserID).update("company", ID);
                    startActivity(new Intent(ProfileActivity.this, DashBoardActivity.class));
                } else {
                    Toast.makeText(ProfileActivity.this,"Wrong Company ID",Toast.LENGTH_SHORT).show();
                }
            }
        },2000);
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