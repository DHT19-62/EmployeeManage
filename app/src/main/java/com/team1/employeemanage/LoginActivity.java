package com.team1.employeemanage;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText username, password;
    private Button btn_SignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();

        addConttrols();

        addEvent();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            UpdateUI(currentUser);
        }
    }

    private void addEvent() {
        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SignInWithEmailAndPassword();
            }
        });
    }

    private void SignInWithEmailAndPassword() {
        String username_ = username.getText().toString();
        String password_ = password.getText().toString();

        mAuth.signInWithEmailAndPassword(username_, password_)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            UpdateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Username or Password are incorrect",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void UpdateUI(FirebaseUser user) {
        Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
        startActivity(intent);
        finish();
    }

    private void addConttrols() {
        btn_SignIn = this.<Button>findViewById(R.id.button_SignIn_Login);
        username = this.<EditText>findViewById(R.id.editText_UserName_Login);
        password = this.<EditText>findViewById(R.id.editText_Password_Login);
    }
}