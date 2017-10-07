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
import android.widget.Toast;

import com.example.anubhav.modern.Constants.IntentConstants;
import com.example.anubhav.modern.Fragments.CatalogFragment;
import com.example.anubhav.modern.Fragments.HomeFragment;
import com.example.anubhav.modern.Fragments.ProfileFragment;
import com.example.anubhav.modern.Models.PostItem;
import com.example.anubhav.modern.Models.UserItem;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    HomeFragment homeFragment;
    CatalogFragment catalogFragment;
    ProfileFragment profileFragment;
    boolean startup;
    String phoneNumber;
    UserItem user;
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
//        db.collection("homeposts").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
//                if(e!=null){
//                    Log.e("PERSISTENCE ERROR",e.getMessage());
//                    return;
//                }
//                for(DocumentChange document : documentSnapshots.getDocumentChanges()){
//                    homePostsArrayList.add(document.getDocument().toObject(PostItem.class));
//                    Log.i("ARRAYVALUECACHE", document.getDocument().toObject(PostItem.class).getDetails());
//
//                }
//            }
//        });

        phoneNumber = getIntent().getStringExtra(IntentConstants.phoneNumberText);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        homeFragment = new HomeFragment();
        catalogFragment = new CatalogFragment();
        profileFragment = new ProfileFragment();

//        getHomePosts();
//        if(fetchUserIfExists()){
        setFragment(homeFragment);
//        }
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

//    public void getHomePosts() {
//        db.collection("homeposts")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            homePostsArrayList.clear();
//
//                            for (DocumentSnapshot document : task.getResult()) {
//                                homePostsArrayList.add(document.toObject(PostItem.class));
//                                Log.i("ARRAYVALUE", document.toObject(PostItem.class).getDetails());
//                            }
//                        }
//                    }
//                });
//    }

    public boolean fetchUserIfExists() {
        if (db.collection("homeposts").document().equals(phoneNumber)) {
            return true;
        } else {
            Toast.makeText(this, "Please update your details first", Toast.LENGTH_SHORT).show();
            setFragment(profileFragment);
            return false;
        }

    }

}
