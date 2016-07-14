package prj3.cs496.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.VoidCallback;

public class AddFriendActivity extends AppCompatActivity {
    RestAdapter mRestAdapter;
    MemberRepository mMemberRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Button addBtn = (Button)findViewById(R.id.add_btn);
        final EditText email = (EditText)findViewById(R.id.add_email_info);
        mRestAdapter = new RestAdapter(getApplicationContext(), "http://52.78.69.111:3000/api");
        mMemberRepository = mRestAdapter.createRepository(MemberRepository.class);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chat = email.getText().toString();
                if(chat.length() == 0){
                    Toast.makeText(getApplicationContext(),"Empty",Toast.LENGTH_SHORT).show();
                }else{
                    Member currentUser = ((ChatApp)AddFriendActivity.this.getApplication()).getCurrentUser();
                    String userId = currentUser.getId().toString();
                    mMemberRepository.addFriend(userId, chat, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            Log.d("ADDFRIEND", "SUCCESS");
                            finish();
                        }

                        @Override
                        public void onError(Throwable t) {
                            Log.d("ADDFRIEND", "FAILED: " + t.getMessage());
                            Toast.makeText(getApplicationContext(), "Find error", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });
    }
}