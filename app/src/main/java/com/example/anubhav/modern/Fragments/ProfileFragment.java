package com.example.anubhav.modern.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anubhav.modern.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Anubhav on 25-08-2017.
 */

public class ProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profilefragmentlayout, container, false);
        TextView nameTextView = v.findViewById(R.id.profileName);
        TextView numberTextView = v.findViewById(R.id.profileNumber);
        CircleImageView circleImageView = v.findViewById(R.id.profile_image);


        /**
         * TODO :Set action for changing name and number accordingly after database connectivity
         * */


        return v;
    }
}
