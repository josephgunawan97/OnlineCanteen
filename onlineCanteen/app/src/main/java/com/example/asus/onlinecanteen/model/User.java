package com.example.asus.onlinecanteen.model;

import android.net.Uri;

/**
 * Created by Jessica on 1/20/2018.
 */

public class User {

    String name;
    String userNim;
    String userPhone;
    String img;


    public User(String name, String userNim, String userPhone, String img) {
        this.name = name;
        this.userNim = userNim;
        this.userPhone = userPhone;
        this.img = img;
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

    public String getUserPhone() {
        return userPhone;
    }

    public String getimg() { return img; }
}
