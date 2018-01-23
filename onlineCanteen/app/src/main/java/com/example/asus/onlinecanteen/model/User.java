package com.example.asus.onlinecanteen.model;

/**
 * Created by Jessica on 1/20/2018.
 */

public class User {

    String userId;
    String userName;
    String userPassword;
    String userNim;
    String userEmail;
    String userCategory;
    String userPhotoPath;
    String userPhone;

    public User(String userId, String userName, String userPassword, String userNim, String userEmail, String userCategory, String userPhotoPath, String userPhone) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userNim = userNim;
        this.userEmail = userEmail;
        this.userCategory = userCategory;
        this.userPhotoPath = userPhotoPath;
        this.userPhone = userPhone;
    }


    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserNim() {
        return userNim;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserCategory() {
        return userCategory;
    }

    public String getUserPhotoPath() {
        return userPhotoPath;
    }

    public String getUserPhone() {
        return userPhone;
    }
}
