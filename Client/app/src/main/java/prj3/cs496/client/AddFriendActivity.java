package prj3.cs496.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Button addBtn = (Button)findViewById(R.id.add_btn);
        final EditText email = (EditText)findViewById(R.id.add_email_info);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chat = email.getText().toString();
                if(chat.length() == 0){
                    Toast.makeText(getApplicationContext(),"Empty",Toast.LENGTH_SHORT).show();
                }else{
                    /*
                    mChatRoomRepository.sendText(roomId, chat, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            email.setText("");
                        }

                        @Override
                        public void onError(Throwable t) {
                        }
                    });
                    */
                }
            }
        });
    }
}
