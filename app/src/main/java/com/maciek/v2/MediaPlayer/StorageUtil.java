package com.maciek.v2.MediaPlayer;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by Geezy on 25.07.2018.
 */

public class StorageUtil {
    private final String STORAGE = " com.valdioveliu.valdio.audioplayer.STORAGE";
    private SharedPreferences preferences;
    private Context context;

    public StorageUtil(Context context) {
        this.context = context;
    }

    public void storeHashMap(HashMap<Integer, String> hashMap) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(hashMap);
        editor.putString("hashMap", json);
        editor.apply();
    }

    public HashMap<Integer, String> loadHashMap() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("hashMap", null);
        Type type = new TypeToken<HashMap<Integer, String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void storeHashMapIndex(int index) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("audioIndex", index);
        editor.apply();
    }

    public int loadAudioIndex() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        return preferences.getInt("audioIndex", -1);//return -1 if no data found
    }

    public void clearCachedAudioPlaylist() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
