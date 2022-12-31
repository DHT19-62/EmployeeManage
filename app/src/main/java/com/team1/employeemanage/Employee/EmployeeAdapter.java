package com.team1.employeemanage.Employee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team1.employeemanage.R;

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
        holder.name.setText(employee.getFirstname() +" "+employee.getLastname());
        holder.email.setText(employee.getEmail());

    }

    @Override
    public int getItemCount() {
        return lst_employees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View itemview;

        public TextView name, email;
        public Button delete;

        public ViewHolder(View itemView) {
            super(itemView);
            itemview = itemView;

            addControls();
            addEvents();

        }

        private void addEvents() {
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

        private void addControls() {
            name = this.itemView.<TextView>findViewById(R.id.textView_employeeitem_name);
            email = this.itemview.<TextView>findViewById(R.id.textView_employeeitem_email);
            delete = this.itemView.<Button>findViewById(R.id.button_employeeitem_delete);
        }
    }
}