package prj3.cs496.client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.strongloop.android.loopback.AccessToken;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.UserRepository;
import com.strongloop.android.loopback.callbacks.ObjectCallback;

import org.json.JSONArray;

import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    RestAdapter mRestAdapter;
    MemberRepository mMemberRepository;
    static java.net.CookieManager msCookieManager = new java.net.CookieManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRestAdapter = new RestAdapter(getApplicationContext(), "http://52.78.69.111:3000/api");
        mMemberRepository = mRestAdapter.createRepository(MemberRepository.class);

        JsonObject json = new JsonObject();
        json.addProperty("email", "test@test.com");
        json.addProperty("password", "test");
        mMemberRepository.loginUser("test@test.com", "test", new UserRepository.LoginCallback<Member>() {
            @Override
            public void onSuccess(AccessToken token, Member currentUser) {
                Log.d("ASDFASDFSUCC", token.getUserId().toString());
                mMemberRepository.findCurrentUser(new ObjectCallback<Member>() {
                    @Override
                    public void onSuccess(Member object) {
                        Log.d("CURRENT", object.getEmail());
                    }

                    @Override
                    public void onError(Throwable t) {

                    }
                });
            }

            @Override
            public void onError(Throwable t) {
                Log.d("ASDFASDFFAIL", t.getMessage());

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        callbackManager.onActivityResult(requestCode, resultCode, intent);
    }

}
