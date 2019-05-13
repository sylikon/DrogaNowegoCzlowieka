package com.maciek.droganowegoczlowieka.DB;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Geezy on 15.07.2018.
 */

public class TuristListDbQuery {

    private SQLiteDatabase mDb;
    public TuristListDbQuery(SQLiteDatabase db){
        mDb = db;
    }
    public Cursor getQueriedTouristList(String type_id){
        String[] ary = new String[] {TouristListContract.TouristListEntry.COLUMN_POSITION, TouristListContract.TouristListEntry.COLUMN_NAME, TouristListContract.TouristListEntry.COLUMN_AUDIO_URI, TouristListContract.TouristListEntry.COLUMN_AUDIO};
        String selection = TouristListContract.TouristListEntry.COLUMN_TYPE_ID + " = ?";
        String[] selectionArgs = {type_id};
        return mDb.query(TouristListContract.TouristListEntry.TABLE_NAME,
                ary,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }

    public void updatePosition(Map<Integer, String> map, String typeId) {
        int type = Integer.valueOf(typeId);
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            String sql = String.format("update %1$s set %2$s = %3$s where %4$s = '%5$s' and %6$s = %7$s",
                    TouristListContract.TouristListEntry.TABLE_NAME,            //1
                    TouristListContract.TouristListEntry.COLUMN_POSITION,       //2
                    entry.getKey(),                                             //3
                    TouristListContract.TouristListEntry.COLUMN_NAME,           //4
                    entry.getValue(),                                           //5
                    TouristListContract.TouristListEntry.COLUMN_TYPE_ID,        //6
                    type                                                        //7
            );
            mDb.execSQL(sql);
        }

    }


    public Cursor getAudioCursor(String type_id){
        String[] ary = new String[] {TouristListContract.TouristListEntry.COLUMN_AUDIO};
        String selection = TouristListContract.TouristListEntry.COLUMN_TYPE_ID + " = ?";
        String[] selectionArgs = {type_id};
        return mDb.query(TouristListContract.TouristListEntry.TABLE_NAME,
                ary,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }

    public Cursor getAudioByAudioUri(String audioUri){
        String[] ary = new String[] {TouristListContract.TouristListEntry.COLUMN_AUDIO};
        String selection = TouristListContract.TouristListEntry.COLUMN_AUDIO_URI + " = ?";
        String[] selectionArgs = {audioUri};
        return mDb.query(TouristListContract.TouristListEntry.TABLE_NAME,
                ary,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }

    public Cursor getPictureCursor(String type_id){
        String[] ary = new String[] {TouristListContract.TouristListEntry.COLUMN_PICTURE};
        String selection = TouristListContract.TouristListEntry.COLUMN_TYPE_ID + " = ?";
        String[] selectionArgs = {type_id};
        return mDb.query(TouristListContract.TouristListEntry.TABLE_NAME,
                ary,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }

    public Cursor getVideoCursor(String type_id){
        String[] ary = new String[] {TouristListContract.TouristListEntry.COLUMN_VIDEO};
        String selection = TouristListContract.TouristListEntry.COLUMN_TYPE_ID + " = ?";
        String[] selectionArgs = {type_id};
        return mDb.query(TouristListContract.TouristListEntry.TABLE_NAME,
                ary,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }

    public Cursor getAudioTitle(String audioUri){
        String[] ary = new String[] {TouristListContract.TouristListEntry.COLUMN_NAME,};
        String selection = TouristListContract.TouristListEntry.COLUMN_AUDIO_URI + " = ?";
        String[] selectionArgs = {audioUri};
        return mDb.query(TouristListContract.TouristListEntry.TABLE_NAME,
                ary,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }

    public Cursor getAudioUriByTypeId(String typeId){
        String[] ary = new String[] {TouristListContract.TouristListEntry.COLUMN_AUDIO_URI};
        String selection = TouristListContract.TouristListEntry.COLUMN_TYPE_ID + " = ?";
        String[] selectionArgs = {typeId};
        return mDb.query(TouristListContract.TouristListEntry.TABLE_NAME,
                ary,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }

    public Cursor getPosition(String title){
        String[] ary = new String[] {TouristListContract.TouristListEntry.COLUMN_POSITION, TouristListContract.TouristListEntry.COLUMN_NAME, TouristListContract.TouristListEntry.COLUMN_AUDIO_URI, TouristListContract.TouristListEntry.COLUMN_AUDIO};
        String selection = TouristListContract.TouristListEntry.COLUMN_AUDIO + " = ?";
        String[] selectionArgs = {title};
        return mDb.query(TouristListContract.TouristListEntry.TABLE_NAME,
                ary,
                selection,
                selectionArgs,
                null,
                null,
                null);

    }

    public Cursor getPostionByAudioUri(String audioUri){
        String[] ary = new String[] {TouristListContract.TouristListEntry.COLUMN_POSITION};
        String selection = TouristListContract.TouristListEntry.COLUMN_AUDIO_URI + " = ?";
        String[] selectionArgs = {audioUri};
        return mDb.query(TouristListContract.TouristListEntry.TABLE_NAME,
                ary,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }
    public Cursor getPictureUriByAudioUri(String audio){
        String[] ary = new String[] {TouristListContract.TouristListEntry.COLUMN_PICTURE_URI};
        String selection = TouristListContract.TouristListEntry.COLUMN_AUDIO_URI + " = ?";
        String[] selectionArgs = {audio};
        return mDb.query(TouristListContract.TouristListEntry.TABLE_NAME,
                ary,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }
    public Cursor getVideoUriByAudioUri(String audio){
        String[] ary = new String[] {TouristListContract.TouristListEntry.COLUMN_VIDEO_URI};
        String selection = TouristListContract.TouristListEntry.COLUMN_AUDIO_URI + " = ?";
        String[] selectionArgs = {audio};
        return mDb.query(TouristListContract.TouristListEntry.TABLE_NAME,
                ary,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }



    public Cursor getAudioUriImageUriVideoUriPosByTypeId(String typeId){
        String[] ary = new String[]
                        {TouristListContract.TouristListEntry.COLUMN_AUDIO_URI,
                        TouristListContract.TouristListEntry.COLUMN_PICTURE_URI,
                        TouristListContract.TouristListEntry.COLUMN_VIDEO_URI,
                        TouristListContract.TouristListEntry.COLUMN_POSITION,
                        TouristListContract.TouristListEntry.COLUMN_NAME};
        String selection = TouristListContract.TouristListEntry.COLUMN_TYPE_ID + " = ?";
        String[] selectionArgs = {typeId};
        return mDb.query(TouristListContract.TouristListEntry.TABLE_NAME,
                ary,
                selection,
                selectionArgs,
                null,
                null,
                TouristListContract.TouristListEntry.COLUMN_POSITION);
    }


}
