package com.example.asus.onlinecanteen.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.asus.onlinecanteen.fragment.MerchantOrderListFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by steve on 2/10/2018.
 */

public class Transaction implements Serializable{

    // CONSTANTS
    // Transaction is pending, Items is not processed
    public static final int PENDING = 0;
    // Delivery is in process
    public static final int ON_DELIVERY = 1;
    // Transaction is finished, Items are delivered
    public static final int DELIVERED = 2;

    private String name;
    // Store Identity Variable
    private String sid;
    // User Identity Variable
    private String uid;
    // User Location
    private String location;
    // List of Items
    private HashMap<String, HashMap<String, Integer>> items;
    // Total price
    private int totalPrice;
    // Purchase Date
    private long purchaseDate;
    // Delivery Status
    private int deliveryStatus;

    // Empty Constructor
    public Transaction() {

    }

    public Transaction(@NonNull String store_id, @NonNull String user_id, @NonNull List<PurchasedItem> items, String location) {
        this.sid = store_id;
        this.uid = user_id;
        this.location = location;
        this.totalPrice = 0;
        this.items = new HashMap<>();
        for(PurchasedItem item : items) {
            // Add unit price and quantity of purchased item
            HashMap<String, Integer> itemMap = new HashMap<>();
            itemMap.put("unit_price", item.getProductPrice());
            itemMap.put("quantity", item.getQuantity());
            this.items.put(item.getProductName(), itemMap);
            // Calculate total price of transaction
            this.totalPrice += PurchasedItem.getTotalPrice(item);
        }

        this.purchaseDate = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime().getTime();
        this.deliveryStatus = Transaction.PENDING;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setDeliveryStatus(int deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public void setItems(HashMap<String, HashMap<String, Integer>> items) {
        this.items = items;
    }

    public void setPurchaseDate(long purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getSid() {
        return sid;
    }

    public String getUid() {
        return uid;
    }

    public int getDeliveryStatus() {
        return deliveryStatus;
    }

    public HashMap<String, HashMap<String, Integer>> getItems() {
        return items;
    }

    public String getPurchaseDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        return sdf.format(purchaseDate);
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String names){this.name = names;
        Log.i(MerchantOrderListFragment.class.getSimpleName(),"NAMA ORNG22"+name);
    }

    public String getName(){

        return this.name;
    }

}