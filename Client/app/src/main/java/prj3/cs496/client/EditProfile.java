package prj3.cs496.client;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import java.io.File;


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
        Button btn = (Button) v.findViewById(R.id.name_btn);
        final EditText nameEdit = (EditText) v.findViewById(R.id.edit_name) ;
        String name = currentUser.getUsername();
        nameEdit.setHint(name);
        Glide.with(getContext())
                .load(currentUser.getPicture())
                .into(image);

        TextView useremail = (TextView)v.findViewById(R.id.edit_email_info);
        useremail.setText(currentUser.getEmail().toString());

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery,SELECT_PICTURE);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newname = nameEdit.getText().toString();
                if(newname == null || newname.isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(),"Enter new name",Toast.LENGTH_LONG).show();
                }else{
                    //send new name to db
                    Member currentUser = ((ChatApp) getActivity().getApplication()).getCurrentUser();
                    currentUser.setUsername(newname);
                    currentUser.save(new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            Log.d("EDITNAME", "SUCCESS");
                            Toast.makeText(getActivity().getApplicationContext(), "name is changed", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable t) {
                            Log.d("EDITNAME", "FAIL: " + t.getMessage());
                            Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        this.v = v;
        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        //After selecting profile image from gallery
        if( requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK && data != null){
            final ImageView img = (ImageView) v.findViewById(R.id.profile2);
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePath,null,null,null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            mMemberRepository.testMethod(new File(imagePath), new ObjectCallback<Member>() {
                @Override
                public void onSuccess(Member object) {
                    Glide.with(getContext())
                            .load(object.getPicture())
                            .into(img);
                    ((ChatApp) getActivity().getApplication()).setCurrentUser(object);
                }

                @Override
                public void onError(Throwable t) {

                }
            });
        }
    }

}
