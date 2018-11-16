package com.example.ksy.storage;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileEditorActivity extends AppCompatActivity {

    private Button saveBtn;
    private Button loadBtn;
    private Button clearBtn;
    private EditText edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_editor);

        saveBtn = (Button)findViewById(R.id.save_btn);
        loadBtn = (Button)findViewById(R.id.load_btn);
        clearBtn = (Button)findViewById(R.id.clear_btn2);
        edit = (EditText)findViewById(R.id.data_text);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = edit.getText().toString();
                FileOutputStream out;
                BufferedWriter writer = null;
                try{
                    out = openFileOutput("data", Context.MODE_PRIVATE);
                    writer = new BufferedWriter(new OutputStreamWriter(out));
                    writer.write(data);
                    Toast.makeText(FileEditorActivity.this, "Save Successfully", Toast.LENGTH_SHORT).show();
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    try{
                        if(writer != null){
                            writer.close();
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileInputStream in;
                BufferedReader reader = null;
                StringBuilder content = new StringBuilder();
                try{
                    in = openFileInput("data");
                    reader = new BufferedReader(new InputStreamReader(in));
                    String line = "";

                    while((line = reader.readLine()) != null){
                        content.append(line);
                    }
                    edit.setText(content.toString());
                    edit.setSelection(content.toString().length());
                    Toast.makeText(FileEditorActivity.this, "Load File Successfully", Toast.LENGTH_SHORT).show();
                }catch (IOException e){
                    Toast.makeText(FileEditorActivity.this, "Fail to load File", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }finally {
                    if(reader != null){
                        try{
                            reader.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setText("");
            }
        });
    }
}
