package prj3.cs496.client;

import com.strongloop.android.loopback.Model;

import java.util.List;

/**
 * Created by q on 2016-07-13.
 */
public class ChatRoom extends Model {
    String name;
    List<Member> participant;

    public List<Member> getParticipant() {
        return participant;
    }

    public void setParticipant(List<Member> participant) {
        this.participant = participant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
