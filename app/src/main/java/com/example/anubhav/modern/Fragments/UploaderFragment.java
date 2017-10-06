package com.example.anubhav.modern.Fragments;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.example.anubhav.modern.Models.PostItem;
import com.example.anubhav.modern.R;
import com.example.anubhav.modern.Visible.Login;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Anubhav on 30-09-2017.
 */

public class UploaderFragment extends Fragment {
    final int RC_PHOTO_PICKER = 26;
    EditText detailsEditText;
    FloatingActionButton uploaderFab;
    ImageView mPhotoPickerButton;
    TextView nameTextView;
    FirebaseStorage mFirebaseStorage;
    CircleImageView circleImageView;
    Uri selectedImageUri;
    FirebaseDatabase mFirebaseDatabase;
    StorageReference homeReference;
    DatabaseReference mPostReference;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_PHOTO_PICKER) {
            if (resultCode == RESULT_OK) {
                selectedImageUri = data.getData();
                mPhotoPickerButton.setImageURI(selectedImageUri);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_uploader, null);
        detailsEditText = v.findViewById(R.id.uploader_editText);
        circleImageView = v.findViewById(R.id.uploader_circleImageView);
        uploaderFab = v.findViewById(R.id.uploader_fab);
        nameTextView = v.findViewById(R.id.uploader_nameTextView);
        mPhotoPickerButton = v.findViewById(R.id.uploader_imageView);

        mFirebaseDatabase = FirebaseDatabase.getInstance();


        homeReference = FirebaseStorage.getInstance().getReference().child("homeimagesheresurrajkathuriaphotosfirebasestorage");
        uploaderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detailsEditText.getText().toString().isEmpty()) {
                    detailsEditText.setError("You need to write something");
                } else if (selectedImageUri == null) {
                    Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                } else {//TODO: ADD DATABASE UPLOADING CODE HERE


                    StorageReference photoRef = homeReference.child(selectedImageUri.getLastPathSegment());

                    photoRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri url = taskSnapshot.getDownloadUrl();

                            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                            PostItem postItem = new PostItem(currentDateTimeString.toString().substring(0, 11),
                                    currentDateTimeString.toString().substring(12, 16) + " " + currentDateTimeString.toString().substring(20, 22),
                                    detailsEditText.getText().toString(),
                                    new Login().mphoneNumberSharedPreferences.getString("phone_number", null),
                                    null,
                                    url.toString());


                        }
                    });
                }
            }
        });

        mFirebaseStorage = FirebaseStorage.getInstance();

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
