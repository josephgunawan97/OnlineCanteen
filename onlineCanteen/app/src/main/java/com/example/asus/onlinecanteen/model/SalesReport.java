package com.example.asus.onlinecanteen.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by ASUS on 3/27/2018.
 */

public class SalesReport {
    private String uid;
    private long requestdate;
    private int requeststatus;

    public SalesReport() {}

    public SalesReport(@NonNull String uid) {
        this.uid = uid;
        this.requestdate = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime().getTime();;
        requeststatus= 0;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getRequestdate() {
        return requestdate;
    }

    public void setRequestdate(long requestdate) {
        this.requestdate = requestdate;
    }

    public Integer getRequeststatus() {
        return requeststatus;
    }

    public void setRequeststatus(int requeststatus) {
        this.requeststatus = requeststatus;
    }

    public String getRequestDateString(long x) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        return sdf.format(x);
    }
}