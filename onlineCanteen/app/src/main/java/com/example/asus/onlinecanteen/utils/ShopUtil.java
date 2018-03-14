package com.example.asus.onlinecanteen.utils;

import com.example.asus.onlinecanteen.model.Store;
import com.example.asus.onlinecanteen.model.Transaction;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by Steven Albert on 3/14/2018.
 */

public class ShopUtil {

    // Firebase path to transactions
    private static final String FIREBASE_PATH = "store";

    public static DatabaseReference insert(Store shop) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(FIREBASE_PATH).push();
        reference.setValue(shop);
        return reference;
    }

    public static Query query() {
        return FirebaseDatabase.getInstance().getReference(FIREBASE_PATH);
    }
}
