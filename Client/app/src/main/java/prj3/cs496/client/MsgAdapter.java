package prj3.cs496.client;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by q on 2016-07-14.
 */
public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

    private ArrayList<Message> msgs;

    public MsgAdapter() {
        this.msgs = new ArrayList<Message>();
    }

    @Override
    public int getItemCount() {return msgs.size();}

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Message msg = msgs.get(position);
        holder.mChatView.setText(msg.getContent().getContent());
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

    public void updateAdapter(ArrayList<Message> list) {
        msgs.clear();
        msgs.addAll(list);
        this.notifyDataSetChanged();
    }

    public Message getMessage(int position) {
        return msgs.get(position);
    }
}