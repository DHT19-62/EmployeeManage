package com.team1.employeemanage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaskAdapter extends ArrayAdapter {
    private Activity context;
    private String[] TaskTitle;
    private String[] TaskContent;

    public TaskAdapter(Activity context, String[] TaskTitle, String[] TaskContent){
        super(context,R.layout.task_item,TaskTitle);
        this.context = context;
        this.TaskTitle = TaskTitle;
        this.TaskContent = TaskContent;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.task_item, null,true);

        TextView textView = (TextView) rowView.findViewById(R.id.TaskTitle);
        textView.setText(TaskTitle[position]);

        textView = (TextView) rowView.findViewById(R.id.TaskContent);
        textView.setText(TaskContent[position]);
        return rowView;
    }
}
