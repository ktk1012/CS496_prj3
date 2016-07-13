package prj3.cs496.client;

import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.UserRepository;
import com.strongloop.android.loopback.callbacks.JsonArrayParser;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

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

    public MemberRepository() {
        super("member", null, Member.class);
    }
}
