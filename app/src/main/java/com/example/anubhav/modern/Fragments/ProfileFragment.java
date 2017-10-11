package com.example.anubhav.modern.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.example.anubhav.modern.Constants.ApplicationConstants;
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
    Button saveButton;

    ArrayList<String> mArrayList;
    ArrayAdapter<String> arrayAdapter;
    SharedPreferences mUserDetailsSharedPreferences;

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
        saveButton = v.findViewById(R.id.profile_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableViews(false);

            }
        });
        mUserDetailsSharedPreferences = getContext().getSharedPreferences(ApplicationConstants.loginStatePreferencesName, Context.MODE_PRIVATE);
        enableViews(false);
        Glide.with(getContext().getApplicationContext())
                .load(mUserDetailsSharedPreferences.getString(ApplicationConstants.userPhotoUrl, null))
                .into(circleImageView);
        nameTextView.setText(mUserDetailsSharedPreferences.getString(ApplicationConstants.userName, null));
        numberTextView.setText(mUserDetailsSharedPreferences.getString(ApplicationConstants.phoneNumber, null));


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
                    //TODO: Layout for about Surraj Kathuria
                } else if (i == 2) {
                    //TODO: Layout for about Vimmi Kathuria
                }
            }
        });
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.profile_edit) {
            enableViews(true);
        }
        return super.onOptionsItemSelected(item);
    }


    private void enableViews(boolean b) {

    }
}
