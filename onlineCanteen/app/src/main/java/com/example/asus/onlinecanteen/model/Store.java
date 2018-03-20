package com.example.asus.onlinecanteen.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;

/**
 * Created by ASUS on 2/12/2018.
 */

public class Store implements Parcelable {
    private String storeName;
    private String storeId;
    private String openHour;
    private String closeHour;
    private String location;
    private String img;
    private String email;

    public Store() {}

    public Store(String storeName, String openHour, String closeHour, String location, String img, String email) {
        this.email = email;
        this.storeName = storeName;
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.location = location;
        this.img = img;
    }

    protected Store(Parcel in) {
        storeName = in.readString();
        storeId = in.readString();
        openHour = in.readString();
        closeHour = in.readString();
        location = in.readString();
        img = in.readString();
        email = in.readString();
    }


    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setCloseHour(String closeHour) {
        this.closeHour = closeHour;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setOpenHour(String openHour) {
        this.openHour = openHour;
    }

    public void setImage(String img) { this.img = img; }

    public String getEmail() {
        return email;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getOpenHour() { return openHour; }

    public String getCloseHour() {
        return closeHour;
    }

    public String getLocation() {
        return location;
    }

    public String getImg() {return img;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getStoreName());
        dest.writeString(getStoreId());
        dest.writeString(getOpenHour());
        dest.writeString(getCloseHour());
        dest.writeString(getLocation());
        dest.writeString(getImg());
        dest.writeString(getEmail());
    }
}
