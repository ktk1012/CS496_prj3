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
public class MsgAdapter extends RecyclerView.Adapter {

    private JSONArray msgs;
    private Context context;

    private final int TEXT = 1, IMAGE = 2;

    public MsgAdapter(Context context) {
        this.msgs = new JSONArray();
        this.context = context;
    }

    @Override
    public int getItemCount() {return msgs.length();}

//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        final JSONObject msg;
//        try {
//            msg = msgs.getJSONObject(position);
//            JSONObject content = msg.getJSONObject("content");
//            JSONObject author = msg.getJSONObject("author");
//            holder.mChatView.setText(content.getString("content"));
//            holder.mAuthorView.setText(author.getString("username"));
//            Glide.with(context)
//                    .load(author.getString("picture_thumb"))
//                    .override(75, 75).centerCrop()
//                    .into(holder.mAuthorImgView);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public int getItemViewType(int position) {
        try {
            JSONObject obj = msgs.getJSONObject(position);
            JSONObject content = obj.getJSONObject("content");
            String type = content.getString("type");
            if (type.equals("text")) {
                return TEXT;
            } else if (type.equals("image")) {
                return IMAGE;
            }
            return -1;
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == IMAGE) {
            Log.d("VIEWTYPE", "IMAGE!!");
        }

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        switch (viewType) {
            case TEXT:
                View text_msg = inflater.inflate(R.layout.message, parent, false);
                text_msg.setLayoutParams(lp);
                viewHolder = new ViewHolder(text_msg);
                break;
            case IMAGE:
                View img_msg = inflater.inflate(R.layout.message_img, parent, false);
                img_msg.setLayoutParams(lp);
                viewHolder = new ImageViewHolder(img_msg);
                break;
            default:
                viewHolder = null;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TEXT:
                ViewHolder viewHolder = (ViewHolder) holder;
                configureViewHolder(viewHolder, position);
                break;
            case IMAGE:
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                configureImageViewHolder(imageViewHolder, position);
                break;

        }

    }

    private void configureViewHolder(ViewHolder holder, int position) {
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

    private void configureImageViewHolder(ImageViewHolder holder, int position) {
        final JSONObject msg;
        try {
            msg = msgs.getJSONObject(position);
            JSONObject content = msg.getJSONObject("content");
            JSONObject author = msg.getJSONObject("author");
            holder.mAuthorView.setText(author.getString("username"));
            Glide.with(context)
                    .load(author.getString("picture_thumb"))
                    .override(75, 75).centerCrop()
                    .into(holder.mAuthorImgView);
            Glide.with(context)
                    .load(content.getString("content"))
                    .override(400, 400).centerCrop()
                    .into(holder.imageView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mAuthorImgView;
        public final ImageView imageView;
        public final TextView mAuthorView;

        public ImageViewHolder(View v) {
            super(v);
            mAuthorImgView = (ImageView) v.findViewById(R.id.author_img2);
            mAuthorView = (TextView) v.findViewById(R.id.author2);
            imageView = (ImageView) v.findViewById(R.id.upload_img);
        }

    }
}