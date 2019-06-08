package com.maciek.v2.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.maciek.v2.R;

import static com.maciek.v2.Activities.MediaPlayerActivity.POSITION;
import static com.maciek.v2.Activities.MediaPlayerActivity.TRACK_PROGRESS;
import static com.maciek.v2.Activities.TrackListActivity.TITLE;
import static com.maciek.v2.Activities.TrackListActivity.TYPE_ID;

public class VideoPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    String typeId;
    String title;
    int position;
    int trackProgress;
    private FloatingActionButton mFloatingButton;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        Intent intent = getIntent();
        String uriToLunch = intent.getStringExtra("URI");
        position = intent.getIntExtra(POSITION, -1);
        trackProgress = intent.getIntExtra(TRACK_PROGRESS, -1);
        videoView = findViewById(R.id.video_view);
        mFloatingButton = findViewById(R.id.launch_main_video_activity);
        if (uriToLunch == null) {
            videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.introdnc);
            mFloatingButton.setVisibility(View.VISIBLE);
            mFloatingButton.setOnClickListener(this);

        } else {
            videoView.setVideoURI(Uri.parse("file://" + uriToLunch));
            mFloatingButton.setVisibility(View.VISIBLE);
            mFloatingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), MediaPlayerActivity.class);
                    intent.putExtra(TYPE_ID, typeId);
                    intent.putExtra(TITLE, title);
                    intent.putExtra(POSITION, position);
                    intent.putExtra(TRACK_PROGRESS, trackProgress);
                    startActivity(intent);
                }
            });
        }
        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();
        typeId = intent.getStringExtra(TYPE_ID);
        title = intent.getStringExtra(TITLE);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (typeId == null) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    mFloatingButton.setVisibility(View.GONE);
                } else {
                    Intent intent = new Intent(getApplicationContext(), MediaPlayerActivity.class);
                    intent.putExtra(TYPE_ID, typeId);
                    intent.putExtra(TITLE, title);
                    intent.putExtra(POSITION, position);
                    intent.putExtra(TRACK_PROGRESS, trackProgress);
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (typeId != null && title != null) {
            Intent intent = new Intent(this, MediaPlayerActivity.class);
            intent.putExtra(TYPE_ID, typeId);
            intent.putExtra(TITLE, title);
            intent.putExtra(POSITION, position);
            intent.putExtra(TRACK_PROGRESS, trackProgress);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.launch_main_video_activity:
                startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (videoView != null && !videoView.isPlaying()) {
                videoView.start();
            }
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}
