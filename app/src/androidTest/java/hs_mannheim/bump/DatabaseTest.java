package hs_mannheim.bump;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by benjamin on 14/05/15.
 */
public class DatabaseTest extends AndroidTestCase {

    public void testDropDB() {
        assertTrue("Drop Database failed", mContext.deleteDatabase(DBHelper.DATABASE_NAME));
    }

    public void testCreateDB() {
        DBHelper dbHelper = new DBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue(db.isOpen());
        db.close();
    }

    public void testAddData() {
        DBHelper dbHelper = new DBHelper(mContext);

        ExperimentData experimentData = new ExperimentData();
        experimentData.setSamples(new ArrayList<Sample>(Arrays.asList(new Sample(1, 1, 1))));
        experimentData.setThreshold(Threshold.LOW);
        experimentData.setName("name");
        experimentData.setExperimentType(ExperimentType.FIRST);
        dbHelper.addData(experimentData);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DBContract.Data.TABLE_NAME, null);
        cursor.moveToFirst();
        assertEquals(cursor.getInt(0), 1);
        db.execSQL("DELETE FROM " + DBContract.Data.TABLE_NAME);
    }

    public void testGetAllData() {
        DBHelper dbHelper = new DBHelper(mContext);

        ExperimentData experimentData1 = new ExperimentData();
        experimentData1.setSamples(new ArrayList<Sample>(Arrays.asList(new Sample(1, 1, 1))));
        experimentData1.setThreshold(Threshold.HIGH);
        experimentData1.setExperimentType(ExperimentType.FIRST);
        experimentData1.setName("name1");

        ExperimentData experimentData2 = new ExperimentData();
        experimentData2.setSamples(new ArrayList<Sample>(Arrays.asList(new Sample(0, 0, 0))));
        experimentData2.setThreshold(Threshold.MEDIUM);
        experimentData2.setExperimentType(ExperimentType.SECOND);
        experimentData2.setName("name2");

        dbHelper.addData(experimentData1);
        dbHelper.addData(experimentData2);

        List<ExperimentData> allExperimentData;
        allExperimentData = dbHelper.getAllData();
        assertEquals(experimentData1, allExperimentData.get(0));
        assertEquals(experimentData2, allExperimentData.get(1));
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + DBContract.Data.TABLE_NAME);
    }

}
