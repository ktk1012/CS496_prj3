package prj3.cs496.client;

import com.strongloop.android.loopback.Model;

/**
 * Created by q on 2016-07-13.
 */
public class Message extends Model {
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Member getAuthor() {
        return author;
    }

    public void setAuthor(Member author) {
        this.author = author;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    String roomId;
    Member author;
    Content content;

}
