package com.example.anubhav.modern.Fragments;

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

        return v;
    }
}
