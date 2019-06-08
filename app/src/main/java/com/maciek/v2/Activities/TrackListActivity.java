package com.maciek.v2.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maciek.v2.Adapter.TrackListAdapter;
import com.maciek.v2.DB.TuristListDbHelper;
import com.maciek.v2.DB.TuristListDbQuery;
import com.maciek.v2.R;

import java.io.IOException;
import java.util.HashMap;

import static com.maciek.v2.Activities.MediaPlayerActivity.POSITION;


public class TrackListActivity extends AppCompatActivity implements  TrackListAdapter.ListItemClickListener,  Response.Listener<byte[]>, Response.ErrorListener{

    HashMap<String,String> temp;
    public static String TYPE_ID = "type_id";
    public static String TITLE = "title";
    String typeId;
    String title;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list);
        RecyclerView mRecyclerView = findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        TuristListDbHelper turistListDbHelper = TuristListDbHelper.getInstance(this);
        SQLiteDatabase db = turistListDbHelper.getReadableDatabase();
        TuristListDbQuery turistListDbQuery = new TuristListDbQuery(db);
        Intent intent = getIntent();
        typeId = intent.getStringExtra(TYPE_ID);
        title = intent.getStringExtra(TITLE);
        position = intent.getIntExtra(POSITION, -1);
        Cursor cursor = turistListDbQuery.getQueriedTouristList(typeId);
        TrackListAdapter trackListAdapter = new TrackListAdapter(this, cursor, this);
        mRecyclerView.setAdapter(trackListAdapter);

        temp = new HashMap<>();
        cursor = turistListDbQuery.getAudioCursor(typeId);
        cursor.close();

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
