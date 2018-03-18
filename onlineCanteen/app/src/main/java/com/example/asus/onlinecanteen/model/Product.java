package com.example.asus.onlinecanteen.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ASUS on 1/20/2018.
 */

public class Product implements Parcelable {

    private String tokoId;
    private String name;
    private int stock;
    private int price;
    private String imageUrl;
    private boolean checked;

    // Empty Constructor
    public Product() {
    }

    public Product(String tokoId, String productName, int productStock, int productPrice, String imageUrl) {
        this.tokoId = tokoId;
        this.name = productName;
        this.stock = productStock;
        this.price = productPrice;
        this.imageUrl = imageUrl;
        this.checked = false;
    }

    protected Product(Parcel in) {
        tokoId = in.readString();
        name = in.readString();
        stock = in.readInt();
        price = in.readInt();
        imageUrl = in.readString();
        checked = in.readByte() != 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tokoId);
        dest.writeString(name);
        dest.writeInt(stock);
        dest.writeInt(price);
        dest.writeString(imageUrl);
        dest.writeByte((byte) (checked ? 1 : 0));
    }
}
