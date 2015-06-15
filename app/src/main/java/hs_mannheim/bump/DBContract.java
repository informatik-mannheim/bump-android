package hs_mannheim.bump;

import android.provider.BaseColumns;

/**
 * Created by benjamin on 14/05/15.
 */
public class DBContract {

    public static final class Data implements BaseColumns {

        public static final String TABLE_NAME = "data";

        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_TYPE = "experimentType";
        public static final String COLUMN_THRESHOLD = "bumpForce";
        public static final String COLUMN_NAME = "name";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( "
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, " + COLUMN_TYPE + " TEXT NOT NULL, "
                + COLUMN_THRESHOLD + " TEXT NOT NULL," + COLUMN_NAME + " TEXT NOT NULL" + ")";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class Samples implements  BaseColumns {

        public static final String TABLE_NAME = "samples";

        public static final String COLUMN_DATAID = "dataID";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_X = "x";
        public static final String COLUMN_Y = "y";
        public static final String COLUMN_Z = "z";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( "
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TIMESTAMP + " LONG NOT NULL, "
                + COLUMN_X + " DOUBLE NOT NULL, " + COLUMN_Y + " DOUBLE NOT NULL, "
                + COLUMN_Z + " DOUBLE NOT NULL, " + COLUMN_DATAID + " INTEGER NOT NULL, " + "FOREIGN KEY" + "(" + COLUMN_DATAID + ") " + "REFERENCES " + Data.TABLE_NAME + "(" + Data._ID + ")" + ")";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    }
}
