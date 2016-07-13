package prj3.cs496.client;

import com.strongloop.android.loopback.ModelRepository;

import java.util.List;

/**
 * Created by q on 2016-07-13.
 */
public class ChatRoomRepository extends ModelRepository<ChatRoom>{
    public ChatRoomRepository() {super("chatroom", ChatRoom.class);}
}
