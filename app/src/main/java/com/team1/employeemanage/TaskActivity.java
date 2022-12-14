package com.team1.employeemanage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team1.employeemanage.Task.TaskAdapter;
import com.team1.employeemanage.Task.TaskDetailActivity;

import java.util.Arrays;
import java.util.List;


public class TaskActivity extends AppCompatActivity {
    private static boolean check = false;
    private ProgressBar progressBar;
    private Integer i = -1;
    private String[] TaskTitle;
    private String[] TaskDes;
    private String[] TaskID;

    private static TaskAdapter taskAdapter;
    private static FirebaseFirestore db;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        progressBar = this.<ProgressBar>findViewById(R.id.progress_circular_task);
        initFireStore();
        getAllTask();
        showAllTask();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (DashBoardActivity.getLevel().equals("manager")) {
            getMenuInflater().inflate(R.menu.taskmenu,menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addtask) {
            Intent intent = new Intent(TaskActivity.this,AddTaskActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFireStore() {
        FirebaseApp.initializeApp(TaskActivity.this);
        db = FirebaseFirestore.getInstance();
    }

    private void getAllTask() {
        db.collection("Tasks").document(DashBoardActivity.getCID()).collection("AllTask").
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            TaskTitle = new String[task.getResult().size()];
                            TaskDes = new String[task.getResult().size()];
                            TaskID = new String[task.getResult().size()];
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("Tasks").document(DashBoardActivity.getCID()).collection("AllTask")
                                        .document(document.getId()).collection("members").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()){
                                                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                                        List<String> membergroup = (List<String>) documentSnapshot.get("id");
                                                        for (int j = 0; j < membergroup.size(); j++) {
                                                            if (membergroup.get(j).equals(LoginActivity.getUserID())){
                                                                check = true;
                                                            }
                                                        }
                                                        if (check) {
                                                            i++;
                                                            TaskID[i] = document.getId();
                                                            TaskTitle[i] = document.get("title", String.class);
                                                            TaskDes[i] = document.get("des", String.class);
                                                            check = false;
                                                        }
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    private void showAllTask() {
        progressBar.setVisibility(View.GONE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // ?????nh d???ng l???i m???ng
                TaskTitle = Arrays.copyOfRange(TaskTitle,0,i+1);
                TaskDes = Arrays.copyOfRange(TaskDes,0,i+1);

                taskAdapter = new TaskAdapter(TaskActivity.this,TaskTitle,TaskDes);
                listView = (ListView) findViewById(R.id.ListView_Task);
                listView.setAdapter(taskAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent(TaskActivity.this, TaskDetailActivity.class);
                        intent.putExtra("TaskID",TaskID[position]);
                        startActivity(intent);
                    }
                });
            }
        }, MainActivity.getDelaytime()*2);
    }

}