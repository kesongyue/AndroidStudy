package com.example.ksy.webapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AdapterOfIssueInfo extends ArrayAdapter<IssueInfo> {
    private int resourceId;
    public AdapterOfIssueInfo(Context context, int textViewResourceId, List<IssueInfo> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        IssueInfo item = getItem(position);
        View view;
        AdapterOfIssueInfo.ViewHolderOfIssueInfo viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new AdapterOfIssueInfo.ViewHolderOfIssueInfo();
            viewHolder.title = (TextView)view.findViewById(R.id.issueinfo_title);
            viewHolder.createTime = (TextView)view.findViewById(R.id.issueinfo_create_at);
            viewHolder.state = (TextView)view.findViewById(R.id.issueinfo_state);
            viewHolder.description = (TextView)view.findViewById(R.id.issueinfo_body);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (AdapterOfIssueInfo.ViewHolderOfIssueInfo)view.getTag();
        }
        viewHolder.title.setText(item.GetTitle());
        viewHolder.createTime.setText(item.GetCreated_at());
        viewHolder.state.setText(item.GetState());
        viewHolder.description.setText(item.GetBody());
        return view;
    }

    class ViewHolderOfIssueInfo{
        TextView title,createTime,state,description;
    }
}
