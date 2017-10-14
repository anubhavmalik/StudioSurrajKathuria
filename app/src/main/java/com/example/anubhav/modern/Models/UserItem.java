package com.example.anubhav.modern.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by Anubhav on 26-08-2017.
 */
@IgnoreExtraProperties

public class UserItem implements Serializable {

    String name, photourl, number;

    public UserItem() {
    }

    public UserItem(/*int user_id boolean isAdmin,*/ String name, String photourl, String number) {
        this.name = name;
        this.photourl = photourl;
        this.number = number;

    }

    public String getName() {
        return name;
    }

    public String getPhotourl() {
        return photourl;
    }

    public String getNumber() {
        return number;
    }

}
