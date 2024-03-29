package com.example.anubhav.SurrajKathuriaClothing.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.anubhav.SurrajKathuriaClothing.Adapters.HomeRecyclerAdapter;
import com.example.anubhav.SurrajKathuriaClothing.Constants.ApplicationConstants;
import com.example.anubhav.SurrajKathuriaClothing.Models.PostItem;
import com.example.anubhav.SurrajKathuriaClothing.Models.UserItem;
import com.example.anubhav.SurrajKathuriaClothing.VisibleActivities.Login;
import com.example.anubhav.modern.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
//    HomeListLongClickListener mListener;

    //    interface HomeListLongClickListener{
//
//        void HomeItemLongClicked(PostItem postItem);
//    }
//
//
//    public void setHomeItemLongClickListener (HomeListLongClickListener listener){
//        mListener=listener;
//    }
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
            mProgressBar.setVisibility(View.VISIBLE);
            userItems = new ArrayList<>();
            usersInOrderOfPosts = new ArrayList<>();
            floatingActionButton = v.findViewById(R.id.fab);
            db = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build();
            db.setFirestoreSettings(settings);


            db.collection("homeposts")
                    .orderBy("epoch", Query.Direction.DESCENDING)
                    .limit(250)
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

                    mProgressBar.setVisibility(View.GONE);
                    homeRecyclerAdapter.notifyDataSetChanged();
                }
            });

            getHomePosts();

            final FragmentManager fragmentManager = getFragmentManager();


            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentManager.beginTransaction().add(R.id.content, new HomeUploaderFragment(), "home uploader fragment called").addToBackStack("home uploader fragment called").commit();
                }
            });



            homeRecyclerAdapter = new HomeRecyclerAdapter(getContext(), homePostsArrayList, new HomeRecyclerAdapter.HomeClickListener() {
                @Override
                public void onHomeItemClick(View view, final int position) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    if (homePostsArrayList.get(position).getBy_userNumber() == mUserSharedPreferences.getString(ApplicationConstants.phoneNumber, null)) {
                        builder.setCancelable(true);
                        builder.setMessage("Are you sure you want to delete this post ?");
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.setPositiveButton("Delete Post", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.collection("homeposts")
                                        .whereEqualTo("epoch", homePostsArrayList.get(position).getEpoch())
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot documentSnapshots) {
                                                documentSnapshots.getDocuments().remove(homePostsArrayList.get(position));
                                            }
                                        });
                            }
                        }).show();
                    }
//                    }
                }
            });

            homeRecyclerView.setAdapter(homeRecyclerAdapter);
            homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

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


    public void getHomePosts() {
        db.collection("homeposts")
                .orderBy("epoch", Query.Direction.DESCENDING)
                .limit(250)
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
                        mProgressBar.setVisibility(View.GONE);
                        homeRecyclerAdapter.notifyDataSetChanged();
                    }
                });
    }


    public void onHomeItemClick(View view, final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (homePostsArrayList.get(position).getBy_userNumber() == mUserSharedPreferences.getString(ApplicationConstants.phoneNumber, null)) {
            builder.setCancelable(true);
            builder.setMessage("Are you sure you want to delete this post ?");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setPositiveButton("Delete Post", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    db.collection("homeposts")
                            .whereEqualTo("epoch", homePostsArrayList.get(position).getEpoch())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot documentSnapshots) {
                                    documentSnapshots.getDocuments().remove(homePostsArrayList.get(position));
                                }
                            });
                }
            });
        }
    }
}
