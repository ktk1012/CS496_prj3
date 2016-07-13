package prj3.cs496.client;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.strongloop.android.loopback.RestAdapter;


public class EditProfile extends Fragment {

    private RestAdapter mRestAdapter;
    private MemberRepository mMemberRepository;


    int SELECT_PICTURE = 200;
    private  View v;
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
        ImageView image = (ImageView) v.findViewById(R.id.profile2);

        Glide.with(getContext())
                .load(currentUser.getPicture())
                .into(image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery,SELECT_PICTURE);
            }
        });
        this.v = v;
        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if( requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK && data != null){
            ImageView img = (ImageView) v.findViewById(R.id.profile2);
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePath,null,null,null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            img.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        }
    }

}
