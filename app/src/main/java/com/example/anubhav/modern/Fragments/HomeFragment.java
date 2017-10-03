package com.example.anubhav.modern.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.anubhav.modern.Adapters.HomeRecyclerAdapter;
import com.example.anubhav.modern.MainActivity;
import com.example.anubhav.modern.Models.PostItem;
import com.example.anubhav.modern.R;

import java.util.ArrayList;

/**
 * Created by Anubhav on 25-08-2017.
 */

public class HomeFragment extends Fragment {
    final int uploadRequest = 90;
    HomeRecyclerAdapter homeRecyclerAdapter;
    RecyclerView homeRecyclerView;
    ArrayList<PostItem> postItemArrayList;
    FloatingActionButton floatingActionButton;
    MainActivity mainActivity;

    public HomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_recyclerview, container, false);
        homeRecyclerView = v.findViewById(R.id.homefragment_recyclerView);
        postItemArrayList = new ArrayList<>();
        floatingActionButton = v.findViewById(R.id.fab);

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


        for (int i = 0; i < 8; i++) {
            postItemArrayList.add(new PostItem("date " + i, "Time " + i, "details " + i, "User " + i, null));
        }
        homeRecyclerAdapter = new HomeRecyclerAdapter(getContext(), postItemArrayList, new HomeRecyclerAdapter.HomeClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), "Handle the click", Toast.LENGTH_SHORT).show();
            }
        });

        homeRecyclerView.setAdapter(homeRecyclerAdapter);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        return v;
    }
}
