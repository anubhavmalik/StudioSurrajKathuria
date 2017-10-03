package com.example.anubhav.modern.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anubhav.modern.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Anubhav on 30-09-2017.
 */

public class UploaderFragment extends Fragment {
    final int RC_PHOTO_PICKER = 26;
    EditText detailsEditText;
    FloatingActionButton uploaderFab;
    ImageView mPhotoPickerButton;
    TextView nameTextView;
    CircleImageView circleImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_uploader, null);
        detailsEditText = v.findViewById(R.id.uploader_editText);
        circleImageView = v.findViewById(R.id.uploader_circleImageView);
        uploaderFab = v.findViewById(R.id.uploader_fab);
        nameTextView = v.findViewById(R.id.uploader_nameTextView);
        mPhotoPickerButton = v.findViewById(R.id.uploader_imageView);

        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        return v;
    }

}
