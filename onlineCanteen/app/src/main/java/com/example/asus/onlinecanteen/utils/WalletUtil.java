package com.example.asus.onlinecanteen.utils;

import android.content.Intent;

import com.example.asus.onlinecanteen.activity.LoginActivity;
import com.example.asus.onlinecanteen.activity.MainActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ASUS on 3/14/2018.
 */

public class WalletUtil {

    DatabaseReference walletDatabase, walletReferences;
    String uid;
    int value, credit, debit;

    public Integer getAmount(String id){
        uid = id;
        walletDatabase = FirebaseDatabase.getInstance().getReference();

        walletDatabase.child("wallet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(uid).exists()) {
                    value = snapshot.getValue(Integer.class);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return value;
    }

    Boolean success = false;

    public Boolean creditAmount(String id, final int credit)
    {
        this.success = false;
        uid = id;
        this.credit = credit;
        walletDatabase = FirebaseDatabase.getInstance().getReference();

        walletDatabase.child("wallet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(uid).exists()) {
                    value = snapshot.child(uid).getValue(Integer.class);
                    if(value >= credit)
                    {
                        value -= credit;
                        setAmount(uid,value);
                        success = true;
                    }
                    else success = false;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return success;
    }

    public Boolean debitAmount(String id, int add)
    {
        success = false;
        walletDatabase = FirebaseDatabase.getInstance().getReference();
        uid = walletDatabase.child("wallet").child(id).getKey();
        debit = add;

        walletDatabase.child("wallet").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if((dataSnapshot.getKey().toString()).equals(uid))
                {
                    value = Integer.parseInt(dataSnapshot.getValue().toString()) + debit;
                    setAmount(uid,0);
                    success = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return success;


    }

    private void setAmount(String uid, Integer value) {
        walletReferences = FirebaseDatabase.getInstance().getReference("wallet").child(uid);
        walletReferences.setValue(value);
    }

}
