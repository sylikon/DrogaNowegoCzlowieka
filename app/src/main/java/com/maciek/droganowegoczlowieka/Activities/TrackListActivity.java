package com.maciek.droganowegoczlowieka.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maciek.droganowegoczlowieka.Adapter.TrackListAdapter;
import com.maciek.droganowegoczlowieka.DB.InsertPositionToList;
import com.maciek.droganowegoczlowieka.DB.TouristListContract;
import com.maciek.droganowegoczlowieka.DB.TuristListDbHelper;
import com.maciek.droganowegoczlowieka.DB.TuristListDbQuery;
import com.maciek.droganowegoczlowieka.R;
import com.maciek.droganowegoczlowieka.Utilities.DownloadService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static com.maciek.droganowegoczlowieka.Activities.MediaPlayerActivity.POSITION;


public class TrackListActivity extends AppCompatActivity implements  TrackListAdapter.ListItemClickListener,  Response.Listener<byte[]>, Response.ErrorListener{
//implements Response.Listener<byte[]>, Response.ErrorListener, TrackListAdapter.ListItemClickListener


    private SQLiteDatabase db;
    int count;
    HashMap<String,String> temp;
    private TrackListAdapter trackListAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    public static String TYPE_ID = "type_id";
    public static String TITLE = "title";
    String typeId;
    private ContentLoadingProgressBar loader;
    private int progressStatus;
    private int cursorMax;
    String title;
    private ProgressBar progressBar;    int i=0;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        TuristListDbHelper turistListDbHelper = new TuristListDbHelper(this);
        db = turistListDbHelper.getReadableDatabase();
        TuristListDbQuery turistListDbQuery = new TuristListDbQuery(db);
        Intent intent = getIntent();
        typeId = intent.getStringExtra(TYPE_ID);
        title = intent.getStringExtra(TITLE);
        position = intent.getIntExtra(POSITION, -1);
        Cursor cursor = turistListDbQuery.getQueriedTouristList(typeId);
        trackListAdapter = new TrackListAdapter(this,cursor,this);
        mRecyclerView.setAdapter(trackListAdapter);
        progressBar =findViewById(R.id.progress_bar);

        temp = new HashMap<>();
        cursor = turistListDbQuery.getAudioCursor(typeId);
        cursorMax=cursor.getCount()*3;
        MainActivity.verifyStoragePermissions(this);
        Toast.makeText(this, "Readable: " + isExternalStorageReadable() + " Writable: " + isExternalStorageWritable(), Toast.LENGTH_LONG).show();
        cursor.close();
        loader = findViewById(R.id.loader_track_list);

//        TODO: sprawdzić czy ktoś wyraził zgodę na używanie internetu// korzystanie z internal storage

    }


    @Override
    public void onListItemClick(int clickedItemIndex, String title) throws IOException {
        Intent intent = new Intent(this, MediaPlayerActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(TYPE_ID, typeId);
        intent.putExtra(POSITION, clickedItemIndex);
        startActivity(intent);

//        TODO po kliknieciu na element odpala media playera i puszcza element z list sciaga URI pliki lokalnego


    }




    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MediaPlayerActivity.class);
        intent.putExtra(TYPE_ID, typeId);
        intent.putExtra(TITLE, title);
        intent.putExtra(POSITION, position);
        startActivity(intent);
        // Otherwise defer to system default behavior.
        super.onBackPressed();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(byte[] response) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }




}
