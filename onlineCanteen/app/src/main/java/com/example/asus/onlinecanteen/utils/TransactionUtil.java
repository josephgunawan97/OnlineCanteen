package com.example.asus.onlinecanteen.utils;

import com.example.asus.onlinecanteen.model.Transaction;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by steve on 2/10/2018.
 */

public final class TransactionUtil {

    // Firebase path to transactions
    private static final String FIREBASE_PATH = "transactions";

    public static DatabaseReference insert(Transaction transaction) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(FIREBASE_PATH).push();
        reference.setValue(transaction);
        return reference;
    }

    public static Query query() {
        return FirebaseDatabase.getInstance().getReference(FIREBASE_PATH);
    }

    public static Query query(String orderBy, String value) {
        return query();
    }
}
