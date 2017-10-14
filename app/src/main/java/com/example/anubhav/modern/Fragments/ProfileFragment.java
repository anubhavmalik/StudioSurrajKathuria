package com.example.anubhav.modern.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.anubhav.modern.Constants.ApplicationConstants;
import com.example.anubhav.modern.Models.UserItem;
import com.example.anubhav.modern.R;
import com.example.anubhav.modern.Visible.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Anubhav on 25-08-2017.
 */

public class ProfileFragment extends Fragment {
    final int RC_PHOTO_PICKER = 70;
    EditText nameTextView;
    EditText numberTextView;
    Uri selectedImageUri;
    CircleImageView circleImageView;
    ProgressBar mProgressbar;
    ListView mListView;
    Button saveButton, guestLoginButton;
    boolean imageChanged = false, nameChanged = false;
    ArrayList<String> mArrayList;
    ArrayAdapter<String> arrayAdapter;
    StorageReference userStorageRef;
    String currentImageURL;
    SharedPreferences mUserDetailsSharedPreferences;

    public ProfileFragment() {

    }

    public boolean isImageChanged() {
        return imageChanged;
    }

    public void setImageChanged(boolean imageChanged) {
        this.imageChanged = imageChanged;
    }

    public boolean isNameChanged() {
        return nameChanged;
    }

    public void setNameChanged(boolean nameChanged) {
        this.nameChanged = nameChanged;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PHOTO_PICKER) {
            if (resultCode == RESULT_OK) {
                selectedImageUri = data.getData();
                circleImageView.setImageURI(selectedImageUri);
                super.onActivityResult(requestCode, resultCode, data);

            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mUserDetailsSharedPreferences = getContext().getSharedPreferences(ApplicationConstants.loginStatePreferencesName, Context.MODE_PRIVATE);

        if (mUserDetailsSharedPreferences.getBoolean(ApplicationConstants.loginState, false)) {

            View v = inflater.inflate(R.layout.profilefragmentlayout, container, false);

            nameTextView = v.findViewById(R.id.profileName);
            numberTextView = v.findViewById(R.id.profileNumber);
            saveButton = v.findViewById(R.id.profile_save);
            circleImageView = v.findViewById(R.id.profile_image);
            mListView = v.findViewById(R.id.profile_listView);
            mProgressbar = v.findViewById(R.id.profile_fragment_progress);

            final InquiryFragment inquiryFragment = new InquiryFragment();

            numberTextView.setEnabled(false);
            enableViews(false);
            setHasOptionsMenu(true);


            currentImageURL = mUserDetailsSharedPreferences.getString(ApplicationConstants.userPhotoUrl, null);


            Glide.with(getContext().getApplicationContext())
                    .load(mUserDetailsSharedPreferences.getString(ApplicationConstants.userPhotoUrl, null))
                    .into(circleImageView);


            nameTextView.setText(mUserDetailsSharedPreferences.getString(ApplicationConstants.userName, null));
            numberTextView.setText(mUserDetailsSharedPreferences.getString(ApplicationConstants.phoneNumber, null));

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveButton.setEnabled(false);
                    if (mUserDetailsSharedPreferences.getString(ApplicationConstants.userName, null).equals(nameTextView.getText().toString()) && !isImageChanged()) {
                        Log.i("PROFILEUPDATION", "NO CHANGES");
                        enableViews(false);
                        return;
                    } else if (mUserDetailsSharedPreferences.getString(ApplicationConstants.userName, null).equals(nameTextView.getText().toString())) {
                        setNameChanged(false);
                        enableViews(false);
                    } else {
                        setNameChanged(true);
                        enableViews(false);
                    }

                    final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                    if (isNameChanged() && !isImageChanged()) {
                        Snackbar.make(saveButton, "Updating your details", Snackbar.LENGTH_SHORT).show();
                        firebaseFirestore.collection("users")
                                .document(mUserDetailsSharedPreferences.getString(ApplicationConstants.phoneNumber, null))
                                .update("name", nameTextView.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            mProgressbar.setVisibility(View.GONE);
                                            Snackbar.make(saveButton, "Updated Successfully", Snackbar.LENGTH_SHORT).setCallback(new Snackbar.Callback() {
                                                @Override
                                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                                    super.onDismissed(transientBottomBar, event);
                                                    mUserDetailsSharedPreferences.edit().putString(ApplicationConstants.userName, nameTextView.getText().toString()).apply();

                                                    getActivity().onBackPressed();
                                                }
                                            }).show();
                                            enableViews(false);
                                        }
                                    }
                                });
                    }


                    if (isImageChanged() && isNameChanged()) {
                        mProgressbar.setVisibility(View.VISIBLE);
                        Snackbar.make(saveButton, "Updating your details", Snackbar.LENGTH_SHORT).show();
                        userStorageRef = FirebaseStorage.getInstance().getReference().child(getString(R.string.userStorage));
                        userStorageRef.child(mUserDetailsSharedPreferences.getString(ApplicationConstants.userPhotoUrl, null))
                                .delete();
                        userStorageRef.child(mUserDetailsSharedPreferences
                                .getString(ApplicationConstants.phoneNumber, null) + selectedImageUri.getLastPathSegment())
                                .putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                final UserItem userItem = new UserItem(nameTextView.getText().toString(), task.getResult().getDownloadUrl().toString(), mUserDetailsSharedPreferences.getString(ApplicationConstants.phoneNumber, null));

                                firebaseFirestore.collection("users")
                                        .document(mUserDetailsSharedPreferences.getString(ApplicationConstants.phoneNumber, null))
                                        .set(userItem)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                mUserDetailsSharedPreferences.edit().putString(ApplicationConstants.userPhotoUrl, userItem.getPhotourl()).apply();
                                                mUserDetailsSharedPreferences.edit().putString(ApplicationConstants.userName, userItem.getName()).apply();
                                                mProgressbar.setVisibility(View.GONE);
                                                Snackbar.make(nameTextView, "Changes saved", Snackbar.LENGTH_SHORT).setCallback(new Snackbar.Callback() {
                                                    @Override
                                                    public void onDismissed(Snackbar transientBottomBar, int event) {
                                                        super.onDismissed(transientBottomBar, event);
                                                        getFragmentManager().popBackStack();

                                                    }
                                                }).show();

                                            }
                                        });
                                enableViews(false);
                            }

                        });

                    }
                    if (isImageChanged() && !isNameChanged()) {
                        mProgressbar.setVisibility(View.VISIBLE);
                        Snackbar.make(saveButton, "Updating your details", Snackbar.LENGTH_SHORT).show();
                        userStorageRef = FirebaseStorage.getInstance().getReference().child(getString(R.string.userStorage));
                        userStorageRef.child(mUserDetailsSharedPreferences.getString(ApplicationConstants.userPhotoUrl, null))
                                .delete();
                        userStorageRef.child(mUserDetailsSharedPreferences
                                .getString(ApplicationConstants.phoneNumber, null) + selectedImageUri.getLastPathSegment())
                                .putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                final UserItem userItem = new UserItem(nameTextView.getText().toString(), task.getResult().getDownloadUrl().toString(), mUserDetailsSharedPreferences.getString(ApplicationConstants.phoneNumber, null));

                                firebaseFirestore.collection("users")
                                        .document(mUserDetailsSharedPreferences.getString(ApplicationConstants.phoneNumber, null))
                                        .set(userItem)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                mUserDetailsSharedPreferences.edit().putString(ApplicationConstants.userPhotoUrl, userItem.getPhotourl()).apply();
                                                mProgressbar.setVisibility(View.GONE);
                                                Snackbar.make(nameTextView, "Changes saved", Snackbar.LENGTH_SHORT).setCallback(new Snackbar.Callback() {
                                                    @Override
                                                    public void onDismissed(Snackbar transientBottomBar, int event) {
                                                        super.onDismissed(transientBottomBar, event);
                                                        getFragmentManager().popBackStack();

                                                    }
                                                }).show();

                                            }

                                        });
                                enableViews(false);
                            }
                        });
                    }
                }
            });
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("image/*");

                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");

                    Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                    startActivityForResult(chooserIntent, RC_PHOTO_PICKER);
                    setImageChanged(true);

                }
            });


            return v;
        } else {
            View v = inflater.inflate(R.layout.guest_home, container, false);
            guestLoginButton = v.findViewById(R.id.home_login_button);

            guestLoginButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));


            guestLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), Login.class));
                }

            });


            return v;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.profile_edit) {
            enableViews(true);
        }

        return true;
    }


    private void enableViews(boolean b) {
        nameTextView.setEnabled(b);
        circleImageView.setEnabled(b);
        saveButton.setVisibility(b ? View.VISIBLE : View.GONE);
    }
}
