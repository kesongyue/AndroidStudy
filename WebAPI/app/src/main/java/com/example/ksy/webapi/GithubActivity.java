package com.example.ksy.webapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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

public class GithubActivity extends AppCompatActivity {
    private Button searchBtn;
    private EditText editText;
    private ListView listView;
    private MyListViewAdapter myListViewAdapter;
    private List<ListItem> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github);

        searchBtn = (Button)findViewById(R.id.github_btn);
        editText = (EditText)findViewById(R.id.github_username);
        listView = (ListView)findViewById(R.id.list_view);
        myListViewAdapter = new MyListViewAdapter(GithubActivity.this,R.layout.github_item,list);
        listView.setAdapter(myListViewAdapter);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                myListViewAdapter.notifyDataSetChanged();
                final String username = editText.getText().toString();
                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.github.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();

                GetRequestInterface requestInterface = retrofit.create(GetRequestInterface.class);

                Observable<List<RepoMessage>> observable = requestInterface.getRepo(username);
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<RepoMessage>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(List<RepoMessage> repoMessages) {
                                for(RepoMessage repo : repoMessages){
                                    if(repo.GetHasIssues()) {
                                        ListItem item = new ListItem(repo.GetName(), repo.GetId(), repo.GetIssuesCount(), repo.GetDescription(),username);
                                        list.add(item);
                                    }
                                }
                                myListViewAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem item = list.get(position);
                Intent intent = new Intent(GithubActivity.this,IssueActivity.class);
                intent.putExtra("username",item.GetUsername());
                intent.putExtra("repo",item.GetProjectName());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        list.clear();
        myListViewAdapter.notifyDataSetChanged();
    }
}
