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

        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/:roomId/textmessage", "POST"),
                getClassName() + ".sendText");

        contract.addItem(RestContractItem.createMultipart("/" + getNameForRestUrl() + "/:roomId/image", "POST"),
                getClassName() + ".sendImage");


        return contract;
    }

    public void sendImage(String roomId, File file, final VoidCallback callback) {
        invokeStaticMethod("sendImage", ImmutableMap.of("roomId", roomId, "file", file),
                new Adapter.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        callback.onSuccess();
                    }

                    @Override
                    public void onError(Throwable t) {
                        callback.onError(t);
                    }
                });
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

    public void sendText(String roomId, String content, final VoidCallback callback) {
        invokeStaticMethod("sendText", ImmutableMap.of("roomId", roomId, "content", content),
                new Adapter.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        callback.onSuccess();
                    }

                    @Override
                    public void onError(Throwable t) {
                        callback.onError(t);
                    }
                });
    }

}
