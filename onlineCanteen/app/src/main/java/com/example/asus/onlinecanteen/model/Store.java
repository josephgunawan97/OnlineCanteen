package com.example.asus.onlinecanteen.model;

import java.sql.Time;

/**
 * Created by ASUS on 2/12/2018.
 */

public class Store {
    private String storeName;
    private String storeId;
    private String openHour;
    private String closeHour;
    private String location;
    private String img;

    public Store(String storeName, String openHour, String closeHour, String location, String img) {
        this.storeName = storeName;
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.location = location;
        this.img = img;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getOpenHour() {
        return openHour;
    }

    public String getCloseHour() {
        return closeHour;
    }

    public String getLocation() {
        return location;
    }

    public String getImg() {return img;}
}
