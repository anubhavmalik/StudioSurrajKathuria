package com.example.anubhav.modern.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by Anubhav on 26-08-2017.
 */
@IgnoreExtraProperties

public class UserItem implements Serializable {

    String name, photourl;


    public UserItem(/*int user_id boolean isAdmin,*/ String name, String photourl) {
        this.name = name;
        this.photourl = photourl;
    }

    public String getName() {
        return name;
    }

    public String getPhotourl() {
        return photourl;
    }

}
