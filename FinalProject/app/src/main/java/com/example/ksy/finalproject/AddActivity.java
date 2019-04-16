package com.example.ksy.finalproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView startTime,endTime;
    LinearLayout projectItem,processUnitItem,processSumItem,tagItem;
    ImageView cancelImage,okImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        startTime = (TextView)findViewById(R.id.add_page_start_time_content);
        endTime = (TextView)findViewById(R.id.add_page_end_time_content);
        processUnitItem = (LinearLayout)findViewById(R.id.add_page_process_item);
        projectItem = (LinearLayout)findViewById(R.id.add_page_project_item);
        processSumItem = (LinearLayout)findViewById(R.id.add_page_process_sum_item);
        cancelImage = (ImageView)findViewById(R.id.add_page_top_cancel);
        okImage = (ImageView)findViewById(R.id.add_page_top_positive);
        tagItem = (LinearLayout)findViewById(R.id.add_page_tag_item);

        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        processUnitItem.setOnClickListener(this);
        projectItem.setOnClickListener(this);
        processSumItem.setOnClickListener(this);
        cancelImage.setOnClickListener(this);
        okImage.setOnClickListener(this);
        tagItem.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v){
        String title,content;
        int textViewId;
        switch (v.getId()){
            case R.id.add_page_top_cancel:
                finish();
                break;
            case R.id.add_page_top_positive:
                //TODO: 点击确定按钮放回主界面，并传回主界面需要的信息，比如新增项目名称
                // Code
                break;
            case R.id.add_page_tag_item:
                //TODO： 跳转到“标签”页面
                //Code
                Intent intent = new Intent(AddActivity.this, TagActivity.class);
                startActivity(intent);
                break;

            case R.id.add_page_end_time_content:
            case R.id.add_page_start_time_content:
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String date = String.format("%d年%d月%d日",year,month+1,dayOfMonth);
                                if(v.getId() == R.id.add_page_start_time_content){
                                    startTime.setText(date);
                                }else{
                                    endTime.setText(date);
                                }
                            }
                        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;
            case R.id.add_page_process_item:
                title = ((TextView)processUnitItem.findViewById(R.id.add_page_process_name)).getText().toString();
                content = ((TextView)processUnitItem.findViewById(R.id.add_page_process_content)).getText().toString();
                textViewId = R.id.add_page_process_content;
                SetContent(title,content,textViewId);
                break;
            case R.id.add_page_project_item:
                title = ((TextView)projectItem.findViewById(R.id.add_page_project_name)).getText().toString();
                content = ((TextView)projectItem.findViewById(R.id.add_page_project_content)).getText().toString();
                textViewId = R.id.add_page_project_content;
                SetContent(title,content,textViewId);
                break;
            case R.id.add_page_process_sum_item:
                title = ((TextView)processSumItem.findViewById(R.id.add_page_process_sum_name)).getText().toString();
                content = ((TextView)processSumItem.findViewById(R.id.add_page_process_sum_content)).getText().toString();
                textViewId = R.id.add_page_process_sum_content;
                SetContent(title,content,textViewId);
                break;
            default:break;
        }
    }

    public void SetContent(String title,String content,int textViewId){
        LayoutInflater factory = LayoutInflater.from(this);
        View textEntryView = factory.inflate(R.layout.myedit,null);
        final EditText editText = (EditText)textEntryView.findViewById(R.id.myedit_edit);
        final TextView textView = (TextView)findViewById(textViewId);
        ((EditText)textEntryView.findViewById(R.id.myedit_edit)).setText(content);
        new AlertDialog.Builder(AddActivity.this)
                .setTitle(title)
                .setView(textEntryView)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textView.setText(editText.getText().toString());
                    }
                }).show();
    }
}
