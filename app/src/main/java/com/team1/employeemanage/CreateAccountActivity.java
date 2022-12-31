package com.team1.employeemanage;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText firstname, lastname,
                email, password, repassword;

    private CheckBox checkBox_employee, checkBox_manager;

    private Button signin,cancel;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getSupportActionBar().hide();

        addControls();
        addEvents();
    }

    private void addEvents() {
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createaccount();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccountActivity.this,LoginActivity.class));
            }
        });

        checkBox_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox_manager.setChecked(false);
            }
        });

        checkBox_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox_employee.setChecked(false);
            }
        });
    }

    private void createaccount() {
        String email_ = email.getText().toString();
        String password_ = password.getText().toString();
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email_, password_)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        storeaccountindatabase(user);
        // Tro ve activity Log in
        Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void storeaccountindatabase(FirebaseUser user) {
        // Add a new document with a generated id.
        Map<String, Object> user_ = new HashMap<>();
        user_.put("firstname", firstname.getText().toString());
        user_.put("lastname", lastname.getText().toString());
        user_.put("email", email.getText().toString());
        user_.put("password", password.getText().toString());
        user_.put("company", "");
        if (checkBox_employee.isChecked()){
            user_.put("level", "employee");
        }else {
            if (checkBox_manager.isChecked()){
                user_.put("level", "manager");
            }else{
                user_.put("level", "employee");
            }
        }


        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .document(user.getUid().toString())
                .set(user_, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Luu tai khoan thanh cong");
                        }else {
                            Log.d(TAG, "Luu tai khoan that bai");
                        }
                    }
                });
    }

    private void addControls() {
        firstname = this.<EditText>findViewById(R.id.editText_createaccount_firstname);
        lastname = this.<EditText>findViewById(R.id.editText_createaccount_lastname);
        email = this.<EditText>findViewById(R.id.editText_createaccount_email);
        password = this.<EditText>findViewById(R.id.editText_createaccount_repassword);
        checkBox_employee = this.<CheckBox>findViewById(R.id.checkbox_createaccount_employee);
        checkBox_employee.setChecked(true);
        checkBox_manager = this.<CheckBox>findViewById(R.id.checkbox_createaccount_manager);
        signin = this.<Button>findViewById(R.id.button_createaccount_signin);
        cancel = this.<Button>findViewById(R.id.button_createaccount_cancel);
    }

    @Override
    public void onBackPressed() {
    }
}