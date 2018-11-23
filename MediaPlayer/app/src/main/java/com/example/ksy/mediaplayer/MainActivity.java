package com.example.ksy.mediaplayer;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private TextView startTimeText,endTimeText,nameText;
    private ImageView playImage,stopImage,backImage,fileImage;
    private CircleImageView circleImageView;
    private MusicService musicService;
    private ServiceConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicService = new MusicService();
        mediaPlayer = new MediaPlayer();
        startTimeText = (TextView)findViewById(R.id.start_time);
        endTimeText = (TextView)findViewById(R.id.end_time);
        nameText = (TextView)findViewById(R.id.name);
        playImage = (ImageView)findViewById(R.id.play);
        stopImage = (ImageView)findViewById(R.id.stop);
        backImage = (ImageView)findViewById(R.id.back);
        fileImage = (ImageView)findViewById(R.id.file);
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                musicService = ((MusicService.MyBinder)service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent bindIntent = new Intent(this,MusicService.class);
        bindService(bindIntent,connection,BIND_AUTO_CREATE);

        try{
            if(ContextCompat. checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }else{
                mediaPlayer.setDataSource(Environment.getExternalStorageDirectory()+"/data/山高水长.mp3");
                mediaPlayer.prepareAsync();
            }

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this,"There is no file",Toast.LENGTH_SHORT).show();
        }

        playImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }else {
                    mediaPlayer.pause();
                }
            }
        });

        stopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.reset();
                initMediaPlayer();
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(connection);
                finish();
                System.exit(0);
            }
        });
    }

    private void initMediaPlayer(){
        try{
            mediaPlayer.setDataSource(Environment.getExternalStorageDirectory()+"/data/山高水长.mp3");
            mediaPlayer.prepareAsync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
