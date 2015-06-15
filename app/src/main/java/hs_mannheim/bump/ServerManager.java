package hs_mannheim.bump;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;
import java.util.Random;

import hs_mannheim.bump.IServerManager;
import hs_mannheim.bump.ServerEventListener;

/**
 * Created by benjamin on 29/05/15.
 */
public class ServerManager implements IServerManager {

    private Integer deviceID;
    private String mServer;
    private Socket mSocket;
    private ServerEventListener mServerListener;
    private Activity mActivity;

    public ServerManager(String server, Activity activity) {
        this.mServer = server;
        this.mActivity = activity;
        try {
            mSocket = IO.socket(mServer);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        deviceID = new Random().nextInt();
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on("loggedIn", onLoggedIn);
        mSocket.on("loggedOut", onLoggedOut);
        mSocket.on("message", onMessage);
        mSocket.on("image", onImage);
        mSocket.on("imageDelivered", onImageDelivered);
        mSocket.on("partnerIsLoggedIn", onPartnerIsLoggedIn);
        mSocket.on("messageDelivered", onMessageDelivered);
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Connected to the Server");
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Disconnected from Server");
                    mSocket.off(Socket.EVENT_CONNECT, onConnect);
                    mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
                    mSocket.off("loggedIn", onLoggedIn);
                    mSocket.off("loggedOut", onLoggedOut);
                    mSocket.off("message", onMessage);
                    mSocket.off("partnerIsLoggedIn", onPartnerIsLoggedIn);
                    mSocket.off("messageDelivered", onMessageDelivered);
                }
            });
        }
    };

    private Emitter.Listener onLoggedIn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mServerListener.onLoggedIn();
                    timer.start();
                }
            });
        }
    };

    private Emitter.Listener onPartnerIsLoggedIn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mServerListener.onDeviceMatchingSuccessful();
                    timer.cancel();
                }
            });
        }
    };

    private Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final String message = (String) args[0];
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mServerListener.onTextMessageReceived(message);
                }
            });
        }
    };

    private Emitter.Listener onImage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final byte[] imageBytes = (byte[]) args[0];
            final Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mServerListener.onImageReceived(bitmap);
                }
            });
        }
    };

    private Emitter.Listener onImageDelivered = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mServerListener.onImageDelivered();
                }
            });
        }
    };

    private Emitter.Listener onMessageDelivered = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mServerListener.onTextMessageDelivered();
                }
            });
        }
    };

    private Emitter.Listener onLoggedOut = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mServerListener.onLoggedOut();
                }
            });
        }
    };

    @Override
    public void connect() {
        mSocket.connect();
    }

    @Override
    public void disconnect() {
        mSocket.disconnect();
    }

    @Override
    public void logIn(Long timestamp) {
        JSONObject data = new JSONObject();
        {
            try {
                data.put("deviceID", deviceID);
                data.put("timestamp", timestamp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mSocket.emit("logIn", data);
    }

    @Override
    public void logOut() {
        mSocket.emit("logOut");
    }

    @Override
    public void sendMessage(String message) {
        mSocket.emit("message", message);
    }

    @Override
    public void sendImage(final Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                mSocket.emit("image", byteArray);
            }
        }).start();
    }

    @Override
    public void registerListener(ServerEventListener listener) {
        mServerListener = listener;
    }

    private CountDownTimer timer = new CountDownTimer(300, 100) {

        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            mServerListener.onDeviceMatchingFailed();
            logOut();
        }
    };
}
