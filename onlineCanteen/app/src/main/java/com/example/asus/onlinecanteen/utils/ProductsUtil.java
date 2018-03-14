package com.example.asus.onlinecanteen.utils;

import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.Store;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by Steven Albert on 3/14/2018.
 */

public class ProductsUtil {

    // Firebase path to transactions
    private static final String FIREBASE_PATH = "products";
    private static final String STORE_ID = "tokoId";

    public static DatabaseReference insert(Product product) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(FIREBASE_PATH).push();
        reference.setValue(product);
        return reference;
    }

    public static Query query() {
        return FirebaseDatabase.getInstance().getReference(FIREBASE_PATH);
    }

    public static Query query(Store store) {
        return query().orderByChild(STORE_ID).equalTo(store.getStoreId());
    }

    public static Query query(String orderBy, String value) {
        return query().orderByChild(orderBy).equalTo(value);
    }
}
