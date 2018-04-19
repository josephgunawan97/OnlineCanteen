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
    private String phoneNumber;
    private String openHour;
    private String closeHour;
    private String location;
    private String img;
    private String email;
    private String bio;
    private int deliveryfee;
    private String device_token;

    public Store() {}

    public Store(String storeName, String openHour, String closeHour, String location, String img, String email) {
        this.email = email;
        this.storeName = storeName;
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.location = location;
        this.img = img;
    }

    public Store(String storeName, String phoneNumber, String email, String img, String openHour, String closeHour, String location, String bio, String device_token) {
        this.storeName = storeName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.img = img;
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.location = location;
        this.bio = bio;
        this.device_token = device_token;
    }

    protected Store(Parcel in) {
        storeName = in.readString();
        storeId = in.readString();
        phoneNumber = in.readString();
        openHour = in.readString();
        closeHour = in.readString();
        location = in.readString();
        img = in.readString();
        email = in.readString();
        bio = in.readString();
        deliveryfee = in.readInt();
        device_token = in.readString();
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

    public void setImg(String img) { this.img = img; }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDeliveryfee(int deliveryfee) {this.deliveryfee = deliveryfee; }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public int getDeliveryfee() {return deliveryfee; }

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

    public String getBio() {
        return bio;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDevice_token() {
        return device_token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(storeName);
        dest.writeString(storeId);
        dest.writeString(phoneNumber);
        dest.writeString(openHour);
        dest.writeString(closeHour);
        dest.writeString(location);
        dest.writeString(img);
        dest.writeString(email);
        dest.writeString(bio);
        dest.writeInt(deliveryfee);
        dest.writeString(device_token);
    }
}
