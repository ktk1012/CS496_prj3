package prj3.cs496.client;

import android.app.Application;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by q on 2016-07-12.
 */
public class ChatApp extends Application{
    private String token;
    private Member currentUser;

    public Socket getmSocket() {
        return mSocket;
    }

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://52.78.69.111:3000/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Member getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Member currentUser) {
        this.currentUser = currentUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
