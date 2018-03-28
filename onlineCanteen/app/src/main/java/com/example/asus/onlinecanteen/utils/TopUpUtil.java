package com.example.asus.onlinecanteen.utils;

import com.example.asus.onlinecanteen.model.TopUp;
import com.example.asus.onlinecanteen.model.Transaction;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by steve on 2/10/2018.
 */

public final class TopUpUtil {

    // Firebase path to transactions
    private static final String FIREBASE_PATH = "topuprequest";

    public static DatabaseReference insert(TopUp topUp) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(FIREBASE_PATH).push();
        reference.setValue(topUp);
        return reference;
    }

    public static Query query() {
        return FirebaseDatabase.getInstance().getReference(FIREBASE_PATH);
    }

    public static Query query(String orderBy, String value) {
        return query().orderByChild(orderBy).equalTo(value);
    }
}
