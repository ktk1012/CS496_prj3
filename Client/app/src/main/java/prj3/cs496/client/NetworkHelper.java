package prj3.cs496.client;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by q on 2016-07-12.
 */
public class NetworkHelper {
    private Activity activity;
    private ArrayList<Member> people;       //이름,email
    String useremail;

    public NetworkHelper(Activity activity,String email){
        this.activity = activity;
        this.useremail = email;
    }

    public static boolean isConnected(Activity activity) {
        ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    public static InputStream getImage(final String imageUrl) {
        try {
            // Connect to the server
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // Setup header
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(1000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            // Send the data
            conn.connect();

            // Get response
            int response = conn.getResponseCode();
            InputStream is = conn.getInputStream();
            return is;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getGallerySize(final String imageUrl) {
        try {
            // Connect to the server
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // Setup header
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(1000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            // Send the data
            conn.connect();

            // Get response
            int response = conn.getResponseCode();
            InputStream is = conn.getInputStream();
            return is.read();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void readDataFromDB(String name) {
        if (isConnected(activity) && name != null)
            new GetDataTask().execute(name);
    }

    private void writeDataToDB(String email) {
        if (isConnected(activity)) {
            new SendDataTask().execute(email);
        }
    }

    public class GetDataTask extends AsyncTask<String, Void, Void> {


        private String name;
        public GetDataTask() {
        }

        @Override
        protected Void doInBackground(String... params) {

            name = params[0];
            JSONArray db = getJSON(name);
            people = new ArrayList<>();
            return null;
        }

        public JSONArray getJSON(String name) {

            String rawURL = null;
            InputStream is = null;

            try {
                rawURL = MainActivity.server_url_person + "/" + URLEncoder.encode(name, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.i("PLACE", "getJSON");
                e.printStackTrace();
            }

            try {
                URL url = new URL(rawURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000 /* milliseconds */);
                conn.setConnectTimeout(1000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                Log.i("LogCat", "[GET]CONNECTION ESATABLISHED (" + rawURL + ")");
                int response = conn.getResponseCode();
                is = conn.getInputStream();
                Log.i("LogCat", "[GET]GET RESPOND");
                // Convert the InputStream into a string
                String contentAsString = StreamHelper.readIt(is);
                Log.i("LogCat", "[GET]READ RESPOND: "+contentAsString);
                is.close();
                return new JSONArray(contentAsString);
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            } catch (JSONException e) {
            }

            Log.i("LogCat", "[GET]FAILED TO CONNECT");
            return null;
        }
    }

    public class SendDataTask extends AsyncTask<String, Void, Void> {

        String rawURL;
        String email;

        @Override
        protected Void doInBackground(String... params) {
            email = params[0];
            try {
                rawURL = MainActivity.server_url_person + "/" + URLEncoder.encode(useremail, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            putJSON();

            return null;
        }

        public void putJSON() {
            OutputStream os = null;

            int len = 0;
            try {
                len = email.getBytes("UTF-8").length;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                URL url = new URL(rawURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000 /* milliseconds */);
                conn.setConnectTimeout(1000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(len);
                os = conn.getOutputStream();
                OutputStreamWriter wrt = new OutputStreamWriter(os, "UTF-8");
                Log.i("LogCat", "[SEND]ENCODING: " + wrt.getEncoding());
                Log.i("LogCat", "[SEND]JSON RAW: " + email);
                wrt.write(email);
                wrt.flush();
                conn.connect();
                int response = conn.getResponseCode();
                os.close();
            } catch (MalformedURLException e) {
                Log.i("LogCat", "[SEND]FAILED TO CONNECT by MalformedURLException");
            } catch (IOException e) {
                Log.i("LogCat", "[SEND]FAILED TO CONNECT by IOException");
                Log.i("LogCat", e.toString());
            }
        }

    }
}
