package com.example.anubhav.modern.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.example.anubhav.modern.Models.TimeFetcher;
import com.example.anubhav.modern.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.victor.loading.newton.NewtonCradleLoading;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Anubhav on 30-09-2017.
 */

public class HomeUploaderFragment extends Fragment {
    final int RC_PHOTO_PICKER = 26;
    EditText detailsEditText;
    FloatingActionButton uploaderFab;
    ImageView mPhotoPickerButton;
    TextView nameTextView;
    CircleImageView circleImageView;
    Uri selectedImageUri;
    FirebaseFirestore db;
    StorageReference homeStorageReference;
    SharedPreferences mPhoneNumberPreference;
    NewtonCradleLoading newtonCradleLoading;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PHOTO_PICKER) {
            if (resultCode == RESULT_OK) {
                selectedImageUri = data.getData();
                mPhotoPickerButton.setImageURI(selectedImageUri);
                super.onActivityResult(requestCode, resultCode, data);

            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_uploader, null);
        detailsEditText = v.findViewById(R.id.homeuploader_editText);
        circleImageView = v.findViewById(R.id.homeuploader_circleImageView);
        uploaderFab = v.findViewById(R.id.homeuploader_fab);
        nameTextView = v.findViewById(R.id.homeuploader_nameTextView);
        mPhotoPickerButton = v.findViewById(R.id.homeuploader_imageView);
        mPhoneNumberPreference = this.getActivity().getSharedPreferences(ApplicationConstants.loginStatePreferencesName, MODE_PRIVATE);

        newtonCradleLoading = v.findViewById(R.id.homenewton_cradle);
        newtonCradleLoading.setLoadingColor(getResources().getColor(R.color.colorPrimaryDark));
        showProgress(false);

        nameTextView.setText(mPhoneNumberPreference.getString(ApplicationConstants.userName, null));

        Glide.with(HomeUploaderFragment.this)
                .load(mPhoneNumberPreference.getString(ApplicationConstants.userPhotoUrl, null))
                .into(circleImageView);


        homeStorageReference = FirebaseStorage.getInstance().getReference().child(getString(R.string.homeImageStorage));
        uploaderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detailsEditText.getText().toString().isEmpty()) {
                    detailsEditText.setError("You need to write something");
                } else if (selectedImageUri == null) {
                    Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                } else {
                    enableViews(false);
                    Snackbar.make(mPhotoPickerButton, "Posting...", Snackbar.LENGTH_INDEFINITE).show();
                    showProgress(true);
                    newtonCradleLoading.start();

                    StorageReference photoRef = homeStorageReference.child(selectedImageUri.getLastPathSegment());

                    photoRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri url = taskSnapshot.getDownloadUrl();
                            db = FirebaseFirestore.getInstance();

                            TimeFetcher timeFetcher = new TimeFetcher();
                            PostItem postItem = new PostItem(timeFetcher.getPhoneDate(),
                                    timeFetcher.getPhoneTime(),
                                    detailsEditText.getText().toString(),
                                    mPhoneNumberPreference.getString(ApplicationConstants.userName, null),
                                    mPhoneNumberPreference.getString(ApplicationConstants.userPhotoUrl, null),
                                    url.toString(),
                                    System.currentTimeMillis() + "");
                            db.collection("homeposts").add(postItem).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    showProgress(false);
                                    Snackbar.make(mPhotoPickerButton, "You Posted Successfully", Snackbar.LENGTH_SHORT).show();
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            showProgress(false);
                                            enableViews(true);
                                            Snackbar.make(mPhotoPickerButton, "Oops! Something went wrong.", Snackbar.LENGTH_SHORT).show();
                                        }
                                    });
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

    private void enableViews(boolean status) {
        mPhotoPickerButton.setEnabled(status);
        detailsEditText.setEnabled(status);
        circleImageView.setEnabled(status);

    }

    private void showProgress(boolean status) {
        newtonCradleLoading.setVisibility(status ? View.VISIBLE : View.GONE);
    }


}
