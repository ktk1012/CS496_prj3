package prj3.cs496.client;

import com.strongloop.android.loopback.User;

import java.util.List;

/**
 * Created by q on 2016-07-12.
 */
public class Member extends User {
    private String picture;
    private String picture_thumb;
    private List<String> chatroom;
    private List<String> friends;
    private String username;

    public String getPicture_thumb() {
        return picture_thumb;
    }

    public void setPicture_thumb(String picture_thumb) {
        this.picture_thumb = picture_thumb;
    }

    public List<String> getChatroom() {
        return chatroom;
    }

    public void setChatroom(List<String> chatroom) {
        this.chatroom = chatroom;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
