package com.example.anubhav.modern.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.bumptech.glide.Glide;
import com.example.anubhav.modern.Constants.ApplicationConstants;
import com.example.anubhav.modern.Models.PostItem;
import com.example.anubhav.modern.R;
import com.example.anubhav.modern.Visible.Login;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

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
    FirebaseFirestore db;
    StorageReference homeReference;
    DatabaseReference mPostReference;
    SharedPreferences mPhoneNumberPreference;

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
        mPhoneNumberPreference = this.getActivity().getSharedPreferences(ApplicationConstants.phonePreferencesName, MODE_PRIVATE);
        final String phoneNumber = mPhoneNumberPreference.getString(ApplicationConstants.phoneNumber, "123488535");
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
//        Task<DocumentSnapshot> task = db.collection("users").document(phoneNumber).get();
//        Glide.with(UploaderFragment.this)
//                .load(task.getResult().toObject(UserItem.class).getPhotourl())
//                .into(circleImageView);


        db.collection("users").document(phoneNumber).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(getContext(), "Some Error Occurred", Toast.LENGTH_SHORT).show();
                    return;
                }

                Glide.with(UploaderFragment.this)
                        .load("gs://modern-9526d.appspot.com/homeimagesheresurrajkathuriaphotosfirebasestorage/image:155928")
//                        .load(documentSnapshot.toObject(UserItem.class).getPhotourl())
                        .into(circleImageView);

            }

        });

//              .addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
//                if(e!=null){
//                    Toast.makeText(getContext(), "Some Error Occurred", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                for(DocumentChange change : documentSnapshots.getDocumentChanges()){
//                    Glide.with(UploaderFragment.this)
//                                .load(change.getDocument().toObject(UserItem.class).getPhotourl())
//                                .into(circleImageView);
//                    }
//                }
//        });


        homeReference = FirebaseStorage.getInstance().getReference().child(getString(R.string.homeImageStorage));
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
                                    new Login().mPhoneNumberSharedPreferences.getString("phone_number", null),
                                    null,
                                    url.toString());


                        }
                    });
                }
            }
        });

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
