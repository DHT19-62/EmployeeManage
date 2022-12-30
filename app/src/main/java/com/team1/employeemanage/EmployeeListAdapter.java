package com.team1.employeemanage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


public class EmployeeListAdapter extends ArrayAdapter {
    private String[] EmployeeEmail;
    public static boolean[] Check;
    private Activity context;

    public EmployeeListAdapter(Activity context,String[] Email, boolean[] Check) {
        super(context,R.layout.employee_item,Email);
        this.EmployeeEmail = Email;
        this.Check = Check;
        this.context = context;
    }


    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView=inflater.inflate(R.layout.employee_item, null,true);

        TextView textView = (TextView) rowView.findViewById(R.id.EmployeeEmail);
        textView.setText(EmployeeEmail[position]);
        CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.CheckedEmployee);
        checkBox.setChecked(Check[position]);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    Check[position] = true;
                } else Check[position] = false;
            }
        });

        return rowView;
    }
}
