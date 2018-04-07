package com.example.asus.onlinecanteen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.model.TopUp;
import com.example.asus.onlinecanteen.model.Withdraw;
import com.example.asus.onlinecanteen.utils.WalletUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rickhen Hermawan on 03/04/2018.
 */

public class WithdrawDetailActivity extends AppCompatActivity {

    TextView transactiondate, username, bankname, amount, transfername, accountnumber;
    Button accept;
    Withdraw withdraw;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    Intent intent;
    int pos;
    ArrayList<Withdraw> withdrawHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_withdraw_request);

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("withdrawrequest");
        intent = getIntent();
        pos = intent.getIntExtra("Position",0);
        withdrawHistory = (ArrayList<Withdraw>) intent.getSerializableExtra("withdraw");

        withdraw = withdrawHistory.get(pos);

        mAuth = FirebaseAuth.getInstance();


        transactiondate = findViewById(R.id.withdraw_detail_withdraw_date);
        username = findViewById(R.id.request_name);
        bankname = findViewById(R.id.bank_name);
        transfername = findViewById(R.id.trans_name);
        amount = findViewById(R.id.withdraw_detail_amount);
        accept = findViewById(R.id.accept_request);
        accountnumber = findViewById(R.id.request_number);

        transactiondate.setText(withdraw.getRequestDateString(withdraw.getRequestdate()));
        transfername.setText(withdraw.getTransfername());
        bankname.setText(withdraw.getBank());
        accountnumber.setText(Integer.toString(withdraw.getAccountnumber()));
        amount.setText("Withdraw Amount : Rp. "+Integer.toString(withdraw.getAmount())+",-");
        if(withdraw.getRequeststatus()==1)
        {
            accept.setEnabled(false);
        }


        FirebaseDatabase.getInstance().getReference("store").child(withdraw.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(WithdrawDetailActivity.class.getSimpleName(), "TRANS + "+ dataSnapshot.getValue());

                username.setText(dataSnapshot.child("storeName").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("wallet").child(withdraw.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            WalletUtil walletUtil = new WalletUtil();
                            String id = dataSnapshot.getValue().toString();
                            walletUtil.creditAmount(withdraw.getUid(), withdraw.getAmount());
                            Toast.makeText(getApplicationContext(), "Request accepted", Toast.LENGTH_LONG).show();
                            updateReq();
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
            }
        });
    }

    public void updateReq(){
        DatabaseReference withdrawDatabase= FirebaseDatabase.getInstance().getReference();

        withdrawDatabase.child("withdrawrequest").orderByChild("uid").equalTo(withdraw.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot productSnapshot: dataSnapshot.getChildren()) {
                    if (productSnapshot.child("requestdate").getValue().equals(withdraw.getRequestdate())) {

                        HashMap<String, Object> result = new HashMap<>();
                        result.put("requeststatus", 1);
                        reference.child(productSnapshot.getKey()).updateChildren(result);
                        //Log.i(MerchantOrderDetailActivity.class.getSimpleName(), "UPDATE TRANS "+ reference.child(value).ge);
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
