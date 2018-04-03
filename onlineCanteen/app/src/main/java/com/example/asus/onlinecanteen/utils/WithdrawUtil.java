package com.example.asus.onlinecanteen.utils;

import com.example.asus.onlinecanteen.model.Withdraw;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by Rickhen Hermawan on 02/04/2018.
 */

public final class WithdrawUtil {

    // Firebase path to transactions
    private static final String FIREBASE_PATH = "withdrawrequest";

    public static DatabaseReference insert(Withdraw withdraw) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(FIREBASE_PATH).push();
        reference.setValue(withdraw);
        return reference;
    }

    public static Query query() {
        return FirebaseDatabase.getInstance().getReference(FIREBASE_PATH);
    }

    public static Query query(String orderBy, String value) {
        return query().orderByChild(orderBy).equalTo(value);
    }
}
