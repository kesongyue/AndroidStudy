package com.example.ksy.storage2;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private EditText commentEdit ;
    private List<CommentInfo> itemList = new ArrayList<>();
    private ListView listView;
    private MyListViewAdapter myListViewAdapter;
    private Button sendBtn;
    private MyDB dbHelper;
    public static String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        //获得传过来的username
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        commentEdit = (EditText)findViewById(R.id.comment_content);
        sendBtn = (Button)findViewById(R.id.send_btn);
        listView = (ListView)findViewById(R.id.comment_list) ;
        dbHelper = new MyDB(this,"User.db",null,1);
        InitList();

        myListViewAdapter = new MyListViewAdapter(CommentActivity.this,R.layout.item,itemList);
        listView.setAdapter(myListViewAdapter);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(commentEdit.getText().toString())){
                    Toast.makeText(CommentActivity.this,"Comment cannot be empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                String dateString = simpleDateFormat.format(date);

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("username",username);
                values.put("date",dateString);
                values.put("comment",commentEdit.getText().toString());
                values.put("likeNum",0);
                db.insert("Comment",null,values);
                Cursor cursor = db.rawQuery("select * from Comment",null,null);
                if(cursor.moveToLast()){
                    CommentInfo item = new CommentInfo(cursor.getInt(cursor.getColumnIndex("id")),username,dateString,commentEdit.getText().toString(),0);
                    itemList.add(item);
                    myListViewAdapter.notifyDataSetChanged();
                }
                commentEdit.setText("");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(ContextCompat.checkSelfPermission(CommentActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(CommentActivity.this, new String[]{Manifest.permission.READ_CONTACTS},1);
                }else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(CommentActivity.this);
                    dialog.setTitle("Info").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" = ?",new String[]{itemList.get(position).GetUsername()},null);
                    if(cursor.moveToFirst()){
                        //String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        dialog.setMessage("Username: "+itemList.get(position).GetUsername() + "\nPhone: " + phoneNumber);
                    }else{
                        dialog.setMessage("Username: "+itemList.get(position).GetUsername() + "\nPhone number not exist.");
                    }
                    dialog.show();
                    cursor.close();
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int removePos = position;
                AlertDialog.Builder dialog = new AlertDialog.Builder(CommentActivity.this);
                if(username.equals(itemList.get(position).GetUsername())){
                    dialog.setTitle("Delete or not?");
                    dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            Log.d("SELECTITEM",String.valueOf(itemList.get(removePos).GetId()));
                            db.delete("Comment","id = ?" ,new String[]{String.valueOf(itemList.get(removePos).GetId())});
                            itemList.remove(removePos);
                            myListViewAdapter.notifyDataSetChanged();
                        }
                    });
                }else{
                    dialog.setTitle("Report or not?");
                    dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(CommentActivity.this,"Already reported.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    public void InitList(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Comment",null,null);
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String comment = cursor.getString(cursor.getColumnIndex("comment"));
                int likeNum = cursor.getInt(cursor.getColumnIndex("likeNum"));
                CommentInfo commentInfo = new CommentInfo(id,username,date,comment,likeNum);
                itemList.add(commentInfo);
            }while(cursor.moveToNext());
        }
        cursor.close();
    }
}
