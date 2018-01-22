package com.example.asus.onlinecanteen.model;

/**
 * Created by ASUS on 1/20/2018.
 */

public class Product {

    String productId;
    String tokoId;
    String productName;
    Integer productStock;
    Integer productPrice;

    public Product(String productId, String tokoId, String productName, Integer productStock, Integer productPrice) {
        this.productId = productId;
        this.tokoId = tokoId;
        this.productName = productName;
        this.productStock = productStock;
        this.productPrice = productPrice;
    }

    public String getProductId() {
        return productId;
    }

    public String getTokoId() {
        return tokoId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getProductStock() {
        return productStock;
    }

    public Integer getProductPrice() {
        return productPrice;
    }
}
