package com.example.anubhav.modern.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.anubhav.modern.Adapters.HomeRecyclerAdapter;
import com.example.anubhav.modern.Models.PostItem;
import com.example.anubhav.modern.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Created by Anubhav on 25-08-2017.
 */

public class HomeFragment extends Fragment {
    final int uploadRequest = 90;
    HomeRecyclerAdapter homeRecyclerAdapter;
    RecyclerView homeRecyclerView;
    ArrayList<PostItem> homePostsArrayList;
    FloatingActionButton floatingActionButton;
    FirebaseFirestore db;
//    FirebaseDatabase firebaseDatabase;
//    DatabaseReference myPostRef;
//    DatabaseReference myUserRef;

    public HomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_recyclerview, container, false);
        homeRecyclerView = v.findViewById(R.id.homefragment_recyclerView);
        homePostsArrayList = new ArrayList<>();
        floatingActionButton = v.findViewById(R.id.fab);
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        db.collection("homeposts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("PERSISTENCE ERROR", e.getMessage());
                    return;
                }
                for (DocumentChange document : documentSnapshots.getDocumentChanges()) {
                    homePostsArrayList.add(document.getDocument().toObject(PostItem.class));
                    Log.i("ARRAYVALUECACHE", document.getDocument().toObject(PostItem.class).getDetails());
                    homeRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });
        getHomePosts();
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        myPostRef = firebaseDatabase.getReference("Posts");
//        firebaseDatabase.getReference("Users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
//
//                for (DataSnapshot child : children) {
//                    PostItem postItem = child.getValue(PostItem.class);
//                    homePostsArrayList.add(postItem);
//                }
//                homeRecyclerAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Snackbar.make(homeRecyclerView, "Check internet connection", Snackbar.LENGTH_LONG);
//            }
//        });

        final FragmentManager fragmentManager = getFragmentManager();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDestroy();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        fragmentManager.beginTransaction().replace(R.id.content, new UploaderFragment()).commit();
                    }
                });
                thread.start();
            }
        });


//        for (int i = 0; i < 8; i++) {
//            homePostsArrayList.add(new PostItem("date " + i, "Time " + i, "details " + i, "User " + i, null));
//        }
        homeRecyclerAdapter = new HomeRecyclerAdapter(getContext(), homePostsArrayList, new HomeRecyclerAdapter.HomeClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), "Handle the click", Toast.LENGTH_SHORT).show();
            }
        });

        homeRecyclerView.setAdapter(homeRecyclerAdapter);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        return v;
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
                                Log.i("ARRAYVALUE", document.toObject(PostItem.class).getDetails());
                            }
                        }
                        homeRecyclerAdapter.notifyDataSetChanged();
                    }
                });
    }
//    public void retrieveHomePostFromFirebase() {
//
//        myPostRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                homePostsArrayList.clear();
//                for (DataSnapshot postDataSnapshot : dataSnapshot.getChildren()) {
//                    homePostsArrayList.add(postDataSnapshot.getValue(PostItem.class));
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(getContext(), "Please Check Internet Connection...", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

}
