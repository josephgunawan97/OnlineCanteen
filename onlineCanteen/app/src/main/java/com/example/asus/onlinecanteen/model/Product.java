package com.example.asus.onlinecanteen.model;

/**
 * Created by ASUS on 1/20/2018.
 */

public class Product {

    private String tokoId;
    private String name;
    private int stock;
    private int price;

    // Empty Constructor
    public Product() {
    }

    public Product(String tokoId, String productName, Integer productStock, Integer productPrice) {
        this.tokoId = tokoId;
        this.name = productName;
        this.stock = productStock;
        this.price = productPrice;
    }

    public void setTokoId(String tokoId) {
        this.tokoId = tokoId;
    }

    public void setName(String productName) {
        this.name = productName;
    }

    public void setPrice(Integer productPrice) {
        this.price = productPrice;
    }

    public void setStock(Integer productStock) {
        this.stock = productStock;
    }

    public String getTokoId() {
        return tokoId;
    }

    public String getName() {
        return name;
    }

    public Integer getStock() {
        return stock;
    }

    public Integer getPrice() {
        return price;
    }
}
