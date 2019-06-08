package com.maciek.v2.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maciek.v2.DB.TouristListContract;
import com.maciek.v2.DB.TuristListDbHelper;
import com.maciek.v2.DB.TuristListDbQuery;
import com.maciek.v2.R;
import com.maciek.v2.Utilities.VolleyGetRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.maciek.v2.Activities.MediaPlayerActivity.TRACK_PROGRESS;
import static com.maciek.v2.Activities.TrackListActivity.TITLE;
import static com.maciek.v2.Activities.TrackListActivity.TYPE_ID;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<byte[]>, Response.ErrorListener {

    private Button touristButton;
    private Button homeChurchButton;
    private Button oazaYouthButton;
    private Button advancedButton;
    private VolleyGetRequest volleyGetRequest;
    private SQLiteDatabase db;
    private long lastClickTime = 0;
    private ContentLoadingProgressBar loader;

    public static String DATABASE_VERSION = "DATABASE_VERSION";
    public static String LOCAL_DATABASE_VERSION = "LOCAL_DATABASE_VERSION";
    public static String SHOULD_UPDATE_POSITION_1 = "SHOULD_UPDATE_POSITION_1";
    public static String SHOULD_UPDATE_POSITION_2 = "SHOULD_UPDATE_POSITION_2";
    public static String SHOULD_UPDATE_POSITION_3 = "SHOULD_UPDATE_POSITION_3";
    public static String SHOULD_UPDATE_POSITION_4 = "SHOULD_UPDATE_POSITION_4";
    public static String SHOULD_UPDATE_AFTER_FIRST_DOWNLOAD_1 = "SHOULD_UPDATE_AFTER_FIRST_DOWNLOAD_1";
    public static String SHOULD_UPDATE_AFTER_FIRST_DOWNLOAD_2 = "SHOULD_UPDATE_AFTER_FIRST_DOWNLOAD_2";
    public static String SHOULD_UPDATE_AFTER_FIRST_DOWNLOAD_3 = "SHOULD_UPDATE_AFTER_FIRST_DOWNLOAD_3";
    public static String SHOULD_UPDATE_AFTER_FIRST_DOWNLOAD_4 = "SHOULD_UPDATE_AFTER_FIRST_DOWNLOAD_4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TuristListDbHelper turistListDbHelper = TuristListDbHelper.getInstance(this);
        db = turistListDbHelper.getWritableDatabase();
        touristButton = findViewById(R.id.button_tourist);
        homeChurchButton = findViewById(R.id.button_home_church);
        oazaYouthButton = findViewById(R.id.button_oaza_youth);
        advancedButton = findViewById(R.id.button_advanced);
        advancedButton.setOnClickListener(this);
        oazaYouthButton.setOnClickListener(this);
        touristButton.setOnClickListener(this);
        homeChurchButton.setOnClickListener(this);
        loader = findViewById(R.id.loader);
        volleyGetRequest = new VolleyGetRequest(this, db);
        if (isNetworkAvailable()) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            if (prefs.getInt(LOCAL_DATABASE_VERSION, 0) < prefs.getInt(DATABASE_VERSION, 0)) {
                TuristListDbQuery turistListDbQuery = new TuristListDbQuery(db);
                List<String> list = turistListDbQuery.getActiveAudio();
                volleyGetRequest.getActiveAudioFromServerTable(list, findViewById(android.R.id.content), this);
                editor.putBoolean(SHOULD_UPDATE_POSITION_1, true);
                editor.putBoolean(SHOULD_UPDATE_POSITION_2, true);
                editor.putBoolean(SHOULD_UPDATE_POSITION_3, true);
                editor.putBoolean(SHOULD_UPDATE_POSITION_4, true);
                editor.apply();
            }
            volleyGetRequest.insertCurrentDbVersionToSharedPreferences(this, DATABASE_VERSION);
        } else {
            TextView noInternetTextView = findViewById(R.id.text_view_no_internet);
            Button downloadButton = findViewById(R.id.launch_downloader_button);
            Button closeAppButton = findViewById(R.id.exit_app_button);
            downloadButton.setOnClickListener(this);
            closeAppButton.setOnClickListener(this);
            noInternetTextView.setVisibility(View.VISIBLE);
            downloadButton.setVisibility(View.VISIBLE);
            closeAppButton.setVisibility(View.VISIBLE);
        }


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void startDownloading() {
        volleyGetRequest.insertCurrentDbVersionToSharedPreferences(this, LOCAL_DATABASE_VERSION);
        reCreatedb();
        loader.setVisibility(View.VISIBLE);
        volleyGetRequest.getNameAndPosition(Arrays.asList(1, 2, 3, 4), loader, this);
    }


    @Override
    public void onBackPressed() {

    }

    @Override
    public void onClick(View view) {
        Intent mIntent = new Intent(this, MediaPlayerActivity.class);
        mIntent.putExtra(TRACK_PROGRESS, 0);
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            Toast.makeText(this, getText(R.string.click_on_button_too_fast), Toast.LENGTH_SHORT).show();
            return;
        }

        lastClickTime = SystemClock.elapsedRealtime();

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
            case R.id.launch_downloader_button:
                if (isNetworkAvailable()) {
                    startDownloading();
                } else {
                    Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.exit_app_button:
                finish();
                moveTaskToBack(true);
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
        super.onResume();
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.was_download_succesfull), Context.MODE_PRIVATE);
        int isSuccesful = sharedPreferences.getInt(getString(R.string.was_download_succesfull), 0);
        if (isSuccesful == 4) {
            touristButton.setVisibility(View.VISIBLE);
            homeChurchButton.setVisibility(View.VISIBLE);
            oazaYouthButton.setVisibility(View.VISIBLE);
            advancedButton.setVisibility(View.VISIBLE);

        } else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(SHOULD_UPDATE_AFTER_FIRST_DOWNLOAD_1, true);
            editor.putBoolean(SHOULD_UPDATE_AFTER_FIRST_DOWNLOAD_2, true);
            editor.putBoolean(SHOULD_UPDATE_AFTER_FIRST_DOWNLOAD_3, true);
            editor.putBoolean(SHOULD_UPDATE_AFTER_FIRST_DOWNLOAD_4, true);
            editor.apply();
            if (isNetworkAvailable()) {
                startDownloading();
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void reCreatedb() {
        db.execSQL("DROP TABLE IF EXISTS " + TouristListContract.TouristListEntry.TABLE_NAME);
        db.execSQL("CREATE TABLE " + TouristListContract.TouristListEntry.TABLE_NAME + " (" +
                TouristListContract.TouristListEntry._ID + " INTEGER PRIMARY KEY," +
                TouristListContract.TouristListEntry.COLUMN_POSITION + " NUMBER," +
                TouristListContract.TouristListEntry.COLUMN_AUDIO + " TEXT," +
                TouristListContract.TouristListEntry.COLUMN_NAME + " TEXT," +
                TouristListContract.TouristListEntry.COLUMN_AUDIO_URI + " TEXT," +
                TouristListContract.TouristListEntry.COLUMN_PICTURE + " TEXT," +
                TouristListContract.TouristListEntry.COLUMN_PICTURE_URI + " TEXT," +
                TouristListContract.TouristListEntry.COLUMN_VIDEO + " TEXT," +
                TouristListContract.TouristListEntry.COLUMN_VIDEO_URI + " TEXT," +
                TouristListContract.TouristListEntry.COLUMN_IS_ACTIVE + " BOOLEAN," +
                TouristListContract.TouristListEntry.COLUMN_CAN_TAKE_PHOTO + " BOOLEAN," +
                TouristListContract.TouristListEntry.COLUMN_TYPE_ID + " NUMBER);");
        finish();
    }


}
