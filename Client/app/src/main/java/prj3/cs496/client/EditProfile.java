package prj3.cs496.client;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.strongloop.android.loopback.RestAdapter;


public class EditProfile extends Fragment {

    private RestAdapter mRestAdapter;
    private MemberRepository mMemberRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRestAdapter = new RestAdapter(getContext(), "http://52.78.69.111:3000/api");
        mMemberRepository = mRestAdapter.createRepository(MemberRepository.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_profile,container,false);
        Member currentUser = ((ChatApp) getActivity().getApplication()).getCurrentUser();
        ImageView imageView = (ImageView) v.findViewById(R.id.profile2);

        Glide.with(getContext())
                .load(currentUser.getPicture())
                .into(imageView);

        return v;
    }


    public void onClick_img(View v){
        Intent galleryIn = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIn,2013);
    }

}
