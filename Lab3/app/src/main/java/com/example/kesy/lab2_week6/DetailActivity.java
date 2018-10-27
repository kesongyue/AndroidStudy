package com.example.kesy.lab2_week6;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

public class DetailActivity extends AppCompatActivity {

    private String[] data = {"分享信息","不感兴趣","查看更多信息","出错反馈"};
    private boolean isClickStar = false;
    private DynamicReceiver dynamicReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        IntentFilter dynamicFilter = new IntentFilter();
        dynamicFilter.addAction(DynamicReceiver.DYNAMICACTION);
        dynamicReceiver = new DynamicReceiver();
        registerReceiver(dynamicReceiver,dynamicFilter);

        ArrayAdapter<String>adapter = new ArrayAdapter<String>(DetailActivity.this,R.layout.operations,data);
        ListView listView = (ListView)findViewById(R.id.detail_listview);
        listView.setAdapter(adapter);

        final Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();

        TextView name = (TextView)findViewById(R.id.detail_item_name);
        final Item item =(Item)bundle.getSerializable("itemMessage");

        name.setText(item.getTextViewContent());
        TextView category = (TextView)findViewById(R.id.detail_item_category);
        category.setText(item.getCategory());
        TextView nuritent = (TextView)findViewById(R.id.detail_item_nuritent);
        nuritent.setText(item.getNutrient());
        ImageView bgColor = (ImageView)findViewById(R.id.detail_background_color);
        bgColor.setBackgroundResource(item.getBgColor());

        final ImageButton starBtn = (ImageButton)findViewById(R.id.detail_star_btn);
        starBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isClickStar){
                    starBtn.setImageResource(R.drawable.empty_star);
                    isClickStar = false;
                }else{
                    starBtn.setImageResource(R.drawable.full_star);
                    isClickStar = true;
                }
            }
        });
        ImageButton collectBtn = (ImageButton)findViewById(R.id.detail_item_collect);
        collectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setCollected(true);
                Toast.makeText(DetailActivity.this,"已收藏",Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(item);

                Intent intentBroadcast =new Intent(DynamicReceiver.DYNAMICACTION);
                Bundle bundle1 =new Bundle();
                bundle.putSerializable("itemMessage",item);
                intentBroadcast.putExtras(bundle);
                sendBroadcast(intentBroadcast);
            }
        });

        ImageButton backBtn = (ImageButton)findViewById(R.id.detail_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("backItemMessage",item);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);*/
                finish();
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(dynamicReceiver);
    }
}
