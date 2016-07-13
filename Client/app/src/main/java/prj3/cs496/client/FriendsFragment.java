package prj3.cs496.client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;


public class FriendsFragment extends Fragment implements Updatable{
    public ArrayList<Member> people = new ArrayList<Member>();//name,image for each object
    private RestAdapter mRestAdapter;
    private MemberRepository mMemberRepository;
    FriendAdapter adapter;
    public String username;

    public void update() {
        String userId = mMemberRepository.getCurrentUserId().toString();
        Log.d("UPDATE", "UPDATE" + userId);
        mMemberRepository.getFriends(userId, new ListCallback<Member>() {
            @Override
            public void onSuccess(List<Member> objects) {
                Log.d("BADAOHM", String.valueOf(objects.size()));
                people = (ArrayList<Member>) objects;
                adapter.updateAdapter((ArrayList<Member>) objects);
            }

            @Override
            public void onError(Throwable t) {
                Log.e("GETFRIENDSLIST", t.getMessage());
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new FriendAdapter(getActivity(), people);
        mRestAdapter = new RestAdapter(getContext(), "http://52.78.69.111:3000/api");
        mMemberRepository = mRestAdapter.createRepository(MemberRepository.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.friends_fragment, container, false);
        RecyclerView lv = (RecyclerView) v.findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv.setLayoutManager(linearLayoutManager);
        lv.setHasFixedSize(true);
        lv.setAdapter(adapter);
        lv.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());
        setHasOptionsMenu(true);

        lv.setAdapter(adapter);
        String userId = mMemberRepository.getCurrentUserId().toString();
        if (userId != null) {
            Log.d("GET CURRENT USER", userId);
        }

        mMemberRepository.getFriends(userId, new ListCallback<Member>() {
            @Override
            public void onSuccess(List<Member> objects) {
                Log.d("BADAOHM", String.valueOf(objects.size()));
//                people.clear();
                people = (ArrayList<Member>) objects;
                adapter.updateAdapter((ArrayList<Member>) objects);
            }

            @Override
            public void onError(Throwable t) {
                Log.e("GETFRIENDSLIST", t.getMessage());
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item;
        item = menu.add("Add friend");
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_NEVER);
    }
}
