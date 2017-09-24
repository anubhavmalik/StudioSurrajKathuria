package com.example.anubhav.modern.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anubhav.modern.Adapters.CatalogRecyclerAdapter;
import com.example.anubhav.modern.Models.PostItem;
import com.example.anubhav.modern.R;

import java.util.ArrayList;

/**
 * Created by Anubhav on 25-08-2017.
 */

public class CatalogFragment extends Fragment {

    CatalogRecyclerAdapter mCatalogRecyclerAdapter;
    RecyclerView catalogRecyclerView;
    ArrayList<PostItem> arrayList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.catalogcardlayout,container,false);
        v.findViewById(R.id.homefragment_recyclerView);



        return v;
    }
}
