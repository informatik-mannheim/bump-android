package hs_mannheim.bump;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

import hs_mannheim.bump.R;

/**
 * Created by benjamin on 29/05/15.
 */
public class ApplicationActivity extends Activity implements BumpEventListener, ServerEventListener {

    private SensorManager mSensorManager;
    private IBumpDetector mBumpDetector;
    private ServerManager mServerManager;
    private Boolean dataDelivered = false;
    private Boolean dataReceived = false;
    private final int SELECT_PHOTO = 1;
    private ImageView imageView;
    private final String mLabel = "ApplicationActivity";
    private Bitmap selectedImage;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        imageView = (ImageView) findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mBumpDetector = new BumpDetector(mSensorManager, Threshold.LOW);
        mBumpDetector.registerListener(this);
        mBumpDetector.startMonitoring();

        mServerManager = new ServerManager("http://192.168.1.78:3000", this);
//        mServerManager = new ServerManager("http://141.19.142.50:3000/", this);
        mServerManager.registerListener(this);
        mServerManager.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBumpDetector.stopMonitoring();
        mServerManager.disconnect();
    }

    @Override
    public void onBump(List<Sample> samples) {
        vibrate(200);
        progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        try {
            mServerManager.logIn(new SntpTime().execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoggedIn() {
        System.out.println("logged in");
    }

    @Override
    public void onLoggedOut() {
        dataDelivered = false;
        dataReceived = false;
        mBumpDetector.startMonitoring(1000);
    }

    @Override
    public void onDeviceMatchingSuccessful() {
        if (imageView.getDrawable() != null) {
            mServerManager.sendImage(selectedImage);
        } else {
            mServerManager.sendMessage("noData");
        }
    }

    @Override
    public void onDeviceMatchingFailed() {
        progress.setMessage("Bump failed. Please Bump again");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progress.dismiss();
            }}, 1000);
    }

    @Override
    public void onTextMessageReceived(String message) {
        Log.d(mLabel, message);
        dataReceived = true;
        if (dataDelivered) {
            progress.dismiss();
            mServerManager.logOut();
        }
    }

    @Override
    public void onTextMessageDelivered() {
        dataDelivered = true;
        if (dataReceived) {
            progress.dismiss();
            mServerManager.logOut();
        }
    }

    @Override
    public void onImageReceived(Bitmap bitmap) {
        dataReceived = true;
        imageView.setBackgroundResource(0);
        imageView.setImageBitmap(bitmap);
        selectedImage = bitmap;
        if (dataDelivered) {
            progress.dismiss();
            mServerManager.logOut();
        }
    }

    @Override
    public void onImageDelivered() {
        dataDelivered = true;
        if (dataReceived) {
            progress.dismiss();
            mServerManager.logOut();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageView.setBackgroundResource(0);
                        imageView.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
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