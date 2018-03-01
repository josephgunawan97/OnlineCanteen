package com.example.asus.onlinecanteen.model;

import android.support.annotation.NonNull;

/**
 * Created by ASUS on 3/2/2018.
 */

public class Cart {

    // Product
    private Product product;
    // Quantity
    private int quantity;

    public Cart(@NonNull Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public String getProductName() {
        return product.getName();
    }

    public int getProductPrice() { return product.getPrice();}

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {this.quantity = quantity;}

    public static int getTotalPrice(Cart cart) {
        return cart.getQuantity() * cart.getProductPrice();
    }
}
