package com.maciek.v2.DB;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.maciek.v2.Utilities.VolleyGetRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Geezy on 15.07.2018.
 */

public class TuristListDbQuery {

    private SQLiteDatabase mDb;

    public TuristListDbQuery(SQLiteDatabase db) {
        mDb = db;
    }

    public Cursor getQueriedTouristList(String type_id) {
        String[] ary = new String[]{TouristListContract.TouristListEntry.COLUMN_POSITION, TouristListContract.TouristListEntry.COLUMN_NAME, TouristListContract.TouristListEntry.COLUMN_AUDIO_URI, TouristListContract.TouristListEntry.COLUMN_AUDIO};
        String selection = TouristListContract.TouristListEntry.COLUMN_TYPE_ID + " = ? and " + TouristListContract.TouristListEntry.COLUMN_IS_ACTIVE + " = ?";
        String[] selectionArgs = {type_id, "1"};
        return mDb.query(TouristListContract.TouristListEntry.TABLE_NAME,
                ary,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }

    public List<String> getActiveAudio() {
        String[] ary = new String[]{TouristListContract.TouristListEntry.COLUMN_AUDIO};
        String selection = TouristListContract.TouristListEntry.COLUMN_IS_ACTIVE + " = ?";
        String[] selectionArgs = {"1"};
        Cursor c = mDb.query(TouristListContract.TouristListEntry.TABLE_NAME,
                ary,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int audioNameIndex = c.getColumnIndex(TouristListContract.TouristListEntry.COLUMN_AUDIO);
        List<String> activeAudio = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if (c.getString(audioNameIndex) != null || !c.getString(audioNameIndex).equals("null")) {
                activeAudio.add(c.getString(audioNameIndex));
            }
        }
        c.close();
        return activeAudio;
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

    public void disableAudio(List<String> audioList) {
        String audioJoined = VolleyGetRequest.prepareInClause(audioList);
        String sql = String.format("update %1$s set %2$s = %3$s where %4$s in (%5$s)",
                TouristListContract.TouristListEntry.TABLE_NAME,            //1
                TouristListContract.TouristListEntry.COLUMN_IS_ACTIVE,      //2
                0,                                                          //3
                TouristListContract.TouristListEntry.COLUMN_AUDIO,          //4
                audioJoined                                                 //5

        );
        mDb.execSQL(sql);
    }

    public void enableAudio(List<String> audioList) {
        String audioJoined = VolleyGetRequest.prepareInClause(audioList);
        String sql = String.format("update %1$s set %2$s = %3$s where %4$s in (%5$s)",
                TouristListContract.TouristListEntry.TABLE_NAME,            //1
                TouristListContract.TouristListEntry.COLUMN_IS_ACTIVE,      //2
                1,                                                          //3
                TouristListContract.TouristListEntry.COLUMN_AUDIO,          //4
                audioJoined                                                 //5

        );
        mDb.execSQL(sql);
    }

    public void updatePosition(int position, String audio) {
        String sql = String.format("update %1$s set %2$s = %3$s where %4$s = '%5$s'",
                TouristListContract.TouristListEntry.TABLE_NAME,            //1
                TouristListContract.TouristListEntry.COLUMN_POSITION,       //2
                position,                                                   //3
                TouristListContract.TouristListEntry.COLUMN_AUDIO,          //4
                audio                                                       //5

        );
        mDb.execSQL(sql);
    }


    public Cursor getAudioCursor(String type_id) {
        String[] ary = new String[]{TouristListContract.TouristListEntry.COLUMN_AUDIO};
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

    public Cursor getAudioByAudioUri(String audioUri) {
        String[] ary = new String[]{TouristListContract.TouristListEntry.COLUMN_AUDIO};
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

    public Cursor getPictureCursor(String type_id) {
        String[] ary = new String[]{TouristListContract.TouristListEntry.COLUMN_PICTURE};
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

    public Cursor getVideoCursor(String type_id) {
        String[] ary = new String[]{TouristListContract.TouristListEntry.COLUMN_VIDEO};
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

    public Cursor getAudioTitle(String audioUri) {
        String[] ary = new String[]{TouristListContract.TouristListEntry.COLUMN_NAME,};
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

    public Cursor getAudioUriByTypeId(String typeId) {
        String[] ary = new String[]{TouristListContract.TouristListEntry.COLUMN_AUDIO_URI};
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

    public Cursor getPosition(String title) {
        String[] ary = new String[]{TouristListContract.TouristListEntry.COLUMN_POSITION, TouristListContract.TouristListEntry.COLUMN_NAME, TouristListContract.TouristListEntry.COLUMN_AUDIO_URI, TouristListContract.TouristListEntry.COLUMN_AUDIO};
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

    public Cursor getPostionByAudioUri(String audioUri) {
        String[] ary = new String[]{TouristListContract.TouristListEntry.COLUMN_POSITION};
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

    public Cursor getPictureUriByAudioUri(String audio) {
        String[] ary = new String[]{TouristListContract.TouristListEntry.COLUMN_PICTURE_URI};
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

    public Cursor getVideoUriByAudioUri(String audio) {
        String[] ary = new String[]{TouristListContract.TouristListEntry.COLUMN_VIDEO_URI};
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


    public Cursor getAudioUriImageUriVideoUriPosByTypeId(String typeId) {
        String[] ary = new String[]
                {TouristListContract.TouristListEntry.COLUMN_AUDIO_URI,
                        TouristListContract.TouristListEntry.COLUMN_PICTURE_URI,
                        TouristListContract.TouristListEntry.COLUMN_VIDEO_URI,
                        TouristListContract.TouristListEntry.COLUMN_POSITION,
                        TouristListContract.TouristListEntry.COLUMN_NAME};
        String selection = TouristListContract.TouristListEntry.COLUMN_TYPE_ID + " = ? and " + TouristListContract.TouristListEntry.COLUMN_IS_ACTIVE + " = ? ";
        String[] selectionArgs = {typeId, "1"};
        return mDb.query(TouristListContract.TouristListEntry.TABLE_NAME,
                ary,
                selection,
                selectionArgs,
                null,
                null,
                TouristListContract.TouristListEntry.COLUMN_POSITION);
    }


}
