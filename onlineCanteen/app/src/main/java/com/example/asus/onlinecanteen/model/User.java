package com.example.asus.onlinecanteen.model;

/**
 * Created by Jessica on 1/20/2018.
 */

public class User {

    private String phone;
    private String profilePictureUrl;
    private String type;
    private int wallet;

    public User(String phone, String profilePictureUrl) {
        this.phone = phone;
        this.profilePictureUrl = profilePictureUrl;
        this.type = "user";
        this.wallet = 0;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }

    public String getPhone() {
        return phone;
    }

    public String getType() {
        return type;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public int getWallet() {
        return wallet;
    }
}
