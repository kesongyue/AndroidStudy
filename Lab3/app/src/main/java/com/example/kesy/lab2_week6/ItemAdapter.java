package com.example.kesy.lab2_week6;
import  java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public abstract  class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{
    private List<Item> mItem;
    static class ViewHolder extends RecyclerView.ViewHolder{
        Button btn;
        TextView itemText;

        public ViewHolder(View view){
            super(view);
            btn = (Button)view.findViewById(R.id.item_btn);
            itemText = (TextView)view.findViewById(R.id.item_text);
        }
    }
    public abstract void convert(Item item);
    public ItemAdapter(List<Item> itemList){
        mItem = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                Item item = mItem.get(position);
                convert(item);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){
                int position = holder.getAdapterPosition();
                Toast.makeText(view.getContext(),"删除"+mItem.get(position).getTextViewContent(),Toast.LENGTH_SHORT).show();
                mItem.remove(position);
                //notifyDataSetChanged();
                notifyItemRemoved(position);
                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Item item = mItem.get(position);
        holder.btn.setText(item.getBtnText());
        holder.itemText.setText(item.getTextViewContent());
    }

    @Override
    public int getItemCount(){
        return mItem.size();
    }
}
