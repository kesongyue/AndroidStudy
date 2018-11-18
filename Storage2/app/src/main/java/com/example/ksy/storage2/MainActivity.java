package com.example.ksy.storage2;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
    private EditText passwordEdit,usernameEdit,confirmPasswordEdit;
    private Button okBtn;
    private Button clearBtn;
    private RadioGroup radioGroup;
    private ImageView imageView;
    private RadioButton loginRadioBtn;
    private RadioButton registerRadioBtn;
    private MyDB dbHelper;
    private boolean isInLoginPage;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEdit = (EditText)findViewById(R.id.username);
        passwordEdit = (EditText)findViewById(R.id.password);
        confirmPasswordEdit = (EditText)findViewById(R.id.confirm_password);
        okBtn = (Button)findViewById(R.id.ok_btn);
        clearBtn = (Button)findViewById(R.id.clear_btn);
        radioGroup = (RadioGroup)findViewById(R.id.radio_group);
        imageView = (ImageView)findViewById(R.id.add_picture);
        loginRadioBtn = (RadioButton)findViewById(R.id.login_rad_btn);
        registerRadioBtn = (RadioButton)findViewById(R.id.register_rad_btn);
        dbHelper = new MyDB(this,"User.db",null,1);

        isInLoginPage = true;
        imageView.setVisibility(View.GONE);
        confirmPasswordEdit.setVisibility(View.GONE);

        loginRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,loginRadioBtn.getText().toString(),Toast.LENGTH_SHORT).show();
                imageView.setVisibility(View.GONE);
                confirmPasswordEdit.setVisibility(View.GONE);
                isInLoginPage = true;
                passwordEdit.setText("");
                passwordEdit.setHint("Password");
            }
        });

        registerRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setVisibility(View.VISIBLE);
                confirmPasswordEdit.setVisibility(View.VISIBLE);
                isInLoginPage = false;
                passwordEdit.setText("");
                confirmPasswordEdit.setText("");
                passwordEdit.setHint("New Password");
                //Log.d("SELECEIMAGE",imageView.getTag().toString());
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(usernameEdit.getText().toString())){
                    Toast.makeText(MainActivity.this,usernameEdit.getHint().toString()+" cannot be empty",Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(passwordEdit.getText().toString())){
                    Toast.makeText(MainActivity.this,"Password cannot be empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isInLoginPage){
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor = db.rawQuery("select * from User where username = ? and password = ?",new String[]{usernameEdit.getText().toString(),passwordEdit.getText().toString()});
                    if(cursor.getCount() <= 0){
                        Toast.makeText(MainActivity.this,"uername or password invalid",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    passwordEdit.setText("");
                    Toast.makeText(MainActivity.this,"Correct Password",Toast.LENGTH_SHORT).show();
                    //跳转到评论页面
                    Intent intent = new Intent(MainActivity.this,CommentActivity.class);
                    intent.putExtra("username",usernameEdit.getText().toString());
                    startActivity(intent);
                    cursor.close();

                }else{
                    if(!passwordEdit.getText().toString().equals(confirmPasswordEdit.getText().toString())){
                        Toast.makeText(MainActivity.this,"Password Mismatch",Toast.LENGTH_SHORT).show();
                    }else{
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        Cursor cursor = db.rawQuery("select * from User where username = ?",new String[]{usernameEdit.getText().toString()});
                        if(cursor.getCount() > 0){
                            Toast.makeText(MainActivity.this,usernameEdit.getText().toString()+" already existed",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        cursor.close();

                        Bitmap bitmap = null;
                        if(imageView.getTag().toString().equals("selecting")){
                            Resources res = getResources();
                            bitmap = BitmapFactory.decodeResource(res,R.drawable.me);
                        }else{
                            bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                        }
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,os);

                        ContentValues values = new ContentValues();
                        values.put("username",usernameEdit.getText().toString());
                        values.put("password",passwordEdit.getText().toString());
                        values.put("portrait",os.toByteArray());
                        db.insert("User",null,values);
                        Toast.makeText(MainActivity.this,"Register success..",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //申请权限
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},RESULT_LOAD_IMAGE);
                }
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,RESULT_LOAD_IMAGE);
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameEdit.setText("");
                passwordEdit.setText("");
                confirmPasswordEdit.setText("");
                imageView.setImageResource(R.drawable.add);
                imageView.setTag("selecting");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn,null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            BitmapFactory.Options options = new BitmapFactory.Options();
            // options 设为true时，构造出的bitmap没有图片，只有一些长宽等配置信息，但比较快，设为false时，才有图片
            options.inJustDecodeBounds = true;
            int scale = (int)(options.outWidth / (float)100);
            if(scale <= 0){
                scale = 1;
            }
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath,options);
            imageView.setImageBitmap(bitmap);
            if(bitmap != null){
                imageView.setTag("selected");
            }
            Log.d("SELECEIMAGE",imageView.getTag().toString());
        }

    }
}
