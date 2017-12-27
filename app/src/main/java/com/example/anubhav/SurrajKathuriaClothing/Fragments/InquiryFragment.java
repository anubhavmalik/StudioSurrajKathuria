package com.example.anubhav.SurrajKathuriaClothing.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.anubhav.SurrajKathuriaClothing.Constants.ApplicationConstants;
import com.example.anubhav.SurrajKathuriaClothing.Models.InquiryItem;
import com.example.anubhav.SurrajKathuriaClothing.Models.TimeFetcher;
import com.example.anubhav.SurrajKathuriaClothing.VisibleActivities.Login;
import com.example.anubhav.modern.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.victor.loading.newton.NewtonCradleLoading;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Anubhav on 13-10-2017.
 */

public class InquiryFragment extends Fragment {

    Button saveButton;
    EditText titleEditText, detailsEditText;
    FirebaseFirestore db;
    CircleImageView circleImageView;
    TextView nameTextView, contactTextView;
    Button guestLoginButton;
    NewtonCradleLoading newtonCradleLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        SharedPreferences mSharedPreferences = getContext().getSharedPreferences(ApplicationConstants.loginStatePreferencesName, Context.MODE_PRIVATE);

        if (mSharedPreferences.getBoolean(ApplicationConstants.loginState, false)) {
            View v = inflater.inflate(R.layout.enquiry_fragment, container, false);
            saveButton = v.findViewById(R.id.enquiryuploader_fab);
            titleEditText = v.findViewById(R.id.enquiry_titleEditText);
            detailsEditText = v.findViewById(R.id.enquiryuploader_editText);
            circleImageView = v.findViewById(R.id.enquiryuploader_circleImageView);
            newtonCradleLoading = v.findViewById(R.id.enquirynewton_cradle);
            nameTextView = v.findViewById(R.id.enquiryuploader_nameTextView);
            contactTextView = v.findViewById(R.id.contactforinquiry);

            final SharedPreferences sharedPreferences = getContext().getSharedPreferences(ApplicationConstants.loginStatePreferencesName, Context.MODE_PRIVATE);

            Glide.with(getActivity().getApplicationContext())
                    .load(sharedPreferences.getString(ApplicationConstants.userPhotoUrl, null))
                    .asBitmap()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(circleImageView);

            nameTextView.setText(sharedPreferences.getString(ApplicationConstants.userName, null));
            contactTextView.setText("Contact me on : " + sharedPreferences.getString(ApplicationConstants.phoneNumber, null));
            db = FirebaseFirestore.getInstance();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (titleEditText.getText().toString().isEmpty())
                        titleEditText.setError("Can't be left empty");

                    if (detailsEditText.getText().toString().isEmpty())
                        detailsEditText.setError("Can't be left empty");

                    else {
                        newtonCradleLoading.start();
                        newtonCradleLoading.setLoadingColor(getResources().getColor(R.color.colorPrimary));
                        newtonCradleLoading.setVisibility(View.VISIBLE);
                        TimeFetcher timeFetcher = new TimeFetcher();
                        InquiryItem inquiryItem = new InquiryItem(
                                sharedPreferences.getString(ApplicationConstants.userName, null)
                                , timeFetcher.getPhoneDate()
                                , detailsEditText.getText().toString()
                                , System.currentTimeMillis() + ""
                                , timeFetcher.getPhoneTime()
                                , titleEditText.getText().toString()
                                , sharedPreferences.getString(ApplicationConstants.phoneNumber, null));

                        db.collection("enquiries")
                                .add(inquiryItem)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        Snackbar.make(saveButton, "We will get back on your number within 48 hours", Snackbar.LENGTH_SHORT).show();
                                        newtonCradleLoading.setVisibility(View.GONE);
                                    }
                                });

                    }
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
}
