package prj3.cs496.client;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by q on 2016-07-14.
 */
public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

    private JSONArray msgs;

    public MsgAdapter() {
        this.msgs = new JSONArray();
    }

    @Override
    public int getItemCount() {return msgs.length();}

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final JSONObject msg;
        try {
            msg = msgs.getJSONObject(position);
            JSONObject content = msg.getJSONObject("content");
            holder.mChatView.setText(content.getString("content"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mChatView;

        public ViewHolder(View v) {
            super(v);
            mChatView = (TextView) v.findViewById(R.id.message);
        }
    }

    public void updateAdapter(JSONArray newList) {
        Log.d("UPDATEADAPTER", "newList: " +String.valueOf(newList.length()));
        msgs = newList;
        this.notifyDataSetChanged();
    }

    public JSONObject getMessage(int position) throws JSONException {
        return msgs.getJSONObject(position);
    }
}