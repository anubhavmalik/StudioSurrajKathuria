package com.example.anubhav.SurrajKathuriaClothing;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anubhav.SurrajKathuriaClothing.Adapters.CatalogRecyclerAdapter;
import com.example.anubhav.SurrajKathuriaClothing.Adapters.HomeRecyclerAdapter;
import com.example.anubhav.SurrajKathuriaClothing.Fragments.CatalogFragment;
import com.example.anubhav.SurrajKathuriaClothing.Fragments.HomeFragment;
import com.example.anubhav.SurrajKathuriaClothing.Fragments.InquiryFragment;
import com.example.anubhav.SurrajKathuriaClothing.Fragments.ProfileFragment;
import com.example.anubhav.SurrajKathuriaClothing.Helpers.BottomNavigationHelper;
import com.example.anubhav.SurrajKathuriaClothing.Models.PostItem;
import com.example.anubhav.modern.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements HomeRecyclerAdapter.HomeClickListener, CatalogRecyclerAdapter.CatalogClickListener {
    HomeFragment homeFragment;
    CatalogFragment catalogFragment;
    ProfileFragment profileFragment;
    InquiryFragment inquiryFragment;
    boolean startup;
    ArrayList<PostItem> homePostsArrayList;
    BottomNavigationView navigation;
    FirebaseFirestore db;
    private boolean doubleBackToExitPressedOnce;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            BottomNavigationHelper.disableShiftMode(navigation);


            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setTitle("Home");
                    setFragment(homeFragment);


                    return true;
                case R.id.navigation_catalog:
                    setTitle("The Catalog");
                    setFragment(catalogFragment);

                    return true;
                case R.id.navigation_profile:
                    setTitle("Profile");
                    setFragment(profileFragment);


                    return true;

                case R.id.navigation_inquiry:
                    setTitle("Send Inquiry");
                    setFragment(inquiryFragment);


                    return true;
            }
            return false;
        }

    };

    @Override
    public void onBackPressed() {


        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press one more time to exit.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        startup = true;
        homePostsArrayList = new ArrayList<>();
        navigation = findViewById(R.id.navigation);
//        navigation.enable
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

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

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        homeFragment = new HomeFragment();
        catalogFragment = new CatalogFragment();
        profileFragment = new ProfileFragment();
        inquiryFragment = new InquiryFragment();


//        homeFragment.setHomeItemLongClickListener(this);

        navigation.setSelectedItemId(R.id.navigation_catalog);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (startup)
            fragmentTransaction.add(R.id.content, fragment);
        else
            fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.addToBackStack("Main");
        fragmentTransaction.commit();
        startup = false;
    }


    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onHomeItemClick(View view, int position) {

    }
}


