package com.example.asus.onlinecanteen.model;

import android.net.Uri;

/**
 * Created by Jessica on 1/20/2018.
 */

public class User {

    String name;
    String userNim;
    String userCategory;
    String userPhone;

    // Empty Constructor
    public User() {
    }

    public User(String name, String userNim, String userCategory, String userPhone) {
        this.name = name;
        this.userNim = userNim;
        this.userCategory = userCategory;
        this.userPhone = userPhone;
    }

    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }

    public void setUserNim(String userNim) {
        this.userNim = userNim;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserNim() {
        return userNim;
    }

    public String getUserCategory() {
        return userCategory;
    }

    public String getUserPhone() {
        return userPhone;
    }
}
