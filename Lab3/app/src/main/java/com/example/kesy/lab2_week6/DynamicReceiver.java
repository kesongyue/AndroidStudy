package com.example.kesy.lab2_week6;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

public class DynamicReceiver extends BroadcastReceiver{
    public static final String DYNAMICACTION = "com.example.kesy.Lab3.MyDynamicFilter";

    @Override
    public void onReceive(Context context,Intent intent){
        Bundle bundle = intent.getExtras();
        Item item = (Item)bundle.getSerializable("itemMessage");
        if(intent.getAction().equals(DYNAMICACTION)){
            NotificationManager manager =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            Notification notify = builder.build();
            builder.setContentTitle("已收藏")
                    .setContentText(item.getTextViewContent())
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.empty_star)
                    .setAutoCancel(true);
            Intent mintent = new Intent(context,MainActivity.class);
            mintent.putExtra("itemName",item.getTextViewContent());
            mintent.putExtra("isClickedFloatingActionButton",true);
            PendingIntent mPendingIntent = PendingIntent.getActivity(context,0,mintent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(mPendingIntent);
            notify.defaults |=Notification.DEFAULT_SOUND;
            manager.notify(1,notify);
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if(intent.getAction().equals(DynamicReceiver.DYNAMICACTION)){
            String name =item.getTextViewContent();
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);//实例化RemoteView,其对应相应的Widget布局
            views.setTextViewText(R.id.appwidget_text,"已收藏  "+name);
            Intent i = new Intent(context, MainActivity.class);
            i.putExtra("isClickedFloatingActionButton",true);
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.appwidget_image, pi); //设置点击事件
            ComponentName me = new ComponentName(context, NewAppWidget.class);
            appWidgetManager.updateAppWidget(me, views);
        }
    }

}
