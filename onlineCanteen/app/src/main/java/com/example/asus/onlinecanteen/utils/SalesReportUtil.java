package com.example.asus.onlinecanteen.utils;

import com.example.asus.onlinecanteen.model.SalesReport;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by ASUS on 3/27/2018.
 */

public class SalesReportUtil {
    // Firebase path to sales report
    private static final String FIREBASE_PATH = "salesreportrequest";

    public static DatabaseReference insert(SalesReport salesReport) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(FIREBASE_PATH).push();
        reference.setValue(salesReport);
        return reference;
    }

    public static Query query() {
        return FirebaseDatabase.getInstance().getReference(FIREBASE_PATH);
    }

    public static Query query(String orderBy, String value) {
        return query().orderByChild(orderBy).equalTo(value);
    }
}
