package com.team1.employeemanage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TaskDetailActivity extends AppCompatActivity {

    private String TaskID, host, status, deadline, title, content, HostCheck;

    private static FirebaseFirestore db;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        TaskID = getIntent().getStringExtra("TaskID");

        initFireStore();
        getTaskDetail();
        showTaskDetail();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (DashBoardActivity.Level.equals("manager")) {
            getMenuInflater().inflate(R.menu.taskdetialmenu,menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (HostCheck.equals(LoginActivity.UserID)) {
            if (item.getItemId() == R.id.deletetask) {
                db.collection("Tasks").document(DashBoardActivity.CompanyID)
                        .collection("AllTask").document(TaskID).delete();
                startActivity(new Intent(TaskDetailActivity.this, TaskActivity.class));
            }

            if (item.getItemId() == R.id.updatetask) {
                Intent intent = new Intent(TaskDetailActivity.this, AddTaskActivity.class);
                intent.putExtra("TaskID", TaskID);
                startActivity(intent);
            }
        } else {
            Toast.makeText(TaskDetailActivity.this,"You are not this task owner !!!",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFireStore() {
        FirebaseApp.initializeApp(TaskDetailActivity.this);
        db = FirebaseFirestore.getInstance();
    }

    private void getTaskDetail() {
        DocumentReference docRef = db.collection("Tasks").document(DashBoardActivity.CompanyID)
                .collection("AllTask").document(TaskID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        host = document.get("host",String.class);
                        HostCheck = host;
                        host = getNamefromID(host);
                        title = document.get("title",String.class);
                        content = document.get("content",String.class);
                        status = "Status: " + document.get("status",String.class);
                        deadline = "Deadline: " + document.get("deadline",String.class);
                    }
                }
            }
        });
    }

    private void showTaskDetail() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView = (TextView) findViewById(R.id.Title);
                textView.setText(title);
                textView = (TextView) findViewById(R.id.Host);
                textView.setText(host);
                textView = (TextView) findViewById(R.id.Content);
                textView.setText(content);
                textView = (TextView) findViewById(R.id.Status);
                textView.setText(status);
                textView = (TextView) findViewById(R.id.Deadline);
                textView.setText(deadline);
            }
        }, 2000);
    }

    private String getNamefromID(String ID) {
        db.collection("Users").document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                host = "Host: " + documentSnapshot.get("lastname",String.class) + " " + documentSnapshot.get("firstname",String.class);
            }
        });
        return host;
    }

}