package com.example.kesy.lab2_week6;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class StaticReceiver extends BroadcastReceiver {
    private static final String STATICACTION = "com.example.kesy.Lab3.MyStaticFiler";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(STATICACTION)){
            Bundle bundle = intent.getExtras();
            Item item =(Item)bundle.getSerializable("itemBroadcast");

            NotificationManager manager =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            Notification notify = builder.build();
            builder.setContentTitle("今日推荐")
                    .setContentText(item.getTextViewContent())
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.empty_star)
                    .setAutoCancel(true);
            //Toast.makeText(context,"Get Broadcast",Toast.LENGTH_SHORT).show();
            Intent mintent = new Intent(context,DetailActivity.class);
            Bundle mbundle =new Bundle();
            mbundle.putSerializable("itemMessage",item);
            mintent.putExtras(mbundle);
            PendingIntent mPendingIntent = PendingIntent.getActivity(context,0,mintent,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(mPendingIntent);
            notify.defaults |=Notification.DEFAULT_SOUND;
            manager.notify(0,notify);
        }
    }
}
