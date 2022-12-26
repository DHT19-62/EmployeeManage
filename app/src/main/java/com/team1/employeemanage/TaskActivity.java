package com.team1.employeemanage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class TaskActivity extends AppCompatActivity {
    private static boolean check = false;
    private Integer i = -1;
    private String[] TitleTask;
    private String[] DescTask;
    private static TaskAdapter taskAdapter;
    private static String CompanyID = "zrsvUCGsHpTNWmOgvZes";
    private static String UserID = "9Xwvo7cqlJdNqs7HxPfPvgIxmQM2";

    private static FirebaseFirestore db;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        getSupportActionBar().hide();

        initFireStore();
        getAllTask();
        showAllTask();
    }

    private void initFireStore() {
        FirebaseApp.initializeApp(TaskActivity.this);
        db = FirebaseFirestore.getInstance();
    }

    private void getAllTask() {
        db.collection("Tasks").document(CompanyID).collection("AllTask").
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            TitleTask = new String[task.getResult().size()];
                            DescTask = new String[task.getResult().size()];
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("DEBUG",document.getId());
                                db.collection("Tasks").document(CompanyID).collection("AllTask")
                                        .document(document.getId()).collection("members").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()){
                                                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                                        List<String> membergroup = (List<String>) documentSnapshot.get("id");
                                                        for (int j = 0; j < membergroup.size(); j++) {
                                                            if (membergroup.get(j).equals(UserID)){
                                                                check = true;
                                                            }
                                                        }
                                                        if (check) {
                                                            i++;
                                                            TitleTask[i] = document.get("title", String.class);
                                                            DescTask[i] = document.get("content", String.class);
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
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                taskAdapter = new TaskAdapter(TaskActivity.this,TitleTask,DescTask);
                listView = (ListView) findViewById(R.id.TaskList);
                listView.setAdapter(taskAdapter);
            }
        }, 3000);
    }
}