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
import android.widget.Toast;

import com.example.anubhav.modern.Constants.IntentConstants;
import com.example.anubhav.modern.Fragments.CatalogFragment;
import com.example.anubhav.modern.Fragments.HomeFragment;
import com.example.anubhav.modern.Fragments.ProfileFragment;
import com.example.anubhav.modern.Models.PostItem;
import com.example.anubhav.modern.Models.UserItem;
import com.example.anubhav.modern.Visible.GetDetailsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
//        if (fetchUserIfExists()) {
        setFragment(catalogFragment);
//        } else {
//            startActivityForResult(new Intent(MainActivity.this, GetDetailsActivity.class), getDetailRequest);
//        }
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == getDetailRequest && resultCode == RESULT_OK) {
            setFragment(homeFragment);
        }
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

    public void fetchUserIfExists() {
        DocumentReference docRef = db.collection("users").document(phoneNumber);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot == null) {
                        startActivity(new Intent(MainActivity.this, GetDetailsActivity.class));
                        finish();
                    } else {
                        setFragment(catalogFragment);
                    }
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
