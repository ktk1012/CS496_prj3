package prj3.cs496.client;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by q on 2016-07-13.
 */
public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {

    private ArrayList<ChatRoom> rooms;

    public ChatRoomAdapter() {
        this.rooms = new ArrayList<ChatRoom>();
    }

    @Override
    public int getItemCount() {return rooms.size();}

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ChatRoom room = rooms.get(position);
        holder.mNameView.setText(room.getName());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        v.setLayoutParams(lp);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mNameView;

        public ViewHolder(View v) {
            super(v);
            mNameView = (TextView) v.findViewById(R.id.name);
        }
    }

    public void updateAdapter(ArrayList<ChatRoom> list) {
        rooms.clear();
        rooms.addAll(list);
        this.notifyDataSetChanged();
    }
}
