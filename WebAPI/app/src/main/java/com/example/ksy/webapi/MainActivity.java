package com.example.ksy.webapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private List<APIInfo>itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private Button searchBtn;
    private TextView numberTextView;
    //private Observable<APIInfo> observable;
    //private Observer<APIInfo> observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        numberTextView = (TextView)findViewById(R.id.number);
        searchBtn = (Button)findViewById(R.id.btn);
        itemAdapter = new ItemAdapter(itemList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(itemAdapter);

        /*observable = Observable.create(new ObservableOnSubscribe<APIInfo>() {
            @Override
            public void subscribe(ObservableEmitter<APIInfo> emitter) throws Exception {

            }
        });
        observer = new Observer<APIInfo>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(APIInfo apiInfo) {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        }*/

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = numberTextView.getText().toString();
                if(!id.matches("[0-9]+")){
                    Toast.makeText(MainActivity.this,"只能输入数字",Toast.LENGTH_SHORT).show();
                }else{
                    HttpUtil.sendGetRequest(MainActivity.this, "https://space.bilibili.com/ajax/top/showTop?mid=" + id, new HttpGetCallbackListener() {
                        @Override
                        public void onFinish(String data) {
                            Gson gson = new Gson();
                            final APIInfo info = gson.fromJson(data,APIInfo.class);
                            Observable.create(new ObservableOnSubscribe<APIInfo>() {
                                @Override
                                public void subscribe(ObservableEmitter<APIInfo> emitter) throws Exception {
                                    emitter.onNext(info);
                                }
                            }).subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(new Observer<APIInfo>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                }

                                @Override
                                public void onNext(APIInfo apiInfo) {
                                    itemList.add(apiInfo);
                                    itemAdapter.notifyDataSetChanged();
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
                }
            }
        });
    }
}
