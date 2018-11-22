package com.example.ksy.mediaplayer;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private TextView startTimeText,endTimeText,nameText;
    private ImageView playImage,stopImage,backImage,fileImage;
    private CircleImageView circleImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = new MediaPlayer();
        startTimeText = (TextView)findViewById(R.id.start_time);
        endTimeText = (TextView)findViewById(R.id.end_time);
        nameText = (TextView)findViewById(R.id.name);
        playImage = (ImageView)findViewById(R.id.play);
        stopImage = (ImageView)findViewById(R.id.stop);
        backImage = (ImageView)findViewById(R.id.back);
        fileImage = (ImageView)findViewById(R.id.file);


    }
}
