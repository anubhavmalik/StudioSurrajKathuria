package com.example.anubhav.SurrajKathuriaClothing.VisibleActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anubhav.SurrajKathuriaClothing.Constants.ApplicationConstants;
import com.example.anubhav.SurrajKathuriaClothing.MainActivity;
import com.example.anubhav.SurrajKathuriaClothing.Models.UserItem;
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

    private static final int RC_PHOTO_PICKER = 26;
    private static final int REQUEST_READ_PERMISSION = 696;
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

        String activityLabel = "Tell us more";

        TextView tv = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setText("Welcome");
        tv.setTextSize(20);
        tv.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface tf = Typeface.createFromAsset(getAssets(), "Font/BilboSwashCaps-Regular.otf");
        tv.setTypeface(tf);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(tv);


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
        mProgressBar.setLoadingColor(getResources().getColor(R.color.colorPrimary));
        showProgress(false);


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
                    saveButton.setEnabled(false);
                    nameEditText.setEnabled(false);
                    circleImageView.setEnabled(false);
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
//                                            mPhoneSharedPreferences.edit().putBoolean(ApplicationConstants.firstLogin, false).apply();
                                            mPhoneSharedPreferences.edit().putBoolean(ApplicationConstants.loginState, true).apply();
                                            mPhoneSharedPreferences.edit().putString(ApplicationConstants.userName, userItem.getName()).apply();
                                            mPhoneSharedPreferences.edit().putString(ApplicationConstants.userPhotoUrl, url.toString()).apply();

                                            startActivity(new Intent(GetDetailsActivity.this, MainActivity.class));
                                            finish();

                                        }

                                    });
                        }
                    });
                } else {
                    Snackbar.make(saveButton, "Please fill all details and select an image", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openImagePicker() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, RC_PHOTO_PICKER);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

            }
        }
    }

    private void showProgress(boolean status) {
        mProgressBar.setVisibility(status ? View.VISIBLE : View.GONE);
    }

    public void onClick(View v){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
        } else {
            openImagePicker();
        }
    }
}
