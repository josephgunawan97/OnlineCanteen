package com.example.asus.onlinecanteen.model;

import android.support.annotation.NonNull;

/**
 * Created by steve on 2/10/2018.
 */

public class PurchasedItem {

    // Product name
    private String productName;
    // Product price
    private int productPrice;
    // Quantity
    private int quantity;

    public PurchasedItem(@NonNull String productName, int productPrice, int quantity) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public static int getTotalPrice(PurchasedItem purchasedItem) {
        return purchasedItem.getQuantity() * purchasedItem.getProductPrice();
    }
}
