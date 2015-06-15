package hs_mannheim.bump;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import hs_mannheim.bump.Threshold;
import hs_mannheim.bump.DBContract.*;
import hs_mannheim.bump.Sample;

/**
 * Created by benjamin on 14/05/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "experimentData.db";
    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Data.CREATE_TABLE);
        db.execSQL(Samples.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL(Data.DELETE_TABLE);
        db.execSQL(Samples.DELETE_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void addData(ExperimentData data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues dataValues = new ContentValues();
        dataValues.put(Data.COLUMN_TYPE, data.getExperimentType().name());
        dataValues.put(Data.COLUMN_THRESHOLD, data.getThreshold().name());
        dataValues.put(Data.COLUMN_NAME, data.getName());

        long sample_id = db.insert(Data.TABLE_NAME, null, dataValues);

        for (Sample sample: data.getSamples()) {
            ContentValues samplesValues = new ContentValues();
            samplesValues.put(Samples.COLUMN_DATAID, sample_id);
            samplesValues.put(Samples.COLUMN_TIMESTAMP, sample.timestamp);
            samplesValues.put(Samples.COLUMN_X, sample.x);
            samplesValues.put(Samples.COLUMN_Y, sample.y);
            samplesValues.put(Samples.COLUMN_Z, sample.z);
            db.insert(Samples.TABLE_NAME, null, samplesValues);
        }

        db.close();
    }

    public List<ExperimentData> getAllData() {
        List<ExperimentData> allData = new ArrayList<>();

        String query = "SELECT * FROM " + Data.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                ExperimentData data = new ExperimentData();
                data.setID(cursor.getInt(cursor.getColumnIndex(Data._ID)));
                data.setTimestamp(java.sql.Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(Data.COLUMN_TIMESTAMP))));
                data.setExperimentType(ExperimentType.valueOf(cursor.getString(cursor.getColumnIndex(Data.COLUMN_TYPE))));
                data.setThreshold(Threshold.valueOf(cursor.getString(cursor.getColumnIndex(Data.COLUMN_THRESHOLD))));
                data.setName(cursor.getString(cursor.getColumnIndex(Data.COLUMN_NAME)));
                allData.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();

        for (int i = 0; i < allData.size(); i++) {
            String sampleQuery = "SELECT * FROM " + Samples.TABLE_NAME + " WHERE " + Samples.COLUMN_DATAID + " = " + allData.get(i).getID();
            Cursor sampleCursor = db.rawQuery(sampleQuery, null);
            List<Sample> samples = new ArrayList<>();
            if (sampleCursor.moveToFirst()) {
                do {
                    Sample sample = new Sample();
                    sample.timestamp = sampleCursor.getLong(sampleCursor.getColumnIndex(Samples.COLUMN_TIMESTAMP));
                    sample.x = sampleCursor.getDouble(sampleCursor.getColumnIndex(Samples.COLUMN_X));
                    sample.y = sampleCursor.getDouble(sampleCursor.getColumnIndex(Samples.COLUMN_Y));
                    sample.z = sampleCursor.getDouble(sampleCursor.getColumnIndex(Samples.COLUMN_Z));
                    samples.add(sample);
                } while (sampleCursor.moveToNext());
            }
            sampleCursor.close();
            allData.get(i).setSamples(samples);
        }
        db.close();
        return allData;
    }
}
