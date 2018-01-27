package com.example.asus.onlinecanteen.model;

/**
 * Created by ASUS on 1/20/2018.
 */

public class Product {

    String tokoId;
    String productName;
    Integer productStock;
    Integer productPrice;

    // Empty Constructor
    public Product() {
    }

    public Product(String tokoId, String productName, Integer productStock, Integer productPrice) {
        this.tokoId = tokoId;
        this.productName = productName;
        this.productStock = productStock;
        this.productPrice = productPrice;
    }

    public void setTokoId(String tokoId) {
        this.tokoId = tokoId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(Integer productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductStock(Integer productStock) {
        this.productStock = productStock;
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
