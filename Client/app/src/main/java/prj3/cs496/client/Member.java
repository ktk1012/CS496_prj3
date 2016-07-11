package prj3.cs496.client;

import com.strongloop.android.loopback.User;

/**
 * Created by q on 2016-07-12.
 */
public class Member extends User {
    String picture;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
