package prj3.cs496.client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeTransform;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.UserRepository;
import com.strongloop.android.loopback.callbacks.ObjectCallback;

import org.apache.http.params.HttpParams;
import org.json.JSONArray;

import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements TokenRetrieveEnd {
    private CallbackManager callbackManager;
    RestAdapter mRestAdapter;
    prj3.cs496.client.UserRepository mUserRepo;
    static java.net.CookieManager msCookieManager = new java.net.CookieManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        mRestAdapter = new RestAdapter(getApplicationContext(), "http://52.78.69.111:3000/api");
        mUserRepo = mRestAdapter.createRepository(prj3.cs496.client.UserRepository.class);
        LoginButton loginButton = (LoginButton) findViewById(R.id.facebook_login);
        loginButton.setReadPermissions("user_friends, email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), loginResult.getAccessToken().getToken(), Toast.LENGTH_SHORT).show();
                new TokenRetrievalTask(MainActivity.this).execute(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        callbackManager.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void tokenRetrieveEnd(String response) {
        mUserRepo.findCurrentUser(new ObjectCallback<User>() {
            @Override
            public void onSuccess(User object) {

            }

            @Override
            public void onError(Throwable t) {

            }
        });

    }

    private class TokenRetrievalTask extends AsyncTask<String, String, String> {
        private TokenRetrieveEnd tokenRetrieved;
        public TokenRetrievalTask(TokenRetrieveEnd activityContext) {
            this.tokenRetrieved = activityContext;
        }

        protected String doInBackground(String... tokens) {
            String result = "";
            try {
                URL url = new URL("http://52.78.69.111:3000/auth/facebook-token/callback?access_token=" + tokens[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setInstanceFollowRedirects(false);
                urlConnection.connect();

                Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
                List<String> cookiesHeader = headerFields.get("set-cookie");
                if (cookiesHeader != null) {
                    for (String cookie : cookiesHeader) {
                        msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                        if (HttpCookie.parse(cookie).get(0).getName().equalsIgnoreCase("access_token")) {
                            mRestAdapter.setAccessToken(java.net.URLDecoder.decode(HttpCookie.parse(cookie).get(0).getValue(), "UTF-8").split("\\.")[0].split(":")[1]);
                        }
                        if (HttpCookie.parse(cookie).get(0).getName().equalsIgnoreCase("userId")) {
                            result = java.net.URLDecoder.decode(HttpCookie.parse(cookie).get(0).getValue(),"UTF-8").split("\\.")[0].split(":")[1];
                        }
                    }
                }
                urlConnection.disconnect();
            } catch(Exception e) {
                Log.e("Loopback", "Login failed", e);
            }
            return result;
        }


        protected void onPostExecute(String result) {
            SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(mUserRepo.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
            String json = new JSONArray().put(result).toString();
            editor.putString(mUserRepo.PROPERTY_CURRENT_USER_ID, json);
            editor.commit();
            tokenRetrieved.tokenRetrieveEnd("Success");
        }
    }
}
