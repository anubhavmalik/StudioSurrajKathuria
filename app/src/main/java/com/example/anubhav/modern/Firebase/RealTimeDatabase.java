package com.example.anubhav.modern.Firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Anubhav on 27-08-2017.
 */

public class RealTimeDatabase {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myPostRef = firebaseDatabase.getReference("Posts");
    DatabaseReference myUserRef = firebaseDatabase.getReference("Users");

    public DatabaseReference getMyPostRef() {
        return myPostRef;
    }

    public DatabaseReference getMyUserRef() {
        return myUserRef;
    }
}
