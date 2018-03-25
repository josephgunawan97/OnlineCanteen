package com.example.asus.onlinecanteen.model;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class TopUp {

    private String transfername,uid,bank, proofpic;
    private int amount;
    private long requestdate;
    private int requeststatus;

    public TopUp() {}
    public TopUp(@NonNull String transfername, String uid,String proofpic, String bank, int amount) {
        this.transfername = transfername;
        this.uid = uid;
        this.bank = bank;
        this.amount = amount;
        this.proofpic = proofpic;
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

    public String getProofpicUrl() {
        return proofpic;
    }

    public void setProofpicUrl(String proofpicUrl) {
        this.proofpic = proofpicUrl;
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
