package com.example.ksy.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO ：已进入主页面就跳转到详情页面，测试用的：
        Intent intent = new Intent(MainActivity.this,AddActivity.class);
        startActivity(intent);


    }
}
