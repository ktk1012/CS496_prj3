package prj3.cs496.client;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
        Member currentUser = ((ChatApp) getActivity().getApplication()).getCurrentUser();
        String userId = currentUser.getId().toString();
        Log.d("USERID", userId);
        ImageView myPicture = (ImageView)v.findViewById(R.id.myImg);
        TextView myName = (TextView)v.findViewById(R.id.myName);
        myName.setText(currentUser.getUsername().toString());
        Glide.with(getContext())
                .load(currentUser.getPicture_thumb())
                .into(myPicture);

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
        item.setIcon(R.drawable.icon_person_add);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(getContext(), AddFriendActivity.class);
                startActivity(intent);
                return true;
            default:
                Log.d("OPTIONSELECT", String.valueOf(item.getItemId()));
                return super.onOptionsItemSelected(item);
        }
    }
}
