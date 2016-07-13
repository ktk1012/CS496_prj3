package prj3.cs496.client;

import android.util.Log;

import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.callbacks.JsonArrayParser;
import com.strongloop.android.loopback.callbacks.JsonObjectParser;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;
import com.strongloop.android.remoting.adapters.StreamParam;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
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

        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/:roomId/message", "GET"),
                getClassName() + ".getMessage");


        return contract;
    }


    public void join(String roomId, final ObjectCallback<ChatRoom> callback) {
        Log.d("JOIN", getNameForRestUrl());
        invokeStaticMethod("join", ImmutableMap.of("roomId", roomId),
                new JsonObjectParser<ChatRoom>(this, callback));
    }

    public void getMessage(String roomId, int base, final Adapter.JsonCallback callback) {
        invokeStaticMethod("getMessage", ImmutableMap.of("roomId", roomId, "base", base),
                new Adapter.JsonObjectCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                       callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Throwable t) {
                        callback.onError(t);
                    }
                });

    }

}
