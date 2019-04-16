package com.example.ksy.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TagActivity extends AppCompatActivity {
    private List<String> tagList = new ArrayList<>();
    private ArrayAdapter arrayAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        tagList.add("标签1");
        tagList.add("标签2");
        tagList.add("标签3");
        tagList.add("标签4");
        ListView lv = findViewById(R.id.tagList);
        arrayAdapter = new ArrayAdapter(this, R.layout.tag_item, tagList);
        lv.setAdapter(arrayAdapter);
    }
}
