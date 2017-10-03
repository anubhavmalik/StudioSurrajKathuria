package com.example.anubhav.modern.Firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Anubhav on 27-08-2017.
 */

public class RealTimeDatabase {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myPostRef;
    DatabaseReference myUserRef;
    DatabaseReference myCatalogRef;


    public RealTimeDatabase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        this.myPostRef = firebaseDatabase.getReference("Posts");

        this.myUserRef = firebaseDatabase.getReference("Users");

        this.myCatalogRef = firebaseDatabase.getReference("Catalog");
    }

    public DatabaseReference getMyPostRef() {
        return myPostRef;
    }

    public DatabaseReference getMyUserRef() {
        return myUserRef;
    }

    public DatabaseReference getMyCatalogRef() {
        return myCatalogRef;
    }



}
