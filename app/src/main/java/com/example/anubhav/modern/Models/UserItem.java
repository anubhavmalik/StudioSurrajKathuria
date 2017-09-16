package com.example.anubhav.modern.Models;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Anubhav on 26-08-2017.
 */

public class UserItem {
    int user_id;
    boolean isAdmin;
    String name,phone;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserItem(int user_id, boolean isAdmin, String name, String phone) {
        this.user_id = user_id;
        this.isAdmin = isAdmin;
        this.name = name;
        this.phone = phone;
    }
}
