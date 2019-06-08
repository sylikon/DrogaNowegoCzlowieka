package com.maciek.v2.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Geezy on 15.07.2018.
 */

public class TuristListDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 83;
    public static final String DATABASE_NAME = "TouristSet.db";
    private static TuristListDbHelper mInstance = null;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TouristListContract.TouristListEntry.TABLE_NAME + " (" +
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
                    TouristListContract.TouristListEntry.COLUMN_CAN_TAKE_PHOTO + " BOOLEAN," +
                    TouristListContract.TouristListEntry.COLUMN_TYPE_ID + " NUMBER);";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TouristListContract.TouristListEntry.TABLE_NAME;


    public static TuristListDbHelper getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new TuristListDbHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }


    private TuristListDbHelper(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }


    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }


}
