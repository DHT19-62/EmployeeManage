package com.team1.employeemanage;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EmployeeActivity extends AppCompatActivity {

    ArrayList<Employee> lst_employees;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    RecyclerView recyclerView;
    Button button_refresh;
    EmployeeAdapter employeeAdapter;
    TextView txtView_employee;
    String id_company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        getSupportActionBar().hide();

        addControls();

        LoadIdcompany();
        addEvents();
        Looademployee(id_company);
    }

    private void Looademployee(String id_company) {
        FirebaseFirestore.getInstance()
                .collection("Users")
                .whereEqualTo("company", id_company)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId());
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String firstname_ = document.get("firstname").toString();
                                String lastname_ = document.get("lastname" ).toString();
                                String id_ = document.get("id").toString();
                                addEmployee(new Employee(firstname_, lastname_, id_));
                            }
                            employeeAdapter.notifyDataSetChanged();
                        }else {

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
                Loademployee();
            }
        });
    }

    private void Loademployee() {
        lst_employees.clear();
        FirebaseFirestore.getInstance()
                .collection("Users")
                .whereEqualTo("company", id_company)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String firstname_ = document.get("firstname").toString();
                                String lastname_ = document.get("lastname").toString();
                                String email_ = document.get("email").toString();
                                String id_ = document.getId().toString();
                                addEmployee(new Employee(firstname_, lastname_, email_, id_));
                            }
                            employeeAdapter.notifyDataSetChanged();
                        }else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    private void addControls() {

        button_refresh = this.<Button>findViewById(R.id.button_employee_Refresh);
        txtView_employee = this.<TextView>findViewById(R.id.textView_employee_Employee);

        recyclerView = this.<RecyclerView>findViewById(R.id.recylerview_employee);
        lst_employees = new ArrayList<Employee>();
        employeeAdapter = new EmployeeAdapter(lst_employees, this);
        recyclerView.setAdapter(employeeAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

    }





    private void LoadIdcompany() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "id user: " + user.getUid().toString());
        db = FirebaseFirestore.getInstance();
        db.collection("Users").document(user.getUid().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (task.isSuccessful()){
                            setId_company((String) documentSnapshot.get("company"));
                        }
                    }
                });
    }

    public void setId_company(String id_company) {
        this.id_company = id_company;
        Log.d(TAG,"xxx: "+id_company);
    }

    public void addEmployee(Employee employee){
        lst_employees.add(employee);
    }
}