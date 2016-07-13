package prj3.cs496.client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ListCallback;

import java.util.ArrayList;
import java.util.List;


public class FriendsFragment extends Fragment {
    public ArrayList<Member> people = new ArrayList<Member>();//name,image for each object
    private RestAdapter mRestAdapter;
    private MemberRepository mMemberRepository;
    FriendAdapter2 adapter;
    public String username;

    public FriendsFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new FriendAdapter2(getActivity(), people);
        mRestAdapter = new RestAdapter(getContext(), "http://52.78.69.111:3000/api");
        mMemberRepository = mRestAdapter.createRepository(MemberRepository.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.friends_fragment, container, false);
        ListView lv = (ListView) v.findViewById(R.id.list);
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
}
