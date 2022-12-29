package com.team1.employeemanage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

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
        TaskID = getIntent().getStringExtra("TaskID");
        addcontrol();
        addevent();
        initFireStore();
        getEmployee();
        if (TaskID != null) {
            Update();
        }
        onclickAdd();
    }

    @Override
    protected void onStart() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showEmployeeList();
            }
        },3000);
        super.onStart();
    }

    private void addcontrol() {
        title = (EditText) findViewById(R.id.TitleEditText);
        content = (EditText) findViewById(R.id.ContentEditText);
        deadline = (EditText) findViewById(R.id.DeadlineEditText);
        status = (EditText) findViewById(R.id.StatusEditText);
        des = (EditText) findViewById(R.id.DesEditText);
        AddTaskButton = (Button) findViewById(R.id.AddTaskButton);
        EmployeeListView = (ListView) findViewById(R.id.EmployeeList);
        Extend = (ImageView) findViewById(R.id.extend);
        EmployeeFrame = (RelativeLayout) findViewById(R.id.EmployeeFrame);
    }

    private void addevent() {
        Extend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmployeeFrame.setVisibility(View.VISIBLE);
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

    private void Update() {
        DocumentReference document = db.collection("Tasks").document(DashBoardActivity.CompanyID)
                .collection("AllTask").document(TaskID);
        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                title.setText(documentSnapshot.get("title",String.class));
                content.setText(documentSnapshot.get("content",String.class));
                des.setText(documentSnapshot.get("des",String.class));
                status.setText(documentSnapshot.get("status",String.class));
                deadline.setText(documentSnapshot.get("deadline",String.class));
                host = documentSnapshot.get("host",String.class);
            }
        });

        document.collection("members").document("ID").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                List<String> memberlist = (List<String>) documentSnapshot.get("id");
                for (int i = 0; i < EmployeeIDs.length; i++) {
                    for (int j = 0; j < memberlist.size(); j++) {
                        if (memberlist.get(j).equals(EmployeeIDs[i])) {
                            CheckedEmployee[i] = true;
                        }
                    }
                }
            }
        });
        AddTaskButton.setText("Update");
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