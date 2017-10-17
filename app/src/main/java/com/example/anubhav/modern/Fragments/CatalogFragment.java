package com.example.anubhav.modern.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
import com.google.firebase.firestore.Query;
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
    ProgressBar mProgressBar;
//    FloatingActionButton floatingActionButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.catalog_recyclerview, container, false);
        catalogRecyclerView = v.findViewById(R.id.catalog_recyclerViewList);
        setHasOptionsMenu(true);
/**        floatingActionButton = v.findViewById(R.id.catalog_fab); */
        mProgressBar = v.findViewById(R.id.catalog_fragment_progress);
        catalogPostItemarrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
/**        final FragmentManager fragmentManager = getFragmentManager();
 //        floatingActionButton.setOnClickListener(new View.OnClickListener() {
 //            @Override
 //            public void onClick(View view) {
 //                Thread thread = new Thread(new Runnable() {
 //                    @Override
 //                    public void run() {
 //                        fragmentManager.beginTransaction().replace(R.id.content, new CatalogUploaderFragment()).commit();
 //                    }
 //                });
 //                thread.start();
 //            }
 //
 //        });*/
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        db.collection("catalogposts")
                .orderBy("epoch", Query.Direction.DESCENDING)
                .limit(100)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        for (DocumentChange document : documentSnapshots.getDocumentChanges()) {
                            catalogPostItemarrayList.add(document.getDocument().toObject(CatalogItem.class));
                        }
                        mProgressBar.setVisibility(View.GONE);
                        catalogRecyclerAdapter.notifyDataSetChanged();

                    }
                });
        getCatalogPosts();


        catalogRecyclerAdapter = new CatalogRecyclerAdapter(getContext(), catalogPostItemarrayList, new CatalogRecyclerAdapter.CatalogClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Snackbar.make(mProgressBar, "Item Clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
        catalogRecyclerView.setAdapter(catalogRecyclerAdapter);
        catalogRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        return v;
    }

    public void getCatalogPosts() {
        db.collection("catalogposts")
                .limit(100)
                .orderBy("epoch", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            catalogPostItemarrayList.clear();

                            for (DocumentSnapshot document : task.getResult()) {
                                catalogPostItemarrayList.add(document.toObject(CatalogItem.class));
                            }
                        }
                        mProgressBar.setVisibility(View.GONE);
                        catalogRecyclerAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.catalog_menu, menu);


        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.catalog_menu_about_us) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content, new AboutUsFragment()).commit();
        }
        return true;
    }


}
