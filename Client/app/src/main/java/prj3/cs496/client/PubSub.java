package prj3.cs496.client;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;

import java.util.ArrayList;

/**
 * Created by q on 2016-07-07.
 */
public class PubSub {
    ArrayList<String> container = new ArrayList<String>();
    com.github.nkzawa.socketio.client.Socket mSocket;

    public PubSub(com.github.nkzawa.socketio.client.Socket mSocket) {
        this.mSocket = mSocket;
    }

    public void Subscribe(String collectionName, String modelId, String method, String methodName, Emitter.Listener callback) {
        String name = "";
        if (method == "POST") {
            name = collectionName + "/" + methodName + "/" + method;
            mSocket.on(name, callback);
        } else {
            name = collectionName + "/" + modelId + "/" + methodName + "/" + method;
            mSocket.on(name, callback);
        }
        Log.d("SUBSCRIBE", name);
        container.add(name);
    }

    public void UnscribeAll() {
        for (int i = 0; i < container.size(); i++) {
            mSocket.off(container.get(i));
        }
        container.clear();
    }

}
