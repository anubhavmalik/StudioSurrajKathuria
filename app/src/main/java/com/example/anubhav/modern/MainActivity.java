package com.example.anubhav.modern;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.anubhav.modern.Constants.IntentConstants;
import com.example.anubhav.modern.Fragments.CatalogFragment;
import com.example.anubhav.modern.Fragments.HomeFragment;
import com.example.anubhav.modern.Fragments.ProfileFragment;
import com.example.anubhav.modern.Models.PostItem;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final int getDetailRequest = 140;
    HomeFragment homeFragment;
    CatalogFragment catalogFragment;
    ProfileFragment profileFragment;
    boolean startup;
    String phoneNumber;
    ArrayList<PostItem> homePostsArrayList;
    FirebaseFirestore db;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setTitle("Home");
                    setFragment(homeFragment);


                    return true;
                case R.id.navigation_catalog:
                    setTitle("Catalog");
                    setFragment(catalogFragment);

                    return true;
                case R.id.navigation_profile:
                    setTitle("Profile");
                    setFragment(profileFragment);


                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        startup = true;
        homePostsArrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);


        phoneNumber = getIntent().getStringExtra(IntentConstants.phoneNumberText);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        homeFragment = new HomeFragment();
        catalogFragment = new CatalogFragment();
        profileFragment = new ProfileFragment();


        setFragment(catalogFragment);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == getDetailRequest && resultCode == RESULT_OK) {
//            setFragment(homeFragment);
//        }
    }

    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (startup)
            fragmentTransaction.add(R.id.content, fragment);
        else
            fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
        startup = false;
    }


}
