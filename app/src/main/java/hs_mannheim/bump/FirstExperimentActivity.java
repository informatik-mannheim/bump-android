package hs_mannheim.bump;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;

import hs_mannheim.bump.BumpDetector;
import hs_mannheim.bump.BumpEventListener;
import hs_mannheim.bump.IBumpDetector;
import hs_mannheim.bump.R;
import hs_mannheim.bump.Sample;
import hs_mannheim.bump.ServerEventListener;
import hs_mannheim.bump.ServerManager;
import hs_mannheim.bump.SntpClient;
import hs_mannheim.bump.Threshold;

/**
 * Created by benjamin on 02/06/15.
 */

public class FirstExperimentActivity extends ActionBarActivity implements ServerEventListener, BumpEventListener {

    private SensorManager mSensorManager;
    private IBumpDetector mBumpDetector;
    private ServerManager mServerManager;
    private int mColor = Color.WHITE;
    private LinearLayout bgElement;
    private DBHelper dbHelper;
    private Boolean dataDelivered = false;
    private Boolean dataReceived = false;
    private Boolean record = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstexperiment);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mBumpDetector = new BumpDetector(mSensorManager, Threshold.LOW);
        mBumpDetector.registerListener(this);
        mBumpDetector.startMonitoring();

//        mServerManager = new ServerManager("http://141.19.142.50:3000/", this);
        mServerManager = new ServerManager("http://192.168.1.78:3000", this);
        mServerManager.connect();
        mServerManager.registerListener(this);

        dbHelper = new DBHelper(getApplicationContext());

        bgElement = (LinearLayout) findViewById(R.id.container);
        // Set custom view on action bar.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.name_field);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBumpDetector.stopMonitoring();
        mServerManager.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_firstexperiment, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Integer getBackgroundColor() {
        int color = Color.TRANSPARENT;
        Drawable background = bgElement.getBackground();
        if (background instanceof ColorDrawable) {
            color = ((ColorDrawable) background).getColor();
        }
        return color;
    }

    private void switchBackgroundColor() {
        if (getBackgroundColor() == Color.WHITE) {
            mColor = Color.BLUE;
            bgElement.setBackgroundColor(mColor);
        } else {
            mColor = Color.WHITE;
            bgElement.setBackgroundColor(mColor);
        }
    }

    public void switchBackgroundColor(View view) {
        switchBackgroundColor();
    }

    public void toggleRecord(View view) {
        record = !record;
    }


    @Override
    public void onBump(List<Sample> samples) {
        vibrate(200);
        try {
            mServerManager.logIn(new SntpTime().execute().get());
            if (record) {
                ExperimentData experimentData = new ExperimentData();
                EditText name = (EditText) findViewById(R.id.name);
                experimentData.setName(name.getText().toString());
                experimentData.setThreshold(Threshold.LOW);
                experimentData.setExperimentType(ExperimentType.FIRST);
                experimentData.setSamples(samples);
                dbHelper.addData(experimentData);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTextMessageReceived(final String message) {
        dataReceived = true;
        bgElement.setBackgroundColor(Integer.parseInt(message));
        mColor = Integer.parseInt(message);
        if (dataDelivered) {
            mServerManager.logOut();
        }
    }

    @Override
    public void onDeviceMatchingSuccessful() {
        mServerManager.sendMessage(Integer.toString(mColor));
    }

    @Override
    public void onDeviceMatchingFailed() {
        TextView textView = new TextView(this);
        textView.setText("Bump failed. \n Please bump again.");
        textView.setGravity(Gravity.CENTER);

        Toast toast = new Toast(this);
        toast.setView(textView);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onTextMessageDelivered() {
        dataDelivered = true;
        if (dataReceived) {
            mServerManager.logOut();
        }
    }

    @Override
    public void onImageReceived(Bitmap bitmap) {

    }

    @Override
    public void onImageDelivered() {
        dataDelivered = true;
        if (dataReceived) {
            mServerManager.logOut();
        }
    }

    @Override
    public void onLoggedIn() {
        System.out.println("logged in");
    }

    @Override
    public void onLoggedOut() {
        System.out.println("logged out");
        dataDelivered = false;
        dataReceived = false;
        mBumpDetector.startMonitoring(1000);
    }

    private void vibrate(int milliseconds) {
        ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(milliseconds);
    }

    private class SntpTime extends AsyncTask<String, Integer, Long> {

        @Override
        protected Long doInBackground(String... strings) {
            long now = 0;
            SntpClient client = new SntpClient();
            if (client.requestTime("0.de.pool.ntp.org", 30000)) {
                now = client.getNtpTime();
            }
            return now;
        }
    }

}
