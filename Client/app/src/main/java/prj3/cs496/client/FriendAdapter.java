package prj3.cs496.client;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Member> people;

    public FriendAdapter(Context context, ArrayList<Member> people){
        this.context = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.people = people;
    }

    @Override
    public int getItemCount() {
        return people.size();
    }




    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Member member = people.get(position);
        holder.mNameView.setText(member.getEmail());
        Glide.with(context)
                .load(member.getPicture_thumb())
                .into(holder.mImageView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.person,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final ImageView mImageView;
       // public final ImageView mImageView;

        public ViewHolder(View view) {
            super (view);
            mView = view;

            mImageView = (ImageView) view.findViewById(R.id.profile);
            mNameView = (TextView) view.findViewById(R.id.name);
            //mImageView = (ImageView) view.findViewById(R.id.img);
        }
    }

    public void updateAdapter(ArrayList<Member> list) {
        people.clear();
        people.addAll(list);
        this.notifyDataSetChanged();
    }
}
