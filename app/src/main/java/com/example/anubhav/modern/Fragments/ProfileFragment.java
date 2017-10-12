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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.example.anubhav.modern.Constants.ApplicationConstants;
import com.example.anubhav.modern.Models.PostItem;
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
    ListView mListView;
    Button saveButton, guestLoginButton;
    boolean imageChanged = false, nameChanged = false;
    ArrayList<String> mArrayList;
    ArrayList<PostItem> updationArrayList, updationCopyArrayList;
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
                    if (mUserDetailsSharedPreferences.getString(ApplicationConstants.userName, null).equals(nameTextView.getText().toString()))
                        setNameChanged(false);
                    else if (!mUserDetailsSharedPreferences.getString(ApplicationConstants.userName, null).equals(nameTextView.getText().toString())) {
                        setNameChanged(true);
                    }

                    else if (isImageChanged() && isNameChanged()) {
                        Snackbar.make(saveButton, "Updating your details", Snackbar.LENGTH_SHORT).show();
                        userStorageRef = FirebaseStorage.getInstance().getReference().child(getString(R.string.userStorage));
                        userStorageRef.child(mUserDetailsSharedPreferences.getString(ApplicationConstants.userPhotoUrl, null))
                                .delete();
                        userStorageRef.child(mUserDetailsSharedPreferences
                                .getString(ApplicationConstants.phoneNumber, null) + selectedImageUri.getLastPathSegment())
                                .putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                UserItem userItem = new UserItem(nameTextView.getText().toString(), task.getResult().getDownloadUrl().toString(), mUserDetailsSharedPreferences.getString(ApplicationConstants.phoneNumber, null));
                                final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                firebaseFirestore.collection("users")
                                        .document(mUserDetailsSharedPreferences.getString(ApplicationConstants.phoneNumber, null))
                                        .set(userItem)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.i("USERURLEDIT", "photourl for user has been edited successfully");
                                            }
                                        });
//                                firebaseFirestore.collection("homeposts")
//                                        .orderBy("epoch", Query.Direction.DESCENDING)
//                                        .get()
//                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                if (task.isSuccessful()) {
//                                                    updationArrayList = new ArrayList<>();
//
//                                                    for (DocumentSnapshot document : task.getResult()) {
//                                                        updationArrayList.add(document.toObject(PostItem.class));
//                                                    }
//                                                }
//                                                for (int i = 0; i < updationArrayList.size(); i++) {
//                                                    if (updationArrayList.get(i).getUserImageURL().equals(mUserDetailsSharedPreferences.getString(currentImageURL, null))) {
//                                                        updationCopyArrayList.add(updationArrayList.get(i));
//                                                    }
//                                                }
//                                            }
//                                        });
//                                //TODO: UPDATE VALUE OF USER PHOTO URL FOR EACH POST


                                Snackbar.make(nameTextView, "Changes saved", Snackbar.LENGTH_SHORT).show();
                                mUserDetailsSharedPreferences.edit()
                                        .putString(ApplicationConstants.userPhotoUrl, task.getResult().getDownloadUrl().toString());
                            }
                        });

                    }

                }
            });
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/jpeg");
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
                    setImageChanged(true);
                }
            });

            mArrayList = new ArrayList<>();
            mArrayList.add("Reach Us");
            mArrayList.add("About Surraj Kathuria");
            mArrayList.add("About Vimmi Kathuria");
            arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mArrayList);
            mListView.setAdapter(arrayAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 0) {
                        //TODO : Layout for REACH US
                    } else if (i == 1) {
                        //TODO: Layout for about Surraj Kathuria
                    } else if (i == 2) {
                        //TODO: Layout for about Vimmi Kathuria
                    }
                }
            });
            return v;
        } else {
            View v = inflater.inflate(R.layout.guest_home, container, false);
//            enableViews(false);
            guestLoginButton = v.findViewById(R.id.home_login_button);
//            saveButton=v.findViewById(R.id.profile_save);

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
