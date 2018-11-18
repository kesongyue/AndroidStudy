package com.example.ksy.storage2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDB extends SQLiteOpenHelper{
    public static final String CREATE_USER="create table User ("
        + "username text primary key,"
        + "password text,"
        + "portrait blob)";

    public static  final String CREATE_COMMENT = "create table Comment ("
            + "id integer primary key autoincrement,"
            + "username text,"
            + "date text,"
            + "comment text,"
            + "likeNum integer)";

    public static final String CREATE_UC = "create table UC ("
            + "id integer primary key autoincrement,"
            + "commentId integer,"
            + "username text)";

    private Context mContext;
    public MyDB(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_COMMENT);
        db.execSQL(CREATE_UC);
        //Toast.makeText(mContext,"Create Succeeded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
    }
}
