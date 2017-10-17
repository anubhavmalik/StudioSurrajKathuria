package com.example.anubhav.modern.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.anubhav.modern.R;

import java.util.Locale;

/**
 * Created by Anubhav on 17-10-2017.
 */

public class AboutUsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about_us_fragment, container, false);
        final Button locateUsButton = v.findViewById(R.id.locate_us_button);
        String mTitle = " Surraj Kathuria's Store ";
        final String geoUri = "http://maps.google.com/maps?q=loc:" + 28.467437 + "," + 77.017303 + " (" + mTitle + ")";
        locateUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String uri = String.format(Locale.ENGLISH, geoUri);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    getContext().startActivity(intent);
                } catch (android.content.ActivityNotFoundException anfe) {
                    Snackbar.make(locateUsButton, "You do not have any apps to show location", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }
}