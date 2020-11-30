package com.projectsudi_ostfalia_android.sudidemo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.VideoView;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VideoActivity extends AppCompatActivity {

    private int mVideoId;

    private VideoView mVideoView;
    private MediaController mMediaController;
    private FirebaseUser mUser = null;
    private String TAG = "SudiDEMO VideoDemo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        init();
        addEventListener();

    }

    public void init() {


        // Video ID wird aus Inetent gerufen
        mVideoId = R.raw.rieselvideo_720p;

        // VideoView wird initialisiert
        mVideoView = (VideoView) findViewById(R.id.videoview01
        );
        mMediaController = new MediaController(this) {
            @Override
            public void show() {
                super.show(0);
            }
        };
        mMediaController.setAnchorView(mVideoView);

        mVideoView.setMediaController(mMediaController);
        // Video wird der VideoView zugefügt
        mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + mVideoId));

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            // Video wird gestartet
            mVideoView.start();
        } else {
            Toast.makeText(getApplicationContext(), "Um dieses Video sehen zu können bitte mit Firebase einloggen.",
                    Toast.LENGTH_SHORT).show();
        }


    }

    public void addEventListener() {


    }
}
