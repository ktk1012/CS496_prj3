package prj3.cs496.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import java.util.ArrayList;

public class AddRoomActivity extends AppCompatActivity {

    private RestAdapter mRestAdapter;
    private ChatRoomRepository mChatRoomRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        mRestAdapter = new RestAdapter(getApplicationContext(), "http://52.78.69.111:3000/api");
        mChatRoomRepository = mRestAdapter.createRepository(ChatRoomRepository.class);

        Button makeRoom = (Button)findViewById(R.id.room_btn);
        makeRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText room_edit = (EditText) findViewById(R.id.roomname);
                String room_name = room_edit.getText().toString();
                if (room_name.length() == 0) {
                    room_edit.setError("Name is blank");
                }
                final ChatRoom room = mChatRoomRepository.createObject(
                        ImmutableMap.of("name", room_name, "participant", new ArrayList<Member>()));
                room.save(new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(AddRoomActivity.this, ChatRoomActivity.class);
                        intent.putExtra("roomId", room.getId().toString());
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(Throwable t) {
                        room_edit.setError(t.getMessage());
                    }
                });
            }
        });
    }
}
