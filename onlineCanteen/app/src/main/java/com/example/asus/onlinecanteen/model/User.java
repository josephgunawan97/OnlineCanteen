package com.example.asus.onlinecanteen.model;

/**
 * Created by Jessica on 1/20/2018.
 */

public class User {

    String userName;
    String userPassword;
    String userNim;
    String userEmail;
    String userCategory;
    String userPhotoPath;
    String userPhone;

    // Empty Constructor
    public User() {
    }

    public User(String userName, String userPassword, String userNim, String userEmail, String userCategory, String userPhotoPath, String userPhone) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userNim = userNim;
        this.userEmail = userEmail;
        this.userCategory = userCategory;
        this.userPhotoPath = userPhotoPath;
        this.userPhone = userPhone;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserNim(String userNim) {
        this.userNim = userNim;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public void setUserPhotoPath(String userPhotoPath) {
        this.userPhotoPath = userPhotoPath;
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
