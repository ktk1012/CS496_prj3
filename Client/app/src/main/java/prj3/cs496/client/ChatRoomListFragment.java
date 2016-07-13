package prj3.cs496.client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by q on 2016-07-12.
 */
public class ChatRoomListFragment extends Fragment{
    private RestAdapter mRestAdapter;
    private MemberRepository mMemberRepository;
    private ChatRoomAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ChatRoomAdapter();
        mRestAdapter = new RestAdapter(getContext(), "http://52.78.69.111:3000/api");
        mMemberRepository = mRestAdapter.createRepository(MemberRepository.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chatrooms_fragment, container, false);
        RecyclerView lv = (RecyclerView) v.findViewById(R.id.room_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);;
        lv.setLayoutManager(linearLayoutManager);
        lv.setHasFixedSize(true);
        lv.setAdapter(mAdapter);
        lv.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());
        setHasOptionsMenu(true);

        mMemberRepository.getChatRooms(mMemberRepository.getCurrentUserId().toString(),
                new ListCallback<ChatRoom>() {
                    @Override
                    public void onSuccess(List<ChatRoom> objects) {
                        mAdapter.updateAdapter((ArrayList<ChatRoom>) objects);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d("GETROOMS", "ERROR:" + t.getMessage());

                    }
                });

        return v;
    }

}
