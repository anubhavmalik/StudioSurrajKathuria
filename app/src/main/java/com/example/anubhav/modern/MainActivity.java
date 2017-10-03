package com.example.anubhav.modern;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.anubhav.modern.Constants.IntentConstants;
import com.example.anubhav.modern.Fragments.CatalogFragment;
import com.example.anubhav.modern.Fragments.HomeFragment;
import com.example.anubhav.modern.Fragments.ProfileFragment;
import com.example.anubhav.modern.Models.PostItem;
import com.example.anubhav.modern.Models.UserItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    HomeFragment homeFragment;
    CatalogFragment catalogFragment;
    ProfileFragment profileFragment;
    boolean startup;
    ArrayList<PostItem> postItemArrayList;
    UserItem user;
    String phoneNumber;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myPostRef;
    DatabaseReference myUserRef;
    DatabaseReference myCatalogRef;
    SharedPreferences mSharedPreferences;

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

        //TODO: THIS FUNCTION SHOULD COPY ARRAY OF POST ITEMS AND THE CURRENT USER DETAILS TO PASS TO FRAGMENT
        phoneNumber = getIntent().getStringExtra(IntentConstants.phoneNumberText);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myPostRef = firebaseDatabase.getReference("Posts");

        myUserRef = firebaseDatabase.getReference("Users");

        myCatalogRef = firebaseDatabase.getReference("Catalog");
        retrieveHomePostFromFirebase();
        retrieveUserFromFirebase();
        retrieveCatalogPostFromFirebase();
        homeFragment = new HomeFragment();
        catalogFragment = new CatalogFragment();
        profileFragment = new ProfileFragment();

        setFragment(homeFragment);
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

    public void retrieveHomePostFromFirebase() {

        myPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postItemArrayList.clear();
                for (DataSnapshot postDataSnapshot : dataSnapshot.getChildren()) {
                    postItemArrayList.add(postDataSnapshot.getValue(PostItem.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void retrieveUserFromFirebase() {
        myUserRef.child(phoneNumber).addValueEventListener((new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(UserItem.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }));
    }

    public void retrieveCatalogPostFromFirebase() {
        myCatalogRef.addValueEventListener((new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }));
    }
}
