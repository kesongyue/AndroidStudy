package com.example.ksy.webapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IssueActivity extends AppCompatActivity {
    private Button addIssue;
    private EditText token,title,body;
    private AdapterOfIssueInfo adapter;
    private List<IssueInfo> list = new ArrayList<>();
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);

        addIssue = (Button)findViewById(R.id.issue_btn);
        token = (EditText)findViewById(R.id.issue_token);
        body = (EditText)findViewById(R.id.issue_body);
        title = (EditText)findViewById(R.id.issue_title);
        listView = (ListView)findViewById(R.id.issue_listview);
        adapter = new AdapterOfIssueInfo(IssueActivity.this,R.layout.issue_item,list);
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String repo = intent.getStringExtra("repo");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        GetRequestInterface requestInterface = retrofit.create(GetRequestInterface.class);

        Observable<List<IssueInfo>> observable = requestInterface.getIssue(username,repo);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<IssueInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<IssueInfo> issueInfos) {
                        if(issueInfos.isEmpty()){
                            Toast.makeText(IssueActivity.this,"No Issue",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for(IssueInfo item : issueInfos){
                            list.add(item);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
