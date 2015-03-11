package hs_mannheim.bump;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;


public class MainActivity extends ActionBarActivity implements Observer {

    private SensorManager _sensorManager;
    private IBumpDetector _bumpDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        _bumpDetector = new BumpDetector(_sensorManager);
        _bumpDetector.addBumpObserver(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        vibrate(700);
        showBumpText(700);
    }

    private void vibrate(int milliseconds) {
        ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(milliseconds);
    }

    private void showBumpText(int milliseconds) {
        final TextView bump_text = (TextView) findViewById(R.id.bump_text);
        bump_text.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                bump_text.setVisibility(View.INVISIBLE);
            }
        }, milliseconds);
    }
}
