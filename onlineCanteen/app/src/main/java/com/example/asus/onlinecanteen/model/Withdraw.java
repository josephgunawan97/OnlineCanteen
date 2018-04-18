package com.example.asus.onlinecanteen.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Rickhen Hermawan on 02/04/2018.
 */

public class Withdraw implements Serializable {

    private String transfername,uid,bank;
    private int amount;
    private String accountnumber;
    private long requestdate;
    private int requeststatus;

    public Withdraw() {}

    public Withdraw(@NonNull String transfername, String uid, String bank, int amount, String accountnumber) {
        this.transfername = transfername;
        this.uid = uid;
        this.bank = bank;
        this.amount = amount;
        this.accountnumber = accountnumber;
        this.requestdate = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime().getTime();;
        requeststatus=0;
    }


    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTransfername() {
        return transfername;
    }

    public void setTransfername(String transfername) {
        this.transfername = transfername;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccountnumber() { return accountnumber; }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getRequestdate() {
        return requestdate;
    }

    public void setRequestdate(long requestdate) {
        this.requestdate = requestdate;
    }

    public int getRequeststatus() {
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
