package com.maciek.v2.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maciek.v2.DB.InsertPositionToList;
import com.maciek.v2.DB.TouristListContract;
import com.maciek.v2.DB.TuristListDbHelper;
import com.maciek.v2.DB.TuristListDbQuery;
import com.maciek.v2.R;
import com.maciek.v2.Utilities.DownloadService;

public class DownloaderActivity extends AppCompatActivity implements Response.Listener<byte[]>, Response.ErrorListener, View.OnClickListener {

    private ProgressBar progressBar;
    private SQLiteDatabase db;
    private Button acceptButton, rejectButton, goToMainButton;
    private TextView textView;
    private TuristListDbHelper turistListDbHelper;
    private SharedPreferences.Editor editor;
    private TuristListDbQuery turistListDbQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloader);
        turistListDbHelper = TuristListDbHelper.getInstance(this);
        db = turistListDbHelper.getReadableDatabase();
        textView = findViewById(R.id.downloader_textView);
        progressBar = findViewById(R.id.progress_bar_downloader);
        acceptButton = findViewById(R.id.accept_download_button);
        rejectButton = findViewById(R.id.reject_download_button);
        goToMainButton = findViewById(R.id.go_to_main_activity);
        acceptButton.setOnClickListener(this);
        rejectButton.setOnClickListener(this);
        goToMainButton.setOnClickListener(this);
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.was_download_succesfull), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        db = turistListDbHelper.getReadableDatabase();
        turistListDbQuery = new TuristListDbQuery(db);
    }


    @Override
    protected void onResume() {
        super.onResume();
        boolean shouldDownload = true;
        if (isNetworkAvailable()) {
            Intent intent = getIntent();
            shouldDownload = intent.getBooleanExtra("startUpdate", false);
        } else {
            textView.setText(getString(R.string.no_internet_connection_downloader));
        }

        if (shouldDownload) {
            registerReceiver(receiver, new IntentFilter(
                    DownloadService.NOTIFICATION));
            downloadConent("1");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int resultCode = bundle.getInt(DownloadService.RESULT);
                if (resultCode == RESULT_OK) {
                    progressBar.setProgress(bundle.getInt(DownloadService.COUNTER));
                    String message = bundle.getInt(DownloadService.COUNTER) + "/" + bundle.getInt(DownloadService.COUNTERMAX);
                    if (bundle.getString(DownloadService.DIRECTORY).equals("audio")) {
                        InsertPositionToList.insertAudioUri(db, bundle.getString(DownloadService.FILEPATH), bundle.getString(DownloadService.FILENAME), bundle.getString(DownloadService.TYPE_ID));
                    } else if (bundle.getString(DownloadService.DIRECTORY).equals("picture")) {
                        InsertPositionToList.insertPictureUri(db, bundle.getString(DownloadService.FILEPATH), bundle.getString(DownloadService.FILENAME), bundle.getString(DownloadService.TYPE_ID));
                    } else {
                        InsertPositionToList.insertVideoUri(db, bundle.getString(DownloadService.FILEPATH), bundle.getString(DownloadService.FILENAME), bundle.getString(DownloadService.TYPE_ID));
                    }
                    if ((bundle.getInt(DownloadService.COUNTER) == bundle.getInt(DownloadService.COUNTERMAX))) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Integer test = Integer.parseInt(bundle.getString(DownloadService.TYPE_ID));
                        test++;
                        if (test <= 4)
                            downloadConent(test.toString());
                        else {
                            textView.setText(getString(R.string.succesfull_download_downloader));
                            goToMainButton.setVisibility(View.VISIBLE);
                            unregisterReceiver(receiver);
                            editor.putInt(getString(R.string.was_download_succesfull), 4);
                            editor.commit();
                        }
                    }

                } else {
                    unregisterReceiver(receiver);
                    reCreateTable();
                    editor.putInt(getString(R.string.was_download_succesfull), 0);
                    editor.commit();
                    textView.setText(getString(R.string.download_failed_downloader));
                    progressBar.setVisibility(View.GONE);

                }
            }
        }
    };


    private void downloadConent(String typeId) {
        Cursor cursor;
        int progressStatus = 0;

        cursor = turistListDbQuery.getAudioCursor(typeId);
        switch (typeId) {
            case "1":
                textView.setText(getString(R.string.download_tourist_path));
                break;
            case "2":
                textView.setText(getString(R.string.download_oaza_path));
                break;
            case "3":
                textView.setText(getString(R.string.download_house_path));
                break;
            case "4":
                textView.setText(getString(R.string.download_advanced_path));
                break;
        }

        progressBar.setMax(cursor.getCount() * 3);
        progressBar.setVisibility(View.VISIBLE);
        if (cursor.moveToFirst()) {
            do {
                String data = cursor.getString(cursor.getColumnIndex("AUDIO"));
                String mUrl = "http://185.243.55.31/audio/" + data;
                Intent intent = new Intent(this, DownloadService.class);
                // add infos for the service which file to download and where to store
                intent.putExtra(DownloadService.FILENAME, data);
                intent.putExtra(DownloadService.URL,
                        mUrl);
                intent.putExtra(DownloadService.COUNTERMAX, cursor.getCount() * 3);
                intent.putExtra(DownloadService.DIRECTORY, "audio");
                intent.putExtra(DownloadService.TYPE_ID, typeId);
                intent.putExtra(DownloadService.COUNTER, progressStatus++);
                startService(intent);
            } while (cursor.moveToNext());
        }
        cursor = turistListDbQuery.getPictureCursor(typeId);
        if (cursor.moveToFirst()) {
            do {

                String data = cursor.getString(cursor.getColumnIndex("PICTURE"));
                String mUrl = "http://185.243.55.31/foto/" + data;
                Intent intent = new Intent(this, DownloadService.class);
                // add infos for the service which file to download and where to store
                intent.putExtra(DownloadService.FILENAME, data);
                intent.putExtra(DownloadService.URL,
                        mUrl);
                intent.putExtra(DownloadService.COUNTERMAX, cursor.getCount() * 3);
                intent.putExtra(DownloadService.DIRECTORY, "picture");
                intent.putExtra(DownloadService.TYPE_ID, typeId);
                intent.putExtra(DownloadService.COUNTER, progressStatus++);
                startService(intent);
            } while (cursor.moveToNext());
        }
        cursor = turistListDbQuery.getVideoCursor(typeId);
        if (cursor.moveToFirst()) {
            do {
                String data = cursor.getString(cursor.getColumnIndex("VIDEO"));
                if (data == null) {
                    data = "null";
                }
                String mUrl = "http://185.243.55.31/video/" + data;
                Intent intent = new Intent(this, DownloadService.class);
                // add infos for the service which file to download and where to store
                intent.putExtra(DownloadService.FILENAME, data);
                intent.putExtra(DownloadService.URL,
                        mUrl);
                intent.putExtra(DownloadService.COUNTERMAX, cursor.getCount() * 3);
                intent.putExtra(DownloadService.DIRECTORY, "video");
                intent.putExtra(DownloadService.COUNTER, progressStatus++);
                intent.putExtra(DownloadService.TYPE_ID, typeId);
                startService(intent);
            } while (cursor.moveToNext());
        }
        cursor.close();

    }


    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(byte[] response) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.accept_download_button:
                if (!isNetworkAvailable()) {
                    Toast.makeText(this, getString(R.string.toast_turn_internet_on_downloader), Toast.LENGTH_SHORT).show();
                    break;
                }
                registerReceiver(receiver, new IntentFilter(
                        DownloadService.NOTIFICATION));
                downloadConent("1");
                acceptButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);

                break;
            case R.id.reject_download_button:
                reCreateTable();
                finish();
                moveTaskToBack(true);
                break;
            case R.id.go_to_main_activity:
                startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void reCreateTable() {
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
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, getString(R.string.back_button_toast_on_downloader), Toast.LENGTH_SHORT).show();
    }
}
