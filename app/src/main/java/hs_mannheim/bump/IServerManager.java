package hs_mannheim.bump;

import android.graphics.Bitmap;

/**
 * Created by benjamin on 29/05/15.
 */
public interface IServerManager {
    void connect();
    void disconnect();
    void logIn(Long timestamp);
    void logOut();
    void sendMessage(String message);
    void sendImage(Bitmap bitmap);
    void registerListener(ServerEventListener listener);
}
