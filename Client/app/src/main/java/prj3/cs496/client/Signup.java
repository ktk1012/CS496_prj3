package prj3.cs496.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import java.util.ArrayList;

/**
 * Created by q on 2016-07-13.
 */
public class Signup extends AppCompatActivity {

    private RestAdapter mRestAdapter;
    private MemberRepository mMemberRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_window);
        mRestAdapter = new RestAdapter(getApplicationContext(), "http://52.78.69.111:3000/api");
        mMemberRepository = mRestAdapter.createRepository(MemberRepository.class);
        Button submit_btn = (Button) findViewById(R.id.signup_btn);
        submit_btn.setOnClickListener(mClickListener);
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText email_edit = (EditText) findViewById(R.id.signup_email);
            EditText name_edit = (EditText) findViewById(R.id.signup_name);
            EditText password_edit = (EditText) findViewById(R.id.signup_pw);
            EditText password_confirm = (EditText) findViewById(R.id.signup_pw2);

            String email = email_edit.getText().toString();
            String name = name_edit.getText().toString();
            String password = password_edit.getText().toString();
            String confirm = password_confirm.getText().toString();

            boolean valid = true;


            if (email.length() == 0) {
                valid = false;
                email_edit.setError("Email is empty");
            }

            if (name.length() == 0) {
                valid = false;
                name_edit.setError("Name is empty");
            }

            if (password.length() == 0) {
                valid = false;
                password_edit.setError("Password is empty");
            }

            if (!password.equals(confirm)) {
                valid = false;
                password_confirm.setError("Password and Confirm is different");
            }

            if (valid == false) {
                return;
            }

            Member member = mMemberRepository.createUser(email, password,
                    ImmutableMap.of(
                            "username", name,
                            "friends", new ArrayList<String>(),
                            "chatroom", new ArrayList<String>(),
                            "picture", "http://cs496bucket.s3.amazonaws.com/placeholder.jpg",
                            "picture_thumb", "http://cs496bucket.s3.amazonaws.com/placeholder_thumb.jpg"));

            member.save(new VoidCallback() {
                @Override
                public void onSuccess() {
                    Log.d("SIGNUP", "SUCCESS");
                    finish();
                }

                @Override
                public void onError(Throwable t) {
                    Log.d("SIGNUP", "FAIL: " + t.getMessage());
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT);
                }
            });



        }

    };

}
