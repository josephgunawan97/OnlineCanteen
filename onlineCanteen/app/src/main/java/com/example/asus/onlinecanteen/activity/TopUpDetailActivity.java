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
import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.TopUp;
import com.example.asus.onlinecanteen.model.Transaction;
import com.example.asus.onlinecanteen.model.User;
import com.example.asus.onlinecanteen.utils.WalletUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class TopUpDetailActivity extends AppCompatActivity {

    TextView transactiondate, username, bankname, amount, transfername;
    ImageView transferproof;
    Button accept;
    TopUp topUp;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    Intent intent;
    int pos;
    ArrayList<TopUp> topupHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_topup_request);

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("topuprequest");
        intent = getIntent();
        pos = intent.getIntExtra("Position",0);
        topupHistory = (ArrayList<TopUp>) intent.getSerializableExtra("topup");

        topUp = topupHistory.get(pos);

        mAuth = FirebaseAuth.getInstance();


        transactiondate = findViewById(R.id.topup_detail_topup_date);
        username = findViewById(R.id.request_name);
        bankname = findViewById(R.id.bank_name);
        transfername = findViewById(R.id.trans_name);
        amount = findViewById(R.id.topup_detail_amount);
        accept = findViewById(R.id.accept_request);
        transferproof = findViewById(R.id.proofdetail);

        transactiondate.setText(topUp.getRequestDateString(topUp.getRequestdate()));
        transfername.setText(topUp.getTransfername());
        bankname.setText(topUp.getBank());
        amount.setText("Top-up Amount : "+Integer.toString(topUp.getAmount()));
        if(topUp.getRequeststatus()==1)
        {
            accept.setEnabled(false);
        }

        if(topUp.getProofpicUrl() != null) {
            Glide.with(transferproof.getContext())
                    .load(topUp.getProofpicUrl())
                    .into(transferproof);
        }


        FirebaseDatabase.getInstance().getReference("users").child(topUp.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TopUpDetailActivity.class.getSimpleName(), "TRANS + "+ dataSnapshot.getValue());

                username.setText(dataSnapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("wallet").child(topUp.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        WalletUtil walletUtil = new WalletUtil();
                        walletUtil.debitAmount(topUp.getUid(), topUp.getAmount());
                        Toast.makeText(getApplicationContext(), "Request accepted", Toast.LENGTH_LONG).show();
                        updateReq();
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
            }
        });
    }

    public void updateReq(){
        DatabaseReference topUpDatabase= FirebaseDatabase.getInstance().getReference();

        topUpDatabase.child("topuprequest").orderByChild("uid").equalTo(topUp.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot productSnapshot: dataSnapshot.getChildren()) {
                    if (productSnapshot.child("requestdate").getValue().equals(topUp.getRequestdate())) {

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