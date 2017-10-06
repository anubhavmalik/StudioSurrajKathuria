package com.example.anubhav.modern;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.anubhav.modern.Constants.IntentConstants;
import com.example.anubhav.modern.Fragments.CatalogFragment;
import com.example.anubhav.modern.Fragments.HomeFragment;
import com.example.anubhav.modern.Fragments.ProfileFragment;
import com.example.anubhav.modern.Models.PostItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
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

        getHomePosts();


        phoneNumber = getIntent().getStringExtra(IntentConstants.phoneNumberText);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        homeFragment = new HomeFragment();
        catalogFragment = new CatalogFragment();
        profileFragment = new ProfileFragment();

        setFragment(homeFragment);
        BottomNavigationView navigation = findViewById(R.id.navigation);
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

    public void getHomePosts() {
        db.collection("homeposts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            homePostsArrayList.clear();

                            for (DocumentSnapshot document : task.getResult()) {
                                homePostsArrayList.add(document.toObject(PostItem.class));
                                Log.d("ARRAYVALUE", document.toObject(PostItem.class).getDetails());
                            }
                        }
                    }
                });
    }

}
