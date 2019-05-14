package com.maciek.v2.DB;

import android.provider.BaseColumns;

/**
 * Created by Geezy on 15.07.2018.
 */

public class TouristListContract {


    public static  class TouristListEntry implements BaseColumns {
        public static final String TABLE_NAME = "TOURIST_LIST";
        public static final String COLUMN_POSITION = "POSITION";
        public static final String COLUMN_AUDIO = "AUDIO";
        public static final String COLUMN_AUDIO_URI = "LOCAL_URI";
        public static final String COLUMN_NAME = "NAME";
        public static final String COLUMN_TYPE_ID = "TYPE_ID";
        public static final String COLUMN_PICTURE = "PICTURE";
        public static final String COLUMN_PICTURE_URI = "PICTURE_URI";
        public static final String COLUMN_VIDEO = "VIDEO";
        public static final String COLUMN_VIDEO_URI = "VIDEO_URI";
        public static final String COLUMN_IS_ACTIVE = "IS_ACTIVE";


    }

}
