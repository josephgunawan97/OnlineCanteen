package com.example.asus.onlinecanteen.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.OrderDetailAdapter;
import com.example.asus.onlinecanteen.adapter.TransactionDetailAdapter;
import com.example.asus.onlinecanteen.model.Transaction;
import com.example.asus.onlinecanteen.utils.WalletUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MerchantOrderDetailActivity extends AppCompatActivity {

    //Initialize views
    TextView transactiondate, username, location, grandTotal, orderStatus;

    Transaction transaction;
    ArrayList<Transaction> transactionHistory;
    RecyclerView itemsRecyclerView;
    OrderDetailAdapter orderDetailAdapter;
    RecyclerView.LayoutManager layoutManager;

    Button acceptButton, declineButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_order_detail);

        Intent intent = getIntent();
        int pos = intent.getIntExtra("Position", 0);
        transactionHistory =(ArrayList<Transaction>) intent.getSerializableExtra("Transaction");

        transaction = transactionHistory.get(pos);

        mAuth = FirebaseAuth.getInstance();

        //Initialize views
        transactiondate = findViewById(R.id.transaction_date);
        username = findViewById(R.id.username);
        location = findViewById(R.id.user_location);
        grandTotal = findViewById(R.id.transaction_detail_amount);
        orderStatus = findViewById(R.id.order_status);
        acceptButton = findViewById(R.id.acceptOrder);
        declineButton = findViewById(R.id.declineOrder);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalletUtil walletUtil = new WalletUtil();
                walletUtil.debitAmount(mAuth.getCurrentUser().getUid(),transaction.getTotalPrice());
                Toast.makeText(getApplicationContext(),"Order accepted",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MerchantOrderDetailActivity.this, MainActivityMerchant.class);
                startActivity(intent);
                finish();
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Order declined",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MerchantOrderDetailActivity.this, MainActivityMerchant.class);
                startActivity(intent);
                finish();
            }
        });

        //Set views
        transactiondate.setText(Transaction.getPurchasedDateString(transaction.getPurchaseDate()));
        username.setText(transaction.getName());
        location.setText(transaction.getLocation());
        grandTotal.setText("Rp " + String.valueOf(transaction.getTotalPrice()));
        orderStatus.setText(String.valueOf(transaction.getDeliveryStatus())); //TO BE CHANGED LATER

        //Adapter for order items list
        OrderDetailAdapter detailAdapter = new OrderDetailAdapter(transaction.getItems());
        itemsRecyclerView = findViewById(R.id.transaction_detail_items);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        itemsRecyclerView.setLayoutManager(layoutManager);
        itemsRecyclerView.setAdapter(detailAdapter);
    }
}
