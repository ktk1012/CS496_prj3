package prj3.cs496.client;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;




public class EditProfile extends Fragment {




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_edit_profile,container,false);
        return v;
    }


    public void onClick_img(View v){
        Intent galleryIn = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIn,2013);
    }

}
