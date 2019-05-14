package com.maciek.v2.Utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Geezy on 18.07.2018.
 */

public class DownloadService extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "urlpath";
    public static final String DIRECTORY = "directory";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "com.vogella.android.service.receiver";
    public static final String COUNTER = "counter";
    public static final String TYPE_ID = "type_id";
    public static final String COUNTERMAX = "counterMax";
    public DownloadService() {
        super("DownloadService");
    }

    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(Intent intent) {
        String urlPath = intent.getStringExtra(URL);
        String fileName = intent.getStringExtra(FILENAME);
        String directory = intent.getStringExtra(DIRECTORY);
        String type_id = intent.getStringExtra(TYPE_ID);
        File output;
        int counter = intent.getIntExtra(COUNTER, 0);
        int counterMax = intent.getIntExtra(COUNTERMAX, 0);
        counter++;


        if(directory.equals("audio")){
            output = new File(getFilesDir(),
                    fileName);
        }else if(directory.equals("picture")){
            output = new File(getFilesDir(),
                    fileName);
        }else {
            output = new File(getFilesDir(),
                    fileName);
        }

        if (output.exists()) {
            result =-1;
            publishResults(output.getAbsolutePath(), result, fileName, counter, directory, type_id, counterMax);
        }else{
                try {
                    URL u = new URL(urlPath);
                    HttpURLConnection c = (HttpURLConnection) u.openConnection();
                    c.setRequestMethod("GET");
                    c.setDoOutput(true);
                    c.connect();
                    FileOutputStream f = new FileOutputStream(output);


                    InputStream in = c.getInputStream();

                    byte[] buffer = new byte[1024];
                    int len1 = 0;
                    while ((len1 = in.read(buffer)) > 0) {
                        f.write(buffer, 0, len1);
                    }
                    f.close();
                    result = -1;
                } catch (Exception e) {

                }

                publishResults(output.getAbsolutePath(), result, fileName, counter, directory, type_id, counterMax);
            }
        }


    private void publishResults(String outputPath, int result, String fileName, int counter,String directory, String type_id, int counterMax) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(FILEPATH, outputPath);
        intent.putExtra(FILENAME, fileName);
        intent.putExtra(RESULT, result);
        intent.putExtra(COUNTER, counter );
        intent.putExtra(DIRECTORY, directory);
        intent.putExtra(TYPE_ID, type_id);
        intent.putExtra(COUNTERMAX, counterMax);
        sendBroadcast(intent);
    }
}
