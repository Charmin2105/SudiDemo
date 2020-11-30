package com.projectsudi_ostfalia_android.sudidemo;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class AudioActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Button playButton;
    private View.OnClickListener onClickListenerPlayButton;
    private SeekBar seekBar;
    private Handler handler;
    private Runnable runnable;
    private TextView textViewAudioInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);


        SetUp();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        mediaPlayer.release();
        mediaPlayer = null;
    }


    void SetUp(){
        mediaPlayer = MediaPlayer.create(this,R.raw.audiofile);

        playButton = findViewById(R.id.audioPlay);

        onClickListenerPlayButton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    playButton.setText("Continue");
                }
                else {
                    mediaPlayer.start();
                    playButton.setText("Stop");
                }

            }
        };
        playButton.setOnClickListener(onClickListenerPlayButton);

        textViewAudioInfo = (TextView)findViewById(R.id.audioInfo);

        seekBar = (SeekBar)findViewById(R.id.audioSeekbar);
        handler = new Handler();

        initializeSeekBar();


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer!=null && b){

                    mediaPlayer.seekTo(i*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    protected void getAudioStats(){
        int duration  = mediaPlayer.getDuration()/1000; // In milliseconds
        int due = (mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition())/1000;
        int pass = duration - due;

        int min = (duration % 3600) / 60;
        int sec = duration % 60;
        int minpass = (pass % 3600) / 60;
        int secpass = pass % 60;


        textViewAudioInfo.setText(String.format(minpass+":"+ "%02d" + "/" + min + ":" + "%02d", secpass,sec));
    }

    protected void initializeSeekBar(){
        seekBar.setMax(mediaPlayer.getDuration()/1000);

        runnable = new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000; // In milliseconds
                    seekBar.setProgress(mCurrentPosition);
                    getAudioStats();
                }
                handler.postDelayed(runnable,1000);
            }
        };
        handler.postDelayed(runnable,1000);
    }
}
