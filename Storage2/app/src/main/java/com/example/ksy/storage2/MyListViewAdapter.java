package com.example.ksy.storage2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyListViewAdapter  extends ArrayAdapter<CommentInfo>{
    private int resourceId;
    public MyListViewAdapter(Context context, int textViewResourceId, List<CommentInfo>objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final CommentInfo item = getItem(position);
        final View view;
        final ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.portrait = (ImageView)view.findViewById(R.id.item_portrait);
            viewHolder.username = (TextView)view.findViewById(R.id.item_username);
            viewHolder.date = (TextView)view.findViewById(R.id.item_date);
            viewHolder.comment = (TextView)view.findViewById(R.id.item_comment);
            viewHolder.likeNumber = (TextView)view.findViewById(R.id.item_like_num);
            viewHolder.likePic = (ImageView) view.findViewById(R.id.item_like_pic);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.username.setText(item.GetUsername());
        viewHolder.date.setText(item.GetDate());
        viewHolder.comment.setText(item.GetComment());
        viewHolder.likeNumber.setText(String.valueOf(item.GetLikeNum()));

        MyDB dbHelper = new MyDB(view.getContext(),"User.db",null,1);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from User where username = ?",new String[]{item.GetUsername()},null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            byte[] in = cursor.getBlob(cursor.getColumnIndex("portrait"));
            Bitmap bitmap = BitmapFactory.decodeByteArray(in,0,in.length);
            viewHolder.portrait.setImageBitmap(bitmap);
        }
        cursor.close();

        cursor = db.rawQuery("select * from UC where commentId = ? and username = ?",new String[]{String.valueOf(item.GetId()),CommentActivity.username});
        if(cursor.getCount() > 0 ){
            viewHolder.likePic.setImageResource(R.drawable.red);
            viewHolder.likePic.setTag(R.drawable.red);
        }else{
            viewHolder.likePic.setImageResource(R.drawable.white);
            viewHolder.likePic.setTag(R.drawable.white);
        }

        viewHolder.likePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( R.drawable.white == (Integer)viewHolder.likePic.getTag() ){
                    viewHolder.likePic.setImageResource(R.drawable.red);
                    viewHolder.likePic.setTag(R.drawable.red);
                    item.SetLikeNum(item.GetLikeNum()+1);
                    viewHolder.likeNumber.setText(String.valueOf(item.GetLikeNum()));
                    ContentValues values = new ContentValues();
                    values.put("likeNum",item.GetLikeNum());
                    db.update("Comment",values,"id = ?",new String[]{String.valueOf(item.GetId())});

                    values.clear();
                    values.put("commentId",item.GetId());
                    values.put("username",CommentActivity.username);
                    db.insert("UC",null,values);
                }else{
                    viewHolder.likePic.setImageResource(R.drawable.white);
                    viewHolder.likePic.setTag(R.drawable.white);
                    item.SetLikeNum(item.GetLikeNum()-1);
                    viewHolder.likeNumber.setText(String.valueOf(item.GetLikeNum()));
                    ContentValues values = new ContentValues();
                    values.put("likeNum",item.GetLikeNum());
                    db.update("Comment",values,"id = ?",new String[]{String.valueOf(item.GetId())});
                    values.clear();

                    db.delete("UC","commentId = ? and username = ?",new String[]{String.valueOf(item.GetId()),CommentActivity.username});
                }
            }
        });
        return view;
    }

    class ViewHolder{
        ImageView portrait;
        TextView username,date,comment,likeNumber;
        ImageView likePic;
    }
}
