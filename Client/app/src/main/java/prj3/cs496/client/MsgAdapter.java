package prj3.cs496.client;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by q on 2016-07-14.
 */
public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

    private JSONArray msgs;
    private Context context;

    public MsgAdapter(Context context) {
        this.msgs = new JSONArray();
        this.context = context;
    }

    @Override
    public int getItemCount() {return msgs.length();}

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final JSONObject msg;
        try {
            msg = msgs.getJSONObject(position);
            JSONObject content = msg.getJSONObject("content");
            JSONObject author = msg.getJSONObject("author");
            holder.mChatView.setText(content.getString("content"));
            holder.mAuthorView.setText(author.getString("username"));
            Glide.with(context)
                    .load(author.getString("picture_thumb"))
                    .override(75, 75).centerCrop()
                    .into(holder.mAuthorImgView);
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
        public final TextView mAuthorView;
        public final ImageView mAuthorImgView;

        public ViewHolder(View v) {
            super(v);
            mChatView = (TextView) v.findViewById(R.id.message);
            mAuthorView = (TextView) v.findViewById(R.id.author);
            mAuthorImgView = (ImageView) v.findViewById(R.id.author_img);
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

    public void appendAdapter(JSONObject newObj) {
        msgs.put(newObj);
        notifyItemInserted(msgs.length() - 1);
    }
}