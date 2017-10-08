package com.example.anubhav.modern.Visible;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.anubhav.modern.Constants.ApplicationConstants;
import com.example.anubhav.modern.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import de.hdodenhof.circleimageview.CircleImageView;

public class GetDetailsActivity extends AppCompatActivity {

    final int RC_PHOTO_PICKER = 26;
    EditText nameEditText;
    EditText numberEditText;
    ImageView editPhotoImageView;
    CircleImageView circleImageView;
    Button saveButton;
    SharedPreferences mPhoneSharedPreferences;
    FirebaseFirestore db;
    Uri selectedImageUri;
    FirebaseStorage mFirebaseStorage;
    boolean imageUploaded;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_details);

        nameEditText = findViewById(R.id.profile_activity_Name);
        numberEditText = findViewById(R.id.profile_activity_Number);
        editPhotoImageView = findViewById(R.id.edit_activity_profilephoto);
        circleImageView = findViewById(R.id.profile_activity_image);
        saveButton = findViewById(R.id.profile_activity_save);
        mPhoneSharedPreferences = this.getSharedPreferences(ApplicationConstants.phonePreferencesName, MODE_PRIVATE);
        phoneNumber = mPhoneSharedPreferences.getString(ApplicationConstants.phoneNumber, null);
        nameEditText.setText(phoneNumber);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageUploaded && !nameEditText.getText().toString().isEmpty()) {
                    //TODO: CLOUD STORAGE HERE AND STORE URL IN FIRESTORE
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
            }
        }
    }
}
