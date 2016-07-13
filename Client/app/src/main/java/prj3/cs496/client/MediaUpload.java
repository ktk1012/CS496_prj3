package prj3.cs496.client;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import java.io.File;

public class MediaUpload extends AppCompatActivity {

    private int SELECT_EMOZI = 100;
    private int SELECT_PICTURE = 200;

    private String roomId;
    private RestAdapter mRestAdapter;
    private ChatRoomRepository mChatRoomRepository;

    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_upload);
        Button emoziBtn = (Button) findViewById(R.id.upload_emozi);
        Button pictureBtn = (Button) findViewById(R.id.upload_picture);
        mLinearLayout = (LinearLayout) findViewById(R.id.edit_layout);
        mLinearLayout.setVisibility(View.GONE);
        Intent intent = getIntent();
        roomId = intent.getStringExtra("roomId");

        mRestAdapter = new RestAdapter(getApplicationContext(), "http://52.78.69.111:3000/api");
        mChatRoomRepository = mRestAdapter.createRepository(ChatRoomRepository.class);

        emoziBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery,SELECT_EMOZI);
            }
        });

        pictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery,SELECT_PICTURE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_EMOZI && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
            cursor.moveToFirst();
            final String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            mLinearLayout.setVisibility(View.VISIBLE);
            final EditText command_edit = (EditText) findViewById(R.id.edit_command);
            Button submit_btn = (Button) findViewById(R.id.emoji_submit);

            submit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String command = command_edit.getText().toString();
                    if (command.length() == 0) {
                        command_edit.setError("Command input is blank");
                    }
                    mChatRoomRepository.createEmoji(roomId, command, new File(imagePath),
                            new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    Log.d("SUCCESS", "SCSCSC");
                                    finish();
                                }

                                @Override
                                public void onError(Throwable t) {
                                    Log.d("Emoji", "Error: " + t.getMessage());
                                    command_edit.setError("Invalid command");
                                }
                            });
                }
            });
        }

        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            mChatRoomRepository.sendImage(roomId, new File(imagePath),
                    new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            Log.d("FileUpload", "SUCCESS");

                        }

                        @Override
                        public void onError(Throwable t) {
                            Log.d("FileUpload", "FAIL: " + t.getMessage());
                        }
                    });
        }
    }
}

