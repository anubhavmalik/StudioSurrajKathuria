package com.example.anubhav.modern.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.anubhav.modern.Adapters.CatalogRecyclerAdapter;
import com.example.anubhav.modern.Models.CatalogItem;
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
import java.util.List;

/**
 * Created by Anubhav on 25-08-2017.
 */

public class CatalogFragment extends Fragment {

    RecyclerView catalogRecyclerView;
    List<CatalogItem> catalogPostItemarrayList;
    CatalogRecyclerAdapter catalogRecyclerAdapter;
    FirebaseFirestore db;

    //    DatabaseReference myUserRef;
//    FirebaseDatabase firebaseDatabase;
//    DatabaseReference myCatalogRef;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.catalog_recyclerview, container, false);
        catalogRecyclerView = v.findViewById(R.id.catalog_recyclerViewList);
        catalogPostItemarrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        db.collection("catalogposts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("PERSISTENCE ERROR", e.getMessage());
                    return;
                }
                for (DocumentChange document : documentSnapshots.getDocumentChanges()) {
                    catalogPostItemarrayList.add(document.getDocument().toObject(CatalogItem.class));
                    Log.i("CATALOGARRAYVALUECACHE", document.getDocument().toObject(CatalogItem.class).getDetails());
                    catalogRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });
        getCatalogPosts();
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        myUserRef = firebaseDatabase.getReference().child("Users");
//        /*myCatalogRef* =*/
//        firebaseDatabase.getReference().child("Catalog").addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                catalogPostItemarrayList.clear(); //clear existing data
//                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
//
//                for (DataSnapshot uniqueCatalogPost : children) { //for populating the arraylist
//                    CatalogItem catalogItem = uniqueCatalogPost.getValue(CatalogItem.class);
//                    catalogPostItemarrayList.add(catalogItem);
//                    catalogRecyclerAdapter.notifyDataSetChanged();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//                Snackbar.make(catalogRecyclerView, "Please check your internet connection", Snackbar.LENGTH_LONG);
//            }
//        });

//        retrieveCatalogPostFromFirebase();

//        Log.d("POSTITEM - ",catalogPostItemarrayList.get(0).getDetails());
//        for (int i = 0; i < 8; i++) {
//            catalogPostItemarrayList.add(new CatalogItem("Surraj kathuria summer collection is here" + 1, "Summer Collection" + i));
//        }


        catalogRecyclerAdapter = new CatalogRecyclerAdapter(getContext(), catalogPostItemarrayList, new CatalogRecyclerAdapter.CatalogClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), "CatalogItemClicked", Toast.LENGTH_SHORT).show();
            }
        });
        catalogRecyclerView.setAdapter(catalogRecyclerAdapter);
        catalogRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        return v;
    }

    public void getCatalogPosts() {
        db.collection("catalogposts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            catalogPostItemarrayList.clear();

                            for (DocumentSnapshot document : task.getResult()) {
                                catalogPostItemarrayList.add(document.toObject(CatalogItem.class));
                                Log.i("CATALOGARRAYVALUE", document.toObject(CatalogItem.class).getDetails());
                            }
                        }
                        catalogRecyclerAdapter.notifyDataSetChanged();
                    }
                });
    }

//    public void retrieveCatalogPostFromFirebase() {
//        myCatalogRef.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                catalogPostItemarrayList.clear(); //clear existing data
//
//                for (DataSnapshot uniqueCatalogPost : dataSnapshot.getChildren()) { //for populating the arraylist
//                    CatalogItem catalogItem = uniqueCatalogPost.getValue(CatalogItem.class);
//                    catalogPostItemarrayList.add(catalogItem);
//                    catalogRecyclerAdapter.notifyDataSetChanged();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//                Snackbar.make(catalogRecyclerView, "Please check your internet connection", Snackbar.LENGTH_LONG);
//            }
//        });
//    }
}
