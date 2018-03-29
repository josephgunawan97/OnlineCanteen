package com.example.asus.onlinecanteen.model;

/**
 * Created by Jessica on 1/20/2018.
 */

public class User {

    private String name;
    private String userPhone;
    private String profilePictureUrl;

    public User() {}

    public User(String name, String phone, String profilePictureUrl) {
        this.name = name;
        this.userPhone = phone;
        this.profilePictureUrl = profilePictureUrl;
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

    public String getName() {
        return name;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
}
