package prj3.cs496.client;

import android.util.Log;

import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.callbacks.JsonArrayParser;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import java.util.List;

/**
 * Created by q on 2016-07-13.
 */
public class ChatRoomRepository extends ModelRepository<ChatRoom>{
    public ChatRoomRepository() {super("ChatRoom", ChatRoom.class);}

    @Override
    public RestContract createContract() {
        RestContract contract = super.createContract();

        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/:roomId/join", "GET"),
                getClassName() + ".join");

        return contract;
    }

    public void join(String roomId, final ListCallback<Message> callback) {
        Log.d("JOIN", getNameForRestUrl());
        invokeStaticMethod("join", ImmutableMap.of("roomId", roomId),
                new JsonArrayParser<Message>(new MessageRepository(), callback));
    }
}
