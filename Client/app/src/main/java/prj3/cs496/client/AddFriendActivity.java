package prj3.cs496.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.strongloop.android.loopback.RestAdapter;

public class AddFriendActivity extends AppCompatActivity {
    RestAdapter mRestAdapter;
    MemberRepository mMemberRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
       
    }
}
