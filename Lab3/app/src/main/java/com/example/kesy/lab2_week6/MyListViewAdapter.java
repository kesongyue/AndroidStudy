package com.example.kesy.lab2_week6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MyListViewAdapter extends ArrayAdapter<Item> {
    private int resourceId;
    public MyListViewAdapter(Context context, int textViewResourceId, List<Item>objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Item item = getItem(position);// 获取当前的条目实例
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.itemBtn = (Button)view.findViewById(R.id.item_btn);
            viewHolder.itemTextView = (TextView)view.findViewById(R.id.item_text);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.itemBtn.setText(item.getBtnText());
        viewHolder.itemTextView.setText(item.getTextViewContent());
        return view;
    }
    class ViewHolder{
        Button itemBtn;
        TextView itemTextView;
    }
}
