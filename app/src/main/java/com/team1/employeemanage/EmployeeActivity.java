package com.team1.employeemanage;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EmployeeActivity extends AppCompatActivity {

    ArrayList<Employee> lst_employees;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    RecyclerView recyclerView;
    EmployeeAdapter employeeAdapter;
    TextView txtView_employee;
    String id_company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        getSupportActionBar().hide();

        addControls();

        addEvents();
    }

    private void addEvents() {

    }


    private void addControls() {

        txtView_employee = this.<TextView>findViewById(R.id.textView_employee_Employee);

        recyclerView = this.<RecyclerView>findViewById(R.id.recylerview_employee);
        lst_employees = new ArrayList<Employee>();
        LoadIdcompany();
        employeeAdapter = new EmployeeAdapter(lst_employees, this);
        recyclerView.setAdapter(employeeAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);



    }

    private void Loademployee() {
        FirebaseFirestore.getInstance()
                .collection("Users")
                .whereEqualTo("company", id_company)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            lst_employees.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String firstname_ = (String) document.get("firstname");
                                String lastname_ = (String) document.get("lastname");
                                String id_ = (String) document.get("id");
                                addEmployee(new Employee(firstname_, lastname_, id_));
                            }
                            employeeAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void LoadIdcompany() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "id user: " + user.getUid().toString());
        db = FirebaseFirestore.getInstance();
        db.collection("Companys")
                .whereEqualTo("host",  user.getUid().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                setId_company(document.getId().toString());
                                Log.d(TAG, "id company: " + id_company);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void setId_company(String id_company) {
        this.id_company = id_company;
    }

    public void addEmployee(Employee employee){
        lst_employees.add(employee);
    }
}