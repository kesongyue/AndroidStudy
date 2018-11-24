package com.example.ksy.mediaplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {
    private IBinder myBinder = new MyBinder();
    public static MediaPlayer mediaPlayer = null;
    private static String mediaPath;
    public MusicService(){
        //Log.d("MusicService","Gouzhao");
        if(mediaPlayer == null){
            mediaPlayer= new MediaPlayer();
            initMediaPlayer();
        }
    }

    public class MyBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    //TODO :播放或暂停
    public boolean playOrPause(){
        Log.d("MusicService","playOrPause");
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            return true;
        }else {
            mediaPlayer.pause();
            return false;
        }
    }

    //TODO :设置播放位置
    public void seekTo(int pos){
        mediaPlayer.seekTo(pos);
    }

    //TODO : 返回正在播放的音频的路径
    public String getMediaPath(){
        return mediaPath;
    }

    //TODO: 停止播放音乐并将播放位置设为0
    public void stop(){
        mediaPlayer.stop();
        try {
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }

        mediaPlayer.seekTo(0);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    //TODO :初始化播放设置
    private void initMediaPlayer(){
        try{
            //mediaPlayer.reset();
            mediaPath = Environment.getExternalStorageDirectory()+"/data/山高水长.mp3";
            mediaPlayer.setDataSource(mediaPath);
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //TODO :设置播放的音频路径
    public void setPlaySource(String path){
        try{
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            Log.d("MusicService",path);
            mediaPath = path;
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
