package com.team1.employeemanage;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team1.employeemanage.Employee.Employee;
import com.team1.employeemanage.Employee.EmployeeAdapter;

import java.util.ArrayList;

public class EmployeeActivity extends AppCompatActivity {

    ArrayList<Employee> lst_employees;
    FirebaseFirestore db;

    RecyclerView recyclerView;
    Button button_refresh;
    EmployeeAdapter employeeAdapter;
    TextView txtView_employee;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        getSupportActionBar().hide();

        addControls();

        addEvents();

    }

    private void Looademployee(String id_company) {
        FirebaseFirestore.getInstance()
                .collection("Users")
                .whereEqualTo("company", id_company)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId());
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String firstname_ = document.get("firstname").toString();
                                String lastname_ = document.get("lastname").toString();
                                String email_ = document.get("email").toString();
                                String id_ = document.getId();
                                addEmployee(new Employee(firstname_, lastname_, email_, id_));
                            }
                        } else {

                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void addEvents() {
        button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Looademployee(DashBoardActivity.getCID());
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        employeeAdapter.notifyDataSetChanged();
                    }
                }, MainActivity.getDelaytime());
            }
        });
    }





    private void addControls() {

        button_refresh = this.<Button>findViewById(R.id.button_employee_Refresh);
        progressBar = this.<ProgressBar>findViewById(R.id.progress_circular_employee);
        txtView_employee = this.<TextView>findViewById(R.id.textView_employee_Employee);

        recyclerView = this.<RecyclerView>findViewById(R.id.recylerview_employee);
        lst_employees = new ArrayList<Employee>();
        Looademployee(DashBoardActivity.getCID());
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                employeeAdapter = new EmployeeAdapter(lst_employees, EmployeeActivity.this);
                recyclerView.setAdapter(employeeAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EmployeeActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
            }
        }, MainActivity.getDelaytime());

    }

    public void addEmployee(Employee employee) {
        lst_employees.add(employee);
    }
}