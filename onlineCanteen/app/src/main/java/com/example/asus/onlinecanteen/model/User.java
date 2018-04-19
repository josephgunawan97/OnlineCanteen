package com.example.asus.onlinecanteen.model;

/**
 * Created by Jessica on 1/20/2018.
 */

public class User {

    private String name;
    private String userPhone;
    private String profilePictureUrl;
    private String device_token;

    public User() {}

    public User(String name, String phone, String profilePictureUrl, String device_token) {
        this.name = name;
        this.userPhone = phone;
        this.profilePictureUrl = profilePictureUrl;
        this.device_token = device_token;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getName() {
        return name;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public String getDevice_token() {
        return device_token;
    }
}
