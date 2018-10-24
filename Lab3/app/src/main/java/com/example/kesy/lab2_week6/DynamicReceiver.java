package com.example.kesy.lab2_week6;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class DynamicReceiver extends BroadcastReceiver{
    public static final String DYNAMICACTION = "com.example.kesy.Lab3.MyDynamicFilter";

    @Override
    public void onReceive(Context context,Intent intent){
        if(intent.getAction().equals(DYNAMICACTION)){
            NotificationManager manager =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            Notification notify = builder.build();
            builder.setContentTitle("已收藏")
                    .setContentText(intent.getStringExtra("itemName"))
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.empty_star)
                    .setAutoCancel(true);
            Intent mintent = new Intent(context,MainActivity.class);
            mintent.putExtra("itemName",intent.getStringExtra("itemName"));
            mintent.putExtra("isClickedFloatingActionButton",true);
            PendingIntent mPendingIntent = PendingIntent.getActivity(context,0,mintent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(mPendingIntent);
            notify.defaults |=Notification.DEFAULT_SOUND;
            manager.notify(1,notify);
        }
    }

}
