package com.example.anubhav.SurrajKathuriaClothing.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anubhav.modern.R;

/**
 * Created by Anubhav on 03-11-2017.
 */

public class PaperOnBoardingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.onboarding_fragment, container, false);

        // this fragment must be empty
        return v;
    }
}
