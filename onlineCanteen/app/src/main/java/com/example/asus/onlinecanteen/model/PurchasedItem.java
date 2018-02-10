package com.example.asus.onlinecanteen.model;

import android.support.annotation.NonNull;

/**
 * Created by steve on 2/10/2018.
 */

public class PurchasedItem {

    // Product
    private Product product;
    // Quantity
    private int quantity;

    public PurchasedItem(@NonNull Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public String getProductName() {
        return product.getName();
    }

    public int getProductPrice() {
        return product.getPrice();
    }

    public int getQuantity() {
        return quantity;
    }

    public static int getTotalPrice(PurchasedItem purchasedItem) {
        return purchasedItem.getQuantity() * purchasedItem.getProductPrice();
    }
}
