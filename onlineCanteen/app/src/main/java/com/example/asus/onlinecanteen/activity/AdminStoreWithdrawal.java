package com.example.asus.onlinecanteen.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.utils.WalletUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class AdminStoreWithdrawal extends AppCompatActivity {

    EditText emailET;
    EditText passwordET;
    EditText amountET;
    Button withdrawButton;

    String email, password;
    int amount;

    FirebaseAuth firebaseAuth;
    DatabaseReference emailDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_store_withdrawal);

        firebaseAuth = FirebaseAuth.getInstance();

        emailET = (EditText) findViewById(R.id.withdrawEmail);
        amountET = (EditText) findViewById(R.id.withdrawAmount);
        withdrawButton = (Button) findViewById(R.id.changeButton);


        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm())
                {
                    email = emailET.getText().toString();
                    email = email.replaceAll(Pattern.quote("."),",");
                    amount = Integer.parseInt(amountET.getText().toString());

                    emailDatabase = FirebaseDatabase.getInstance().getReference();

                    emailDatabase.child("emailtouid").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                WalletUtil walletUtil = new WalletUtil();
                                String id = dataSnapshot.getValue().toString();
                                walletUtil.creditAmount(id,amount);
                                Intent intent = new Intent(AdminStoreWithdrawal.this, MainActivityAdmin.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


            }
        });
    }

    public boolean validateForm() {
        String email = new String(emailET.getText().toString());
        String amount = new String(amountET.getText().toString());

        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            emailET.setError("Email is required");
            valid = false;
        }

        if (TextUtils.isEmpty(amount)) {
            amountET.setError("Amount is required");
            valid = false;
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
