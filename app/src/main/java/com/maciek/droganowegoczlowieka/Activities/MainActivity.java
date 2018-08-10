package com.maciek.droganowegoczlowieka.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.maciek.droganowegoczlowieka.DB.InsertPositionToList;
import com.maciek.droganowegoczlowieka.DB.TouristListContract;
import com.maciek.droganowegoczlowieka.DB.TuristListDbHelper;
import com.maciek.droganowegoczlowieka.DB.TuristListDbQuery;
import com.maciek.droganowegoczlowieka.R;
import com.maciek.droganowegoczlowieka.Utilities.AndroidDatabaseManager;
import com.maciek.droganowegoczlowieka.Utilities.DownloadService;
import com.maciek.droganowegoczlowieka.Utilities.VolleyGetRequest;

import static com.maciek.droganowegoczlowieka.Activities.MediaPlayerActivity.TRACK_PROGRESS;
import static com.maciek.droganowegoczlowieka.Activities.TrackListActivity.TITLE;
import static com.maciek.droganowegoczlowieka.Activities.TrackListActivity.TYPE_ID;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<byte[]>, Response.ErrorListener{

    private Button touristButton, homeChurchButton, debuggerButton, oazaYouthButton, advancedButton;
    private ProgressBar progressBar;
    private SQLiteDatabase db;
    private int progressStatus = 0;
    private Handler mHandler = new Handler();
    private  Cursor cursor;
    private ContentLoadingProgressBar loader;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TuristListDbHelper turistListDbHelper = new TuristListDbHelper(this);
        db = turistListDbHelper.getWritableDatabase();
        touristButton = findViewById(R.id.button_tourist);
        homeChurchButton = findViewById(R.id.button_home_church);
        debuggerButton = findViewById(R.id.button_db_debugger);
        oazaYouthButton = findViewById(R.id.button_oaza_youth);
        advancedButton = findViewById(R.id.button_advanced);
        advancedButton.setOnClickListener(this);
        oazaYouthButton.setOnClickListener(this);
        debuggerButton.setOnClickListener(this);
        touristButton.setOnClickListener(this);
        homeChurchButton.setOnClickListener(this);
        loader = findViewById(R.id.loader);
        SharedPreferences sharedPreferences = this.getSharedPreferences(
                getString(R.string.was_download_succesfull), this.MODE_PRIVATE);
    }


    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View view) {
        Intent mIntent = new Intent(this, MediaPlayerActivity.class);
        mIntent.putExtra(TRACK_PROGRESS,0);

        switch (view.getId()) {
            case R.id.button_tourist:
                mIntent.putExtra(TITLE, "turysta-wstep.mp3");
                mIntent.putExtra(TYPE_ID, "1");
                startActivity(mIntent);
                break;
            case R.id.button_home_church:
                mIntent.putExtra(TITLE, "domowy-kosciol-wstep.mp3");
                mIntent.putExtra("type_id", "3");
                startActivity(mIntent);
                break;
            case R.id.button_db_debugger:
                startActivity(new Intent(this, AndroidDatabaseManager.class));
                break;
            case R.id.button_advanced:
                mIntent.putExtra(TITLE, "moderator-wstep.mp3");
                mIntent.putExtra("type_id", "4");
                startActivity(mIntent);
                break;
            case R.id.button_oaza_youth:
                mIntent.putExtra(TITLE, "oazowicz-wstep.mp3");
                mIntent.putExtra("type_id", "2");
                startActivity(mIntent);
                break;


        }
    }




    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(byte[] response) {

    }


    @Override
    protected void onResume() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.was_download_succesfull),Context.MODE_PRIVATE);
        int isSuccesful=sharedPreferences.getInt(getString(R.string.was_download_succesfull), 0);
        if(isSuccesful==4){
            touristButton.setVisibility(View.VISIBLE);
            homeChurchButton.setVisibility(View.VISIBLE);
            debuggerButton.setVisibility(View.VISIBLE);
            oazaYouthButton.setVisibility(View.VISIBLE);
            advancedButton.setVisibility(View.VISIBLE);
        }
        if(tableIsEmpty(db)){
//            reCreatedb();
            VolleyGetRequest volleyGetRequest = new VolleyGetRequest(this, db);
            loader.setVisibility(View.VISIBLE);
            volleyGetRequest.getNameAndPosition(1,loader, this);
            volleyGetRequest.getNameAndPosition(2, loader, this);
            volleyGetRequest.getNameAndPosition(3, loader, this);
            volleyGetRequest.getNameAndPosition(4, loader, this);
        }
        super.onResume();

    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    private static boolean tableIsEmpty(SQLiteDatabase db){
        String count = "SELECT count(*) FROM "+TouristListContract.TouristListEntry.TABLE_NAME;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount>0){
            return false;
        }else {
            return true;
        }

    }

    private  void reCreatedb(){
        db.execSQL("DROP TABLE IF EXISTS " + TouristListContract.TouristListEntry.TABLE_NAME);
        db.execSQL("CREATE TABLE " + TouristListContract.TouristListEntry.TABLE_NAME + " (" +
                TouristListContract.TouristListEntry._ID + " INTEGER PRIMARY KEY," +
                TouristListContract.TouristListEntry.COLUMN_POSITION + " NUMBER,"+
                TouristListContract.TouristListEntry.COLUMN_AUDIO + " TEXT," +
                TouristListContract.TouristListEntry.COLUMN_NAME + " TEXT," +
                TouristListContract.TouristListEntry.COLUMN_AUDIO_URI + " TEXT," +
                TouristListContract.TouristListEntry.COLUMN_PICTURE + " TEXT," +
                TouristListContract.TouristListEntry.COLUMN_PICTURE_URI + " TEXT," +
                TouristListContract.TouristListEntry.COLUMN_VIDEO + " TEXT," +
                TouristListContract.TouristListEntry.COLUMN_VIDEO_URI + " TEXT," +
                TouristListContract.TouristListEntry.COLUMN_IS_ACTIVE + " BOOLEAN," +
                TouristListContract.TouristListEntry.COLUMN_TYPE_ID + " NUMBER);");
        finish();
    }






}
