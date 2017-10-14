package com.example.anubhav.modern.Visible;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anubhav.modern.Constants.ApplicationConstants;
import com.example.anubhav.modern.MainActivity;
import com.example.anubhav.modern.Models.UserItem;
import com.example.anubhav.modern.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.victor.loading.newton.NewtonCradleLoading;

import de.hdodenhof.circleimageview.CircleImageView;

public class GetDetailsActivity extends AppCompatActivity {

    final int RC_PHOTO_PICKER = 26;
    EditText nameEditText;
    TextView numberEditText;
    ImageView editPhotoImageView;
    CircleImageView circleImageView;
    Button saveButton;
    SharedPreferences mPhoneSharedPreferences;
    FirebaseFirestore db;
    Uri selectedImageUri;
    FirebaseStorage mFirebaseStorage;
    StorageReference mUserStorageReference, mStorageReference;
    boolean imageSelected = false;
    String phoneNumber;
    NewtonCradleLoading mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_details);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mPhoneSharedPreferences = this.getSharedPreferences(ApplicationConstants.loginStatePreferencesName, MODE_PRIVATE);
        if (!mPhoneSharedPreferences.getBoolean(ApplicationConstants.firstLogin, true)) {
            startActivity(new Intent(GetDetailsActivity.this, MainActivity.class));
            finish();
        }
        nameEditText = findViewById(R.id.profile_activity_Name);
        numberEditText = findViewById(R.id.profile_activity_Number);
        editPhotoImageView = findViewById(R.id.edit_activity_profilephoto);
        circleImageView = findViewById(R.id.profile_activity_image);
        mProgressBar = findViewById(R.id.newtonian_loading);
        mProgressBar.setLoadingColor(getResources().getColor(R.color.colorPrimaryDark));
        showProgress(false);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/jpeg");
//                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, RC_PHOTO_PICKER);
            }

        });
        saveButton = findViewById(R.id.profile_activity_save);

        phoneNumber = mPhoneSharedPreferences.getString(ApplicationConstants.phoneNumber, null);
        numberEditText.setText(phoneNumber);

        mFirebaseStorage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();


        mStorageReference = mFirebaseStorage.getReference();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageSelected && !nameEditText.getText().toString().isEmpty()) {
                    mUserStorageReference = mStorageReference.child(getString(R.string.userStorage)).child(phoneNumber + "profile_image");
                    showProgress(true);
                    mProgressBar.start();
                    Toast.makeText(GetDetailsActivity.this, "Please wait, this will take a while.", Toast.LENGTH_LONG).show();
                    mUserStorageReference.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final Uri url = taskSnapshot.getDownloadUrl();
                            final UserItem userItem = new UserItem(nameEditText.getText().toString(), url.toString(), mPhoneSharedPreferences.getString(ApplicationConstants.phoneNumber, null));
                            Toast.makeText(GetDetailsActivity.this, "Registering you in our awesome client community", Toast.LENGTH_LONG).show();
                            db.collection("users")
                                    .document(mPhoneSharedPreferences.getString(ApplicationConstants.phoneNumber, null))
                                    .set(userItem)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            showProgress(false);
                                            mPhoneSharedPreferences.edit().putBoolean(ApplicationConstants.firstLogin, false).apply();
                                            mPhoneSharedPreferences.edit().putBoolean(ApplicationConstants.loginState, true).apply();
                                            mPhoneSharedPreferences.edit().putString(ApplicationConstants.userName, userItem.getName()).apply();
                                            mPhoneSharedPreferences.edit().putString(ApplicationConstants.userPhotoUrl, url.toString()).apply();

                                            startActivity(new Intent(GetDetailsActivity.this, MainActivity.class));
                                            finish();

                                            Snackbar.make(saveButton, "Congratulations you are now registered !", Snackbar.LENGTH_SHORT).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                                @Override
                                                public void onDismissed(Snackbar transientBottomBar, int event) {

                                                }
                                            }).show();
                                        }
                                    });
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_PHOTO_PICKER) {
            if (resultCode == RESULT_OK) {
                selectedImageUri = data.getData();
                circleImageView.setImageURI(selectedImageUri);
                imageSelected = true;
            }
        }
    }

    private void showProgress(boolean status) {
        mProgressBar.setVisibility(status ? View.VISIBLE : View.GONE);
    }
}
