package com.example.asus.onlinecanteen.utils;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FeaturedProductUtil {

    private static final String FIREBASE_PATH = "featuredproduct";

    public static Query query() {
        return FirebaseDatabase.getInstance().getReference(FIREBASE_PATH);
    }
}
