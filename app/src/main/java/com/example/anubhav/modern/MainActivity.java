package com.example.anubhav.modern;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.anubhav.modern.Constants.IntentConstants;
import com.example.anubhav.modern.Firebase.RealTimeDatabase;
import com.example.anubhav.modern.Fragments.CatalogFragment;
import com.example.anubhav.modern.Fragments.HomeFragment;
import com.example.anubhav.modern.Models.PostItem;
import com.example.anubhav.modern.Models.UserItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    HomeFragment homeFragment;
    CatalogFragment catalogFragment;
    boolean startup;
    ArrayList<PostItem> postItemArrayList;
    UserItem user;


    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setTitle("Home");
                    setFragment(new HomeFragment());

                    return true;
                case R.id.navigation_catalog:
                    setTitle("Catalog");
                    setFragment(new CatalogFragment());

                    return true;
                case R.id.navigation_profile:
//                    TODO:Add fragment here
                    setTitle("Profile");


                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeFragment = new HomeFragment();
        catalogFragment = new CatalogFragment();
        setContentView(R.layout.activity_main);
        startup = true;
//        retrieveFromFirebase(); TODO: THIS FUNCTION SHOULD COPY ARRAY OF POST ITEMS AND THE CURRENT USER DETAILS TO PASS TO FRAGMENT
        getIntent().getStringExtra(IntentConstants.phoneNumberText);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setFragment(new HomeFragment());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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

    public void retrieveFromFirebase() {
        RealTimeDatabase database = new RealTimeDatabase();
        database.getMyPostRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postItemArrayList.clear();
                dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
