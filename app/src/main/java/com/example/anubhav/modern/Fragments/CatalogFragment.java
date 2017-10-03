package com.example.anubhav.modern.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.anubhav.modern.Adapters.CatalogRecyclerAdapter;
import com.example.anubhav.modern.Models.CatalogItem;
import com.example.anubhav.modern.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Anubhav on 25-08-2017.
 */

public class CatalogFragment extends Fragment {

    RecyclerView catalogRecyclerView;
    ArrayList<CatalogItem> catalogPostItemarrayList;
    CatalogRecyclerAdapter catalogRecyclerAdapter;
    DatabaseReference myUserRef;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myCatalogRef;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.catalog_recyclerview, container, false);
        catalogRecyclerView = v.findViewById(R.id.catalog_recyclerViewList);
        catalogPostItemarrayList = new ArrayList<>();
        myUserRef = firebaseDatabase.getReference("Users");
        myCatalogRef = firebaseDatabase.getReference("Catalog");

        retrieveCatalogPostFromFirebase();
        for (int i = 0; i < 8; i++) {
            catalogPostItemarrayList.add(new CatalogItem("Surraj kathuria summer collection is here" + 1, "Summer Collection" + i));
        }

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

    public void retrieveCatalogPostFromFirebase() {
        myCatalogRef.addValueEventListener((new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot uniqueCatalogPost : dataSnapshot.getChildren()) {
                    catalogPostItemarrayList.add(uniqueCatalogPost.getValue(CatalogItem.class));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }));
    }
}
