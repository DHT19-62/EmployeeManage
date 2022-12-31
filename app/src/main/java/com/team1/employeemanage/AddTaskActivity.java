package com.team1.employeemanage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team1.employeemanage.Employee.EmployeeListAdapter;
import com.team1.employeemanage.Task.TaskDetailActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTaskActivity extends AppCompatActivity {

    EditText title,content,deadline,status,des;
    Button AddTaskButton;
    ImageView Extend;
    ListView EmployeeListView;
    RelativeLayout EmployeeFrame;

    private int counter = -1;
    private String host;
    private static FirebaseFirestore db;
    private static Map<String,String> data;
    private static Map<String,List<String>> membersdata;
    private static String TaskID;

    private static String[] EmployeeEmail;
    private static boolean[] CheckedEmployee;
    private static String[] EmployeeIDs;

    EmployeeListAdapter employeeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        getSupportActionBar().hide();
        TaskID = getIntent().getStringExtra("TaskID");

        addcontrol();
        addevent();
        initFireStore();
        getEmployee();

        if (TaskID != null) {
            UpdateTaskDetail();
        }
        onclickAdd();
    }

    @Override
    protected void onStart() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TaskID != null) {
                    UpdateCheckedEmployee();
                }
                showEmployeeList();
            }
        },3000);
        super.onStart();
    }

    private void addcontrol() {
        title = (EditText) findViewById(R.id.EditText_AddTask_Title);
        content = (EditText) findViewById(R.id.EditText_AddTask_Content);
        deadline = (EditText) findViewById(R.id.EditText_AddTask_Deadline);
        status = (EditText) findViewById(R.id.EditText_AddTask_Status);
        des = (EditText) findViewById(R.id.EditText_AddTask_Des);
        AddTaskButton = (Button) findViewById(R.id.Button_AddTask_AddTask);
        EmployeeListView = (ListView) findViewById(R.id.ListView_AddTask_EmployeeList);
        Extend = (ImageView) findViewById(R.id.ImageView_AddTask_extend);
        EmployeeFrame = (RelativeLayout) findViewById(R.id.Relative_AddTask_EmployeeFrame);
    }

    private boolean switches = false;
    private void addevent() {
        Extend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switches == false) {
                    EmployeeFrame.setVisibility(View.VISIBLE);
                    switches = true;
                } else {
                    EmployeeFrame.setVisibility(View.INVISIBLE);
                    switches = false;
                }
            }
        });
    }

    private void initFireStore() {
        FirebaseApp.initializeApp(AddTaskActivity.this);
        db = FirebaseFirestore.getInstance();
    }

    private void setData() {
        data = new HashMap<>();
        data.put("title",title.getText().toString());
        data.put("content",content.getText().toString());
        data.put("des",des.getText().toString());
        data.put("status",status.getText().toString());
        data.put("deadline",deadline.getText().toString());
        if (TaskID == null) {
            host = LoginActivity.UserID;
        }
        data.put("host",host);
        List<String> MembersList = new ArrayList<>();

        MembersList.add(LoginActivity.UserID);
        CheckedEmployee = EmployeeListAdapter.Check;
        for (int i = 0; i < CheckedEmployee.length; i++) {
            if (CheckedEmployee[i]) {
                MembersList.add(EmployeeIDs[i]);
            }
        }
        membersdata = new HashMap<>();
        membersdata.put("id",MembersList);
    }

    private void UpdateTaskDetail() {
        title.setText(TaskDetailActivity.title);
        content.setText(TaskDetailActivity.content);
        des.setText(TaskDetailActivity.des);
        status.setText(TaskDetailActivity.status);
        deadline.setText(TaskDetailActivity.deadline);
        host = TaskDetailActivity.HostCheck;
        AddTaskButton.setText("Update");
    }

    private void UpdateCheckedEmployee() {
        for (int i = 0; i < EmployeeIDs.length; i++) {
            for (int j = 0; j < TaskDetailActivity.MembersIDList.length; j++) {
                if (TaskDetailActivity.MembersIDList[j].equals(EmployeeIDs[i])) {
                    CheckedEmployee[i] = true;
                }
            }
        }
    }


    private void onclickAdd() {
        AddTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData();
                DocumentReference document;
                if (TaskID == null) {
                    document = db.collection("Tasks").document(DashBoardActivity.CompanyID)
                            .collection("AllTask").document();
                } else {
                    document = db.collection("Tasks").document(DashBoardActivity.CompanyID)
                            .collection("AllTask").document(TaskID);
                }
                document.set(data);
                document.collection("members").document("ID").set(membersdata);

                startActivity(new Intent(AddTaskActivity.this,TaskActivity.class));
            }
        });
    }

    private void getEmployee() {
        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                EmployeeEmail = new String[task.getResult().size()];
                CheckedEmployee = new boolean[task.getResult().size()];
                EmployeeIDs = new String[task.getResult().size()];
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if ((document.get("company",String.class).equals(DashBoardActivity.CompanyID)) && !(document.getId().equals(LoginActivity.UserID))) {
                        counter++;
                        EmployeeIDs[counter] = document.getId();
                        EmployeeEmail[counter] = document.get("email",String.class);
                        CheckedEmployee[counter] = false;
                    }
                }
            }
        });
    }

    private void showEmployeeList() {
        EmployeeEmail = Arrays.copyOfRange(EmployeeEmail,0,counter+1);
        CheckedEmployee = Arrays.copyOfRange(CheckedEmployee,0,counter+1);
        EmployeeIDs = Arrays.copyOfRange(EmployeeIDs,0,counter+1);

        employeeAdapter = new EmployeeListAdapter(this,EmployeeEmail,CheckedEmployee);
        EmployeeListView.setAdapter(employeeAdapter);
    }

}