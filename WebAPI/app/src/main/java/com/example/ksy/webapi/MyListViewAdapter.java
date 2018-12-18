package com.example.ksy.webapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
class ListItem{
    String projectName,projectDescription,username;
    int projectId,open_issues_count;
    public ListItem(String projcetName,int projcetId,int projectIssue,String projectDescription,String username){
        this.projectName = projcetName;
        this.projectId = projcetId;
        this.open_issues_count = projectIssue;
        this.projectDescription = projectDescription;
        this.username = username;
    }

    public String GetProjectName() { return projectName; }
    public int GetProjectId() { return projectId; }
    public int GetProjectIssueNumber() {return open_issues_count; }
    public String GetProjectDescription() {return projectDescription; }
    public String GetUsername(){return username;}
}
public class MyListViewAdapter extends ArrayAdapter<ListItem>{
    private int resourceId;
    public MyListViewAdapter(Context context, int textViewResourceId, List<ListItem>objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ListItem item = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.projectName = (TextView)view.findViewById(R.id.github_project_name);
            viewHolder.projectId = (TextView)view.findViewById(R.id.github_project_id);
            viewHolder.projectIssue = (TextView)view.findViewById(R.id.github_project_issue);
            viewHolder.projectDescription = (TextView)view.findViewById(R.id.github_project_description);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.projectName.setText(item.GetProjectName());
        viewHolder.projectId.setText(String.valueOf(item.GetProjectId()));
        viewHolder.projectIssue.setText(String.valueOf(item.GetProjectIssueNumber()));
        viewHolder.projectDescription.setText(item.GetProjectDescription());
        return view;
    }

    class ViewHolder{
        TextView projectName,projectId,projectIssue,projectDescription;
    }
}
