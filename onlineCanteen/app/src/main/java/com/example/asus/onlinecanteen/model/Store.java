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

    public Store() {}

    public Store(String storeName, String openHour, String closeHour, String location) {
        this.storeName = storeName;
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.location = location;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
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
}
