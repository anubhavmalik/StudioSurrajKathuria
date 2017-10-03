package com.example.anubhav.modern.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.anubhav.modern.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Anubhav on 25-08-2017.
 */

public class ProfileFragment extends Fragment {
    EditText nameTextView;
    EditText numberTextView;
    CircleImageView circleImageView;
    ListView mListView;
    ArrayList<String> mArrayList;
    ArrayAdapter<String> arrayAdapter;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profilefragmentlayout, container, false);
        nameTextView = v.findViewById(R.id.profileName);
        numberTextView = v.findViewById(R.id.profileNumber);
        circleImageView = v.findViewById(R.id.profile_image);
        mListView = v.findViewById(R.id.profile_listView);
        mArrayList = new ArrayList<>();
        mArrayList.add("Reach Us");
        mArrayList.add("About Surraj Kathuria");
        mArrayList.add("About Vimmi Kathuria");
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mArrayList);
        mListView.setAdapter(arrayAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    //TODO : Layout for REACH US
                } else if (i == 1) {
                    //TODO: Layout for about surraj kathuria
                } else if (i == 2) {
                    //TODO: Layout for about vimmi kathuria
                }
            }
        });


        return v;
    }
}
