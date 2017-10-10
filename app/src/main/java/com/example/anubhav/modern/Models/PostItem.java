package com.example.anubhav.modern.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by Anubhav on 26-08-2017.
 */
@IgnoreExtraProperties

public class PostItem implements Serializable {
    String date;
    String time;
    String details;
    String by_user;
    String userImageURL;
    String postImageURL;
    String epoch;

    public PostItem() {
    }


    public PostItem(String date, String time, String details, String by_user, String userImageURL, String postImageURL, String epoch) {
        this.date = date;
        this.time = time;
        this.details = details;
        this.by_user = by_user;
        this.userImageURL = userImageURL;
        this.postImageURL = postImageURL;
        this.epoch = epoch;

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

    public String getUserImageURL() {
        return userImageURL;
    }

    public String getPostImageURL() {
        return postImageURL;
    }

    public String getEpoch() {
        return epoch;
    }
}
