package com.team1.employeemanage.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.team1.employeemanage.AddTaskActivity;
import com.team1.employeemanage.DashBoardActivity;
import com.team1.employeemanage.Employee.EmployeeListAdapter;
import com.team1.employeemanage.LoginActivity;
import com.team1.employeemanage.R;
import com.team1.employeemanage.TaskActivity;

import java.util.List;

public class TaskDetailActivity extends AppCompatActivity {

    public static String TaskID, host, status, deadline, title, content, des, HostCheck;
    public static String[] MembersIDList;
    private static String[] EmployeeList;
    private static boolean[] Check;

    private static FirebaseFirestore db;
    private TextView textView;
    private ImageView imageView;
    private RelativeLayout relativeLayout;
    private ListView listView;
    private EmployeeListAdapter employeeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        TaskID = getIntent().getStringExtra("TaskID");

        initFireStore();
        getTaskDetail();

        showTaskDetail();

    }

//    @Override
//    protected void onStart() {
//        showTaskDetail();
//        super.onStart();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (DashBoardActivity.getLevel().equals("manager")) {
            getMenuInflater().inflate(R.menu.taskdetialmenu,menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (HostCheck.equals(LoginActivity.getUserID())) {
            if (item.getItemId() == R.id.deletetask) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(TaskDetailActivity.this);
                alertDialog.setTitle("Thông báo");
                alertDialog.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.setMessage("Bạn có muốn xóa nhiệm vụ này không?");
                alertDialog.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.collection("Tasks").document(DashBoardActivity.getCID())
                                .collection("AllTask").document(TaskID).delete();
                        startActivity(new Intent(TaskDetailActivity.this, TaskActivity.class));
                    }
                });
                alertDialog.show();
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
        DocumentReference docRef = db.collection("Tasks").document(DashBoardActivity.getCID())
                .collection("AllTask").document(TaskID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        host = document.get("host",String.class);
                        HostCheck = host;
                        getNamefromID(host);
                        title = document.get("title",String.class);
                        content = document.get("content",String.class);
                        status = document.get("status",String.class);
                        deadline = document.get("deadline",String.class);
                        des = document.get("des",String.class);
                    }
                }
            }
        });
        docRef.collection("members").document("ID").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    List<String> templist = (List<String>) documentSnapshot.get("id");
                    EmployeeList = new String[templist.size()];
                    MembersIDList = new String[templist.size()];
                    Check = new boolean[templist.size()];
                    for (int i = 0; i < templist.size(); i++) {
                        EmployeeList[i] = templist.get(i);
                        MembersIDList[i] = templist.get(i);
                        Check[i] = true;
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
                textView = (TextView) findViewById(R.id.TextView_TaskDetail_Title);
                textView.setText(title);
                textView = (TextView) findViewById(R.id.TextView_TaskDetail_Host);
                textView.setText(host);
                textView = (TextView) findViewById(R.id.TextView_TaskDetail_Content);
                textView.setText(content);
                textView = (TextView) findViewById(R.id.TextView_TaskDetail_Status);
                textView.setText(status);
                textView = (TextView) findViewById(R.id.TextView_TaskDetail_Deadline);
                textView.setText(deadline);
                getEmailList();
                onclickshowMembers();
            }
        }, 2000);
    }

    private void getNamefromID(String ID) {
        db.collection("Users").document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                host = documentSnapshot.get("lastname",String.class) + " " + documentSnapshot.get("firstname",String.class);
            }
        });
    }

    private void getEmailfromID(String ID, int index) {
            db.collection("Users").document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        EmployeeList[index] = documentSnapshot.get("email", String.class);
                        Log.d("index", "i : " + index + " " + EmployeeList[index]);
                    }
                }
            });
    }

    private void getEmailList() {
        for (int i = 0; i < EmployeeList.length; i++) {
            getEmailfromID(EmployeeList[i],i);
        }
    }

    private boolean switches = false;
    private void onclickshowMembers() {
        imageView = (ImageView) findViewById(R.id.TextView_TaskDetail_extend);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout = (RelativeLayout) findViewById(R.id.Relative_TaskDetail_EmployeeFrame);
                if (switches == false) {
                    relativeLayout.setVisibility(View.VISIBLE);
                    switches = true;
                } else {
                    relativeLayout.setVisibility(View.INVISIBLE);
                    switches = false;
                }
            }
        });
        setMemberList();
    }

    private void setMemberList() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("Meme","List: " + EmployeeList[0]);
                listView = (ListView) findViewById(R.id.ListView_TaskDetail_EmployeeList);
                employeeListAdapter = new EmployeeListAdapter(TaskDetailActivity.this,EmployeeList,Check);
                listView.setAdapter(employeeListAdapter);
            }
        },2000);
    }

}