package com.maciek.v2.DB;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Geezy on 15.07.2018.
 */

public class InsertPositionToList {

    public static void insertAudiJpgDataByPos(SQLiteDatabase db, String title, int type_id, int position, String name, String jpgName, boolean isActive, boolean takePicture) {
        if (db == null) {
            return;
        }

        ContentValues cv = new ContentValues();
        cv.put(TouristListContract.TouristListEntry.COLUMN_AUDIO, title);
        cv.put(TouristListContract.TouristListEntry.COLUMN_POSITION, position);
        cv.put(TouristListContract.TouristListEntry.COLUMN_TYPE_ID, type_id);
        cv.put(TouristListContract.TouristListEntry.COLUMN_NAME, name);
        cv.put(TouristListContract.TouristListEntry.COLUMN_PICTURE, jpgName);
        cv.put(TouristListContract.TouristListEntry.COLUMN_IS_ACTIVE, isActive);
        cv.put(TouristListContract.TouristListEntry.COLUMN_CAN_TAKE_PHOTO, takePicture);


        //insert all guests in one transaction
        try {
            db.beginTransaction();
            //clear the table first
            db.insert(TouristListContract.TouristListEntry.TABLE_NAME, null ,cv);

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            //too bad :(
        } finally {
            db.endTransaction();
        }

    }

    public static void insertAudioUri(SQLiteDatabase db, String uri, String audio, String type_id) {
        if (db == null) {
            return;
        }

        ContentValues cv = new ContentValues();

        cv.put(TouristListContract.TouristListEntry.COLUMN_AUDIO_URI, uri);

        try {
            db.beginTransaction();
            //clear the table first
            db.update(TouristListContract.TouristListEntry.TABLE_NAME, cv, "AUDIO=? AND TYPE_ID =?", new String[] {audio,type_id} );
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            //too bad :(
        } finally {
            db.endTransaction();
        }

    }

    public static void insertVideo(SQLiteDatabase db, String video, String audio) {
        if (db == null) {
            return;
        }

        ContentValues cv = new ContentValues();

        cv.put(TouristListContract.TouristListEntry.COLUMN_VIDEO, video);

        try {
            db.beginTransaction();
            //clear the table first
            db.update(TouristListContract.TouristListEntry.TABLE_NAME, cv, "AUDIO=?", new String[] {audio} );
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            //too bad :(
        } finally {
            db.endTransaction();
        }

    }


    public static void insertPictureUri(SQLiteDatabase db, String uri, String picture, String type_id) {
        if (db == null) {
            return;
        }

        ContentValues cv = new ContentValues();

        cv.put(TouristListContract.TouristListEntry.COLUMN_PICTURE_URI, uri);

        try {
            db.beginTransaction();
            //clear the table first
            db.update(TouristListContract.TouristListEntry.TABLE_NAME, cv, "PICTURE=? AND TYPE_ID =?", new String[] {picture, type_id} );
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            //too bad :(
        } finally {
            db.endTransaction();
        }

    }

    public static void insertVideoUri(SQLiteDatabase db, String uri, String video, String type_id) {
        if (db == null) {
            return;
        }

        ContentValues cv = new ContentValues();

        cv.put(TouristListContract.TouristListEntry.COLUMN_VIDEO_URI, uri);

        try {
            db.beginTransaction();
            //clear the table first
            db.update(TouristListContract.TouristListEntry.TABLE_NAME, cv, "VIDEO=? AND TYPE_ID =?", new String[] {video, type_id} );
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            //too bad :(
        } finally {
            db.endTransaction();
        }

    }
}
