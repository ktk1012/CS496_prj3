package prj3.cs496.client;

import android.app.Application;

/**
 * Created by q on 2016-07-12.
 */
public class ChatApp extends Application{
    private String token;
    private Member currentUser;

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
