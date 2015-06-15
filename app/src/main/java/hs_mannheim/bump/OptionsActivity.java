package hs_mannheim.bump;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import hs_mannheim.bump.R;

/**
 * Created by benjamin on 11/06/15.
 */
public class OptionsActivity extends ActionBarActivity {

    public static String name;
    private DBHelper dbHelper;
    private DatabaseExporter dbExporter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        dbHelper = new DBHelper(getApplicationContext());
        dbExporter = new DatabaseExporter(this);

    }

    public void exportDatabase(View view) {
        dbExporter.export();
    }

    public void wipeDatabse(View view) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(DBContract.Data.DELETE_TABLE);
        db.execSQL(DBContract.Samples.DELETE_TABLE);
        db.execSQL(DBContract.Data.CREATE_TABLE);
        db.execSQL(DBContract.Samples.CREATE_TABLE);
    }
}
