package prj3.cs496.client;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FriendAdapter2 extends BaseAdapter {

    private Activity activity;
    private ArrayList<Member> people;

    public FriendAdapter2(Activity activity, ArrayList<Member> people){
        this.activity = activity;
        this.people = people;
    }

    @Override
    public int getCount() {
        return people.size();
    }

    @Override
    public Object getItem(int i) {
        return people.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;
        if(view == null){
            v = LayoutInflater.from(activity).inflate(R.layout.person,viewGroup,false);
        }else{
            v = view;
        }
        TextView email = (TextView) v.findViewById(R.id.email);
        email.setText(people.get(i).getEmail());
        return v;
    }

    public void updateAdapter(ArrayList<Member> list) {
        people.clear();
        people.addAll(list);
        this.notifyDataSetChanged();
    }
}