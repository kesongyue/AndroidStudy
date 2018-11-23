package com.example.ksy.mediaplayer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service {
    private IBinder myBinder = new MyBinder();
    public class MyBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }
}
