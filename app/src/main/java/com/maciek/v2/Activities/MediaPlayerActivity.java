package com.maciek.v2.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.maciek.v2.Adapter.SlidingImageAdapter;
import com.maciek.v2.DB.TouristListContract;
import com.maciek.v2.DB.TuristListDbHelper;
import com.maciek.v2.DB.TuristListDbQuery;
import com.maciek.v2.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.maciek.v2.Activities.TrackListActivity.TITLE;
import static com.maciek.v2.Activities.TrackListActivity.TYPE_ID;

public class MediaPlayerActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {

    private SQLiteDatabase db;
    private TuristListDbQuery turistListDbQuery;
    private TuristListDbHelper turistListDbHelper;
    private FloatingActionButton mFloatingActionButton;
    private TextView mTextView, trackTitleTextView;
    private Cursor cursor;
    ArrayList<String> listOfImagesSorted;
    private static ViewPager viewPager;
    public static String POSITION = "POSITION";

    private ImageButton previous, next, start;
    private Button showList, goToMainMenu;
    private ImageView imageView;
    int index = -1;
    String title;
    private String typeId;
    private Map<Integer, String> mapAudio, mapVideo, mapImage, mapTitle;

    private MediaPlayer mMediaPlayer;
    public static String TRACK_PROGRESS = "TRACK_PROGRESS";
    int trackProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        Intent intent = getIntent();
        title = intent.getStringExtra(TITLE);
        typeId = intent.getStringExtra(TYPE_ID);
        trackProgress = intent.getIntExtra(TRACK_PROGRESS, -1);

        turistListDbHelper = new TuristListDbHelper(this);
        db = turistListDbHelper.getReadableDatabase();
        turistListDbQuery = new TuristListDbQuery(db);
        cursor = turistListDbQuery.getAudioUriImageUriVideoUriPosByTypeId(typeId);

        int audioNameIndex = cursor.getColumnIndex(TouristListContract.TouristListEntry.COLUMN_NAME);
        int posIndex = cursor.getColumnIndex(TouristListContract.TouristListEntry.COLUMN_POSITION);

        mapAudio = new HashMap<>();
        mapImage = new HashMap<>();
        mapVideo = new HashMap<>();
        mapTitle = new LinkedHashMap<>();
        listOfImagesSorted = new ArrayList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getString(audioNameIndex) != null || !cursor.getString(audioNameIndex).equals("null")) {
                mapTitle.put(cursor.getInt(posIndex), cursor.getString(audioNameIndex));

            }
        }
        mapTitle = updateMap(mapTitle);
        if (mapTitle.get(mapTitle.keySet().size()) == null) {
            turistListDbQuery.updatePosition(mapTitle, typeId);
        }

        cursor = turistListDbQuery.getAudioUriImageUriVideoUriPosByTypeId(typeId);
        int audioUriIndex = cursor.getColumnIndex(TouristListContract.TouristListEntry.COLUMN_AUDIO_URI);
        int videoUriIndex = cursor.getColumnIndex(TouristListContract.TouristListEntry.COLUMN_VIDEO_URI);
        int imgUriIndex = cursor.getColumnIndex(TouristListContract.TouristListEntry.COLUMN_PICTURE_URI);
        posIndex = cursor.getColumnIndex(TouristListContract.TouristListEntry.COLUMN_POSITION);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getString(audioUriIndex) != null || !cursor.getString(audioUriIndex).equals("null")) {
                mapAudio.put(cursor.getInt(posIndex), cursor.getString(audioUriIndex));

            }

            if (cursor.getString(videoUriIndex) != null) {
                if (!cursor.getString(videoUriIndex).equals("null")) {
                    mapVideo.put(cursor.getInt(posIndex), cursor.getString(videoUriIndex));
                }
            }
            if (cursor.getString(imgUriIndex) != null || !cursor.getString(imgUriIndex).equals("null")) {
                mapImage.put(cursor.getInt(posIndex), cursor.getString(imgUriIndex));
                listOfImagesSorted.add(cursor.getString(imgUriIndex));
            }
        }


        //guziczki
        previous = findViewById(R.id.button_previous);
        next = findViewById(R.id.next_button);
        start = findViewById(R.id.start_stop_button);
        mTextView = findViewById(R.id.text_view_current_song);
        mFloatingActionButton = findViewById(R.id.launch_media_player);
        trackTitleTextView = findViewById(R.id.track_name_text_view);
        goToMainMenu = findViewById(R.id.GoToMainMenuButton);
        goToMainMenu.setOnClickListener(this);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        start.setOnClickListener(this);
        mFloatingActionButton.setOnClickListener(this);
        imageView = findViewById(R.id.image_view_media_player);
        showList = findViewById(R.id.showList);
        viewPager = findViewById(R.id.pager);
        showList.setOnClickListener(this);
        // koniec Guziczków

        viewPager.setAdapter(new SlidingImageAdapter(MediaPlayerActivity.this, listOfImagesSorted));
        start.setBackgroundColor(getResources().getColor(R.color.ziolny_ciemny_michala));

        initMediaPlayer();

        if (intent.getIntExtra(POSITION, -1) == -1) {
            try {
                skipNext();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        mTextView.setText(index + ". " + mapTitle.get(index));
        mMediaPlayer.setOnCompletionListener(this);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
            }

            @Override
            public void onPageSelected(int position) {
                start.setImageResource(R.drawable.ic_pause_circle);
                ispressed = false;
                start.setBackgroundColor(getResources().getColor(R.color.ziolny_ciemny_michala));
                if (position > index) {
                    try {
                        skipNext();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        skipPrevious();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        switch (typeId) {
            case "1":
                trackTitleTextView.setText("Turysta");
                break;
            case "2":
                trackTitleTextView.setText("Oaza");
                break;
            case "3":
                trackTitleTextView.setText("Domowy kościół");
                break;
            case "4":
                trackTitleTextView.setText("Zaawansowani");
                break;
        }


    }

    private boolean initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.reset();
        return true;
    }

    static Map<Integer, String> updateMap(Map<Integer, String> map) {

        Integer a = map.size();
        Map<Integer, String> orderedMap = new LinkedHashMap<>();
        for (int i = 0; i < a; i++) {
            if (map.keySet().iterator().hasNext()) {
                if (map.get(map.keySet().iterator().next()) == null) {
                    map.remove(map.keySet().iterator().next());
                    a--;
                }
                orderedMap.put(i, map.get(map.keySet().iterator().next()));
                map.remove(map.keySet().iterator().next());
            }
        }

        return orderedMap;
    }

    @Override
    protected void onResume() {
        Intent intent = getIntent();
        if (intent.getIntExtra(POSITION, -1) != -1) {
            index = intent.getIntExtra(POSITION, -1);
            int temp = index;
            ++index;
            if (temp == 0) {
                index--;
            }
            resumePlaying();
            viewPager.setCurrentItem(temp);

        } else if (trackProgress != -1) {
            int position = intent.getIntExtra(TRACK_PROGRESS, 0);
            mMediaPlayer.seekTo(position);
            mMediaPlayer.start();
        }

        super.onResume();

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onPause() {
        pauseMedia();
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            stopMedia();
            mMediaPlayer.release();
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    int temp;
    boolean ispressed = false;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_button:
                try {
                    index++;
                    viewPager.setCurrentItem(index);
                    skipNext();


                } catch (IOException e) {
                    e.printStackTrace();
                }
                ispressed = false;
                start.setImageResource(R.drawable.ic_pause_circle);
                start.setBackgroundColor(getResources().getColor(R.color.ziolny_ciemny_michala));

                break;
            case R.id.start_stop_button:


                if (ispressed) {
                    ispressed = false;
                    playMedia();
                    start.setImageResource(R.drawable.ic_pause_circle);
                    start.setBackgroundColor(getResources().getColor(R.color.ziolny_ciemny_michala));

                } else {
                    ispressed = true;
                    pauseMedia();
                    start.setImageResource(R.drawable.ic_play_white);
                    start.setBackgroundColor(getResources().getColor(R.color.zielony_michala));


                }
                break;
            case R.id.button_previous:
                try {
                    index++;
                    temp = index;
                    viewPager.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            viewPager.setCurrentItem(temp - 2);

                        }
                    }, 50);

                    skipPrevious();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                ispressed = false;
                start.setImageResource(R.drawable.ic_pause_circle);
                start.setBackgroundColor(getResources().getColor(R.color.ziolny_ciemny_michala));
                break;
            case R.id.showList:
                Intent intentTrackList = new Intent(this, TrackListActivity.class);
                intentTrackList.putExtra(TYPE_ID, typeId);
                intentTrackList.putExtra(TITLE, mapTitle.get(index));
                intentTrackList.putExtra(POSITION, index);
                startActivity(intentTrackList);
                break;
            case R.id.GoToMainMenuButton:
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.launch_media_player:
                Intent intent = new Intent(this, VideoPlayerActivity.class);
                intent.putExtra(TYPE_ID, typeId);
                intent.putExtra(TITLE, mapTitle.get(index));
                intent.putExtra("URI", mapVideo.get(index));
                intent.putExtra(POSITION, index);
                intent.putExtra(TRACK_PROGRESS, mMediaPlayer.getCurrentPosition());
                startActivity(intent);

        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mMediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mMediaPlayer) {
        mMediaPlayer.seekTo(0);
        ispressed = true;
        start.setImageResource(R.drawable.ic_play_white);
        start.setBackgroundColor(getResources().getColor(R.color.zielony_michala));

    }

    private void skipNext() throws IOException {
        if (index == mapAudio.size() - 1) {
            index = mapAudio.size() - 1;
        } else {
            index++;
        }
        stopMedia();
        mMediaPlayer.reset();
        mMediaPlayer.setDataSource("file://" + mapAudio.get(index));
        mMediaPlayer.prepare();
        mMediaPlayer.start();
        mTextView.setText(index + ". " + mapTitle.get(index));

        if (mapVideo.containsKey(index)) {
            mFloatingActionButton.setVisibility(View.VISIBLE);
        } else {
            mFloatingActionButton.setVisibility(View.GONE);
        }

    }

    private void resumePlaying() {
        stopMedia();
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource("file://" + mapAudio.get(index));
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
        mTextView.setText(index + ". " + mapTitle.get(index));

        if (mapVideo.containsKey(index)) {
            mFloatingActionButton.setVisibility(View.VISIBLE);
        } else {
            mFloatingActionButton.setVisibility(View.GONE);
        }
    }

    private void skipPrevious() throws IOException {
        if (index == 0) {
            index = 0;
        } else {
            index--;
        }
        stopMedia();
        mMediaPlayer.reset();
        mMediaPlayer.setDataSource("file://" + mapAudio.get(index));
        mMediaPlayer.prepare();
        mMediaPlayer.start();
        mTextView.setText(index + ". " + mapTitle.get(index));

        if (mapVideo.containsKey(index)) {
            mFloatingActionButton.setVisibility(View.VISIBLE);
        } else {
            mFloatingActionButton.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onError(MediaPlayer mMediaPlayer, int i, int i1) {
        switch (i) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + i1);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + i1);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + i1);
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mMediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mMediaPlayer) {
        mMediaPlayer.start();
    }

    @Override
    public void onSeekComplete(MediaPlayer mMediaPlayer) {

    }


    private void playMedia() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    public void stopMedia() {
        if (mMediaPlayer == null) return;
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    int resumePosition;

    public void pauseMedia() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            resumePosition = mMediaPlayer.getCurrentPosition();
        }
    }

    public void resumeMedia() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.seekTo(resumePosition);
            mMediaPlayer.start();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        stopMedia();
    }
}
