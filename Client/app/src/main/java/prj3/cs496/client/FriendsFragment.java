package prj3.cs496.client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class FriendsFragment extends Fragment {
    public ArrayList<Member> people = new ArrayList<Member>();//name,image for each object
    FriendAdapter adapter;
    public String username;

    public FriendsFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new FriendAdapter(getActivity(), people);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.friends_fragment, container, false);
        RecyclerView lv = (RecyclerView) v.findViewById(R.id.list);
        lv.setAdapter(adapter);
        return v;
    }
}
