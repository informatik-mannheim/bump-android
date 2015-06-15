package hs_mannheim.bump;

import android.graphics.Bitmap;

/**
 * Created by benjamin on 29/05/15.
 */
public interface ServerEventListener {
    void onLoggedIn();
    void onLoggedOut();
    void onDeviceMatchingSuccessful();
    void onDeviceMatchingFailed();
    void onTextMessageReceived(String message);
    void onTextMessageDelivered();
    void onImageReceived(Bitmap bitmap);
    void onImageDelivered();
}
