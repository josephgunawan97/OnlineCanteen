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
    int amount;

    private boolean success = false;

    public boolean creditAmount(String id, final int creditx)
    {
        success = false;
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

    private void setSuccess(boolean success){
        this.success = success;
    }

    public boolean debitAmount(String id, int add)
    {
        setSuccess(true);
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
                    Log.d("Test",""+success);
                    return;
                }
                else
                {
                    setSuccess(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.d("Test","kok "+success);
        return success;


    }

    private void setAmount(String uid, Integer value) {
        walletReferences = FirebaseDatabase.getInstance().getReference("wallet").child(uid);
        walletReferences.setValue(value);
    }



}
