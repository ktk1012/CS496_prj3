package prj3.cs496.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by q on 2016-07-14.
 */
public class InviteFriend extends AppCompatActivity {

    public ArrayList<Member> people = new ArrayList<Member>();//name,image for each object
    private RestAdapter mRestAdapter;
    private MemberRepository mMemberRepository;
    FriendAdapter adapter;
    String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_fragment);
        mRestAdapter = new RestAdapter(getApplicationContext(), "http://52.78.69.111:3000/api");
        mMemberRepository = mRestAdapter.createRepository(MemberRepository.class);

        Intent intent = getIntent();
        roomId = intent.getStringExtra("roomId");

        RecyclerView lv = (RecyclerView) findViewById(R.id.list);
        adapter = new FriendAdapter(getApplicationContext(), people);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv.setLayoutManager(linearLayoutManager);
        lv.setHasFixedSize(true);
        lv.setAdapter(adapter);
        lv.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getApplicationContext()).build());
        Member currentUser = ((ChatApp) InviteFriend.this.getApplication()).getCurrentUser();
        String userId = currentUser.getId().toString();
        ImageView myPicture = (ImageView)findViewById(R.id.myImg);
        TextView myName = (TextView)findViewById(R.id.myName);

        mMemberRepository.getFriends(userId, new ListCallback<Member>() {
            @Override
            public void onSuccess(List<Member> objects) {
//                people.clear();
                Log.d("GETFRIENDINVITE", String.valueOf(objects.size()));
                people = (ArrayList<Member>) objects;
                adapter.updateAdapter((ArrayList<Member>) objects);
            }

            @Override
            public void onError(Throwable t) {
            }
        });

        lv.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View childView, int position) {
                        Member member = people.get(position);
                        mMemberRepository.invite(member.getId().toString(), roomId, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                finish();
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.e("Invite", "ERROR: " + t.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onItemLongPress(View childView, int position) {

                    }
                }));



    }
}
