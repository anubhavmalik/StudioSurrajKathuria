package com.example.anubhav.modern.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Anubhav on 26-08-2017.
 */
@IgnoreExtraProperties

public class PostItem implements Serializable {
    String date;
    String time;
    String details;
    String by_user;
    CircleImageView circleImageView;

    public PostItem(String date, String time, String details, String by_user, CircleImageView circleImageView) {
        this.date = date;
        this.time = time;
        this.details = details;
        this.by_user = by_user;
        this.circleImageView = circleImageView;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDetails() {
        return details;
    }

    public String getBy_user() {
        return by_user;
    }

    public CircleImageView getCircleImageView() {
        return circleImageView;
    }
}
