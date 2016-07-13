package prj3.cs496.client;

import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.UserRepository;
import com.strongloop.android.loopback.callbacks.JsonArrayParser;
import com.strongloop.android.loopback.callbacks.JsonObjectParser;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by q on 2016-07-12.
 */

public class MemberRepository extends UserRepository<Member> {
    public interface LoginCallback extends UserRepository.LoginCallback<Member> {
    }

    @Override
    public RestContract createContract() {
        RestContract contract = super.createContract();

        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/:userId" + "/friends", "GET"),
                getClassName() + ".friends");

        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/:userId" + "/chatrooms", "GET"),
                getClassName() + ".chatrooms");

        contract.addItem(RestContractItem.createMultipart("/" + getNameForRestUrl() + "/profile", "POST"),
                getClassName() + ".uploadprofile");

        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/:userId/friend/:friendEmail", "PUT"),
                getClassName() + ".addFriend");

        return contract;
    }

    public void getFriends(String userId, final ListCallback<Member> callback) {
        invokeStaticMethod("friends", ImmutableMap.of("userId", userId),
                new JsonArrayParser<Member>(this, callback));
    }

    public void getChatRooms(String userId, final ListCallback<ChatRoom> callback) {
        invokeStaticMethod("chatrooms", ImmutableMap.of("userId", userId),
                new JsonArrayParser<ChatRoom>(new ChatRoomRepository(), callback));
    }

    public void testMethod(File file, final ObjectCallback<Member> callback) {
        invokeStaticMethod("uploadprofile", ImmutableMap.of("file", file),
                new JsonObjectParser<Member>(this, callback));
    }

    public void addFriend(String userId, String friendEmail, final VoidCallback callback) {
        invokeStaticMethod("addFriend", ImmutableMap.of("userId", userId, "friendEmail", friendEmail),
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

    public MemberRepository() {
        super("member", null, Member.class);
    }
}
