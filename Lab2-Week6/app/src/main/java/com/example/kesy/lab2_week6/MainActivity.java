package com.example.kesy.lab2_week6;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class MainActivity extends AppCompatActivity {
    private int tag = 0;
    private List<Item> itemRecycleList = new ArrayList<>();
    private List<Item> itemListView = new ArrayList<>();
    private RecyclerView recyclerView;
    private ListView listView;
    private FloatingActionButton floatingActionButton ;
    MyListViewAdapter myListViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initList();

        listView = (ListView)findViewById(R.id.list_view);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.float_action_btn);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final ItemAdapter itemAdapter = new ItemAdapter(itemRecycleList){
            @Override
            public void convert(Item item){
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("itemMessage",item);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            }
        };
        // 添加动画
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(itemAdapter);
        scaleInAnimationAdapter.setDuration(1000);
        recyclerView.setAdapter(scaleInAnimationAdapter);
        recyclerView.setItemAnimator(new OvershootInLeftAnimator());


        myListViewAdapter = new MyListViewAdapter(MainActivity.this,R.layout.item,itemListView);
        listView.setAdapter(myListViewAdapter);
        listView.setVisibility(View.INVISIBLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    return;
                }
               // Log.d("recycleList","has click");
                Item item = itemListView.get(position);
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("itemMessage",item);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    return false;
                }
                final int removePos = position;
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("删除");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        itemListView.remove(removePos);
                        myListViewAdapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.setMessage("确定删除"+itemListView.get(position).getTextViewContent()+"?");
                dialog.show();
                return true;
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(tag == 0){
                    floatingActionButton.setImageResource(R.drawable.mainpage);
                    recyclerView.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    tag = 1;
                }else {
                    floatingActionButton.setImageResource(R.drawable.collect);
                    recyclerView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                    tag = 0;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch(requestCode){
            case 1:
                if (resultCode == RESULT_OK) {
                    Item backItem = (Item)data.getExtras().getSerializable("backItemMessage");
                    for(Item item : itemRecycleList){
                        if(item.getTextViewContent().equals(backItem.getTextViewContent())){
                            if(backItem.getIsCollected()){
                                itemListView.add(item);
                                myListViewAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
                break;
        }
    }
    private void initList(){
        Item soybean = new Item("粮","大豆","粮食","蛋白质",R.color.soybean_color);
        itemRecycleList.add(soybean);
        Item cruciferous = new Item("蔬","十字花科蔬菜","蔬菜","维生素C",R.color.cruciferous_color);
        itemRecycleList.add(cruciferous);
        Item milk = new Item("饮","牛奶","饮品","钙",R.color.milk_color);
        itemRecycleList.add(milk);
        Item fish = new Item("肉","海鱼","肉食","蛋白质",R.color.fish_color);
        itemRecycleList.add(fish);
        Item mushroom = new Item("蔬","菌菇类","蔬菜","微量元素",R.color.mushroom_color);
        itemRecycleList.add(mushroom);
        Item tomato = new Item("蔬","番茄","蔬菜","番茄红素",R.color.tomato_color);
        itemRecycleList.add(tomato);
        Item carrot = new Item("蔬","胡萝卜","蔬菜","胡萝卜素",R.color.carrot_color);
        itemRecycleList.add(carrot);
        Item buckwheat = new Item("粮","荞麦","粮食","膳食纤维",R.color.buckwheat_color);
        itemRecycleList.add(buckwheat);
        Item egg = new Item("杂","鸡蛋","杂","几乎所有营养物质",R.color.egg_color);
        itemRecycleList.add(egg);

        Item collectionFloder = new Item("*","收藏夹","","",R.color.colorPrimary);
        itemListView.add(collectionFloder);
    }

}
