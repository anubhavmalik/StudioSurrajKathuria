package com.example.anubhav.modern.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.anubhav.modern.Adapters.HomeRecyclerAdapter;
import com.example.anubhav.modern.Constants.ApplicationConstants;
import com.example.anubhav.modern.Models.PostItem;
import com.example.anubhav.modern.Models.UserItem;
import com.example.anubhav.modern.R;
import com.example.anubhav.modern.Visible.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Created by Anubhav on 25-08-2017.
 */

public class HomeFragment extends Fragment {
    HomeRecyclerAdapter homeRecyclerAdapter;
    RecyclerView homeRecyclerView;
    ArrayList<PostItem> homePostsArrayList;
    SharedPreferences mUserSharedPreferences;
    FloatingActionButton floatingActionButton;
    Button guestLoginButton;
    ArrayList<UserItem> userItems;
    ArrayList<String> usersInOrderOfPosts;
    FirebaseFirestore db;
    ProgressBar mProgressBar;


    public HomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mUserSharedPreferences = getContext().getSharedPreferences(ApplicationConstants.loginStatePreferencesName, Context.MODE_PRIVATE);


        if (mUserSharedPreferences.getBoolean(ApplicationConstants.loginState, false)) {
            View v = inflater.inflate(R.layout.home_recyclerview, container, false);
            homeRecyclerView = v.findViewById(R.id.homefragment_recyclerView);
            mProgressBar = v.findViewById(R.id.home_fragment_progress);
            homePostsArrayList = new ArrayList<>();
            userItems = new ArrayList<>();
            usersInOrderOfPosts = new ArrayList<>();
            floatingActionButton = v.findViewById(R.id.fab);
            db = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build();
            db.setFirestoreSettings(settings);

//            getHomePosts();
//            getUsers();
            db.collection("homeposts")
                    .orderBy("epoch", Query.Direction.DESCENDING)
                    .limit(150)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    if (e != null) {
                        return;
                    }
                    homePostsArrayList.clear();
                    for (DocumentChange document : documentSnapshots.getDocumentChanges()) {
                        homePostsArrayList.add(document.getDocument().toObject(PostItem.class));
                    }

//                    Collections.reverse(homePostsArrayList);
                    mProgressBar.setVisibility(View.GONE);
                    homeRecyclerAdapter.notifyDataSetChanged();
                }
            });
//            db.collection("users")
//                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                        @Override
//                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
//                            if (e != null) {
//                                return;
//                            }
//                            userItems.clear();
//                            for (DocumentChange document : documentSnapshots.getDocumentChanges()) {
//                                userItems.add(document.getDocument().toObject(UserItem.class));
//                            }
//                        }
//                    });
            getHomePosts();
//            getUsers();

            final FragmentManager fragmentManager = getFragmentManager();


            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            fragmentManager.beginTransaction().replace(R.id.content, new HomeUploaderFragment()).commit();

                        }
                    });
                    thread.start();
                }
            });


            homeRecyclerAdapter = new HomeRecyclerAdapter(getContext(), homePostsArrayList, new HomeRecyclerAdapter.HomeClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getContext(), "Handle the click", Toast.LENGTH_SHORT).show();
                }
            });

            homeRecyclerView.setAdapter(homeRecyclerAdapter);
            homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

            return v;
        } else {
            //TODO: ADD A LAYOUT HERE TO INFLATE IF USER IS NOT SIGNED IN.
            View v = inflater.inflate(R.layout.guest_home, container, false);
            guestLoginButton = v.findViewById(R.id.home_login_button);
            guestLoginButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            guestLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), Login.class));
                }
            });

            return v;
        }
    }


    public void getHomePosts() {
        db.collection("homeposts")
                .orderBy("epoch", Query.Direction.DESCENDING)
                .limit(150)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            homePostsArrayList.clear();

                            for (DocumentSnapshot document : task.getResult()) {
                                homePostsArrayList.add(document.toObject(PostItem.class));
                            }
                        }
//                        Collections.reverse(homePostsArrayList);

                        mProgressBar.setVisibility(View.GONE);
                        homeRecyclerAdapter.notifyDataSetChanged();
                    }
                });
    }

//    public void getUsers(){
//        db.collection("users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            for( DocumentSnapshot documentSnapshot : task.getResult()){
//                                userItems.add(documentSnapshot.toObject(UserItem.class));
//                            }
//                        }
//                    }
//                });
//    }
//    public void sortUsers(){
//        int position;
//        for(int i=0;i<homePostsArrayList.size();i++){
//            usersInOrderOfPosts.add(homePostsArrayList.get(i).getBy_userNumber());
//        }
//        userItems.get(usersInOrderOfPosts.)
//    }


}
