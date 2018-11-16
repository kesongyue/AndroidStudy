package com.example.ksy.storage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText passwordEdit;
    private EditText confirmPasswordEdit;
    private Button okBtn;
    private Button clearBtn;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passwordEdit = (EditText)findViewById(R.id.password);
        confirmPasswordEdit = (EditText)findViewById(R.id.confirm_password);
        okBtn = (Button)findViewById(R.id.ok_btn);
        clearBtn = (Button)findViewById(R.id.clear_btn);

        pref = getSharedPreferences("password",MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        final String password = pref.getString("password","");
        final boolean isRegister = !TextUtils.isEmpty(password);
        //如果已经设置过密码的情况
        if(isRegister){
            confirmPasswordEdit.setVisibility(View.INVISIBLE);
            passwordEdit.setHint("Password");
        }

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果没有设置过密码的情况，要检验password和confirmPassword是否相等
                if(!isRegister){
                    if(TextUtils.isEmpty(passwordEdit.getText().toString())){
                        Toast.makeText(MainActivity.this,"Password cannot be empty",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(passwordEdit.getText().toString().equals(confirmPasswordEdit.getText().toString())){
                        editor.putString("password",passwordEdit.getText().toString());
                        editor.apply();
                        Intent intent = new Intent(MainActivity.this,FileEditorActivity.class);
                        startActivity(intent);
                       // finish();
                    }else{
                        editor.clear();
                        Toast.makeText(MainActivity.this,"Password Mismatch",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(password.equals(passwordEdit.getText().toString())){
                        Intent intent = new Intent(MainActivity.this,FileEditorActivity.class);
                        startActivity(intent);
                       // finish();
                    }else{
                        Toast.makeText(MainActivity.this,"Invaild Password",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordEdit.setText("");
                confirmPasswordEdit.setText("");
            }
        });
    }
}
