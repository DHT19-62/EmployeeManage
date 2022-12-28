package com.team1.employeemanage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    private ArrayList<Employee> lst_employees;
    private Context mContext;

    public EmployeeAdapter(ArrayList<Employee> lst_employees, Context mContext) {
        this.lst_employees = lst_employees;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Nạp layout cho View biểu diễn phần tử sinh viên
        View employeeView = inflater.inflate(R.layout.employee_item_layout, parent, false);

        return new ViewHolder(employeeView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Employee employee = lst_employees.get(position);
<<<<<<< HEAD
        holder.name.setText(employee.getFirstname() +" "+employee.getLastname());
        holder.email.setText(employee.getEmail());
=======
        holder.lastname.setText(employee.getLastname());
        holder.firstname.setText(employee.getFirstname());
>>>>>>> 3b9cb46 (update create account)

    }

    @Override
    public int getItemCount() {
        return lst_employees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View itemview;
<<<<<<< HEAD

        public TextView name, email;
=======
        public TextView firstname;
        public TextView lastname;
>>>>>>> 3b9cb46 (update create account)
        public Button delete;

        public ViewHolder(View itemView) {
            super(itemView);
            itemview = itemView;
<<<<<<< HEAD

            addControls();
            addEvents();

        }

        private void addEvents() {
=======
            firstname = itemView.findViewById(R.id.textView_createaccount_firstname);
            lastname = itemView.findViewById(R.id.textView_createaccount_lastname);
            delete = itemView.findViewById(R.id.button_employeeitem_delete);

>>>>>>> 3b9cb46 (update create account)
            //Xử lý khi nút Chi tiết được bấm
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(),
                                    "Hello", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
<<<<<<< HEAD

        private void addControls() {
            name = this.itemView.<TextView>findViewById(R.id.textView_employeeitem_name);
            email = this.itemview.<TextView>findViewById(R.id.textView_employeeitem_email);
            delete = this.itemView.<Button>findViewById(R.id.button_employeeitem_delete);
        }
=======
>>>>>>> 3b9cb46 (update create account)
    }
}
