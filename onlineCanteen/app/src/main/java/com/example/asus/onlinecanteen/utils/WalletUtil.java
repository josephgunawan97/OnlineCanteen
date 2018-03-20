package com.example.asus.onlinecanteen.utils;

import android.content.Intent;
import android.util.Log;

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

    public void creditAmount(String id, final int creditx)
    {
        uid = id;
        credit = creditx;
        walletDatabase = FirebaseDatabase.getInstance().getReference();

        walletDatabase.child("wallet").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String valueString = snapshot.getValue().toString();
                    value = Integer.parseInt(valueString);
                    if(value >= credit)
                    {
                        setAmount(uid,value-credit);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public void debitAmount(String id, int add)
    {
        walletDatabase = FirebaseDatabase.getInstance().getReference();
        uid = id;
        debit = add;

        walletDatabase.child("wallet").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String valueString = dataSnapshot.getValue().toString();
                    value = Integer.parseInt(valueString);
                    setAmount(uid,value+debit);
                    return;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setAmount(String uid, Integer value) {
        walletReferences = FirebaseDatabase.getInstance().getReference("wallet").child(uid);
        walletReferences.setValue(value);
    }

    public int getAmount(String id){
        walletDatabase = FirebaseDatabase.getInstance().getReference();
        uid = id;
        final int[] x = new int[1];

        walletDatabase.child("wallet").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String valueString = dataSnapshot.getValue().toString();
                    x[0] = Integer.parseInt(valueString);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return x[0];
    }



}
