package prj3.cs496.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;
import com.strongloop.android.remoting.adapters.Adapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatRoomActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String roomId;
    private RestAdapter mRestAdapter;
    private ChatRoomRepository mChatRoomRepository;
    private int base = 0;
    private ChatRoom mChatRoom;
    private MsgAdapter adapter;
    private JSONArray mArray = new JSONArray();
    private PubSub mPubSub;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        roomId = intent.getStringExtra("roomId");
        Button sendBtn = (Button) findViewById(R.id.txtsend_btn);
        final EditText chatTxt = (EditText) findViewById(R.id.chat);

        Socket socket = ((ChatApp) ChatRoomActivity.this.getApplication()).getmSocket();
        mPubSub = new PubSub(socket);
        SubScribe(mPubSub);



        mRestAdapter = new RestAdapter(getApplicationContext(), "http://52.78.69.111:3000/api");
        mChatRoomRepository = mRestAdapter.createRepository(ChatRoomRepository.class);

        Button plusBtn = (Button) findViewById(R.id.plus);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chat = chatTxt.getText().toString();
                if(chat.length() == 0){
                    Toast.makeText(getApplicationContext(),"Empty",Toast.LENGTH_SHORT).show();
                }else{
                    mChatRoomRepository.sendText(roomId, chat, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            chatTxt.setText("");
                        }

                        @Override
                        public void onError(Throwable t) {
                        }
                    });
                }
            }
        });

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MediaUpload.class);
                intent.putExtra("roomId", roomId);
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.chat_list);
        adapter = new MsgAdapter(getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getApplicationContext()).build());
        mRecyclerView.setAdapter(adapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mChatRoomRepository.join(roomId, new ObjectCallback<ChatRoom>() {
                    @Override
                    public void onSuccess(final ChatRoom object) {
                        mChatRoom = object;
                        Log.d("JOINSUCCESS", mChatRoom.getName());
                        setTitle(mChatRoom.getName());

                        mChatRoomRepository.getMessage(roomId, base, new Adapter.JsonCallback() {
                            @Override
                            public void onSuccess(Object response) {
                                JSONObject obj = (JSONObject) response;
                                try {
                                    mArray = obj.getJSONArray("messages");
                                    Log.d("GETMESSAGE", mArray.toString());
                                    base = obj.getInt("next");
                                    adapter.updateAdapter(mArray);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d("CHATROOMJOIN", "FAIL " + t.getMessage());
                    }
                });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_room, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void SubScribe(PubSub pubSub) {
        pubSub.Subscribe("ChatRoom", roomId, "POST", "sendtext", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                ChatRoomActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject)args[0];
                        mArray.put(data);
                        adapter.notifyItemInserted(mArray.length() - 1);
                        mRecyclerView.scrollToPosition(mArray.length() - 1);
                    }
                });
            }
        });

        pubSub.Subscribe("ChatRoom", roomId, "GET", "join", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                ChatRoomActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject)args[0];
                        String username = null;
                        try {
                            username = data.getString("username");
                            Toast.makeText(getApplicationContext(), username + " Join",
                                    Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPubSub.UnscribeAll();
    }

}
