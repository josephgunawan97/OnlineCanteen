package com.example.asus.onlinecanteen.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.OrderDetailAdapter;
import com.example.asus.onlinecanteen.adapter.TransactionDetailAdapter;
import com.example.asus.onlinecanteen.model.Transaction;

import java.util.ArrayList;

public class MerchantOrderDetailActivity extends AppCompatActivity {

    //Initialize views
    TextView transactiondate, username, location, grandTotal, orderStatus;

    Transaction transaction;
    ArrayList<Transaction> transactionHistory;
    RecyclerView itemsRecyclerView;
    OrderDetailAdapter orderDetailAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_order_detail);

        Intent intent = getIntent();
        int pos = intent.getIntExtra("Position", 0);
        transactionHistory =(ArrayList<Transaction>) intent.getSerializableExtra("Transaction");

        transaction = transactionHistory.get(pos);

        //Initialize views
        transactiondate = findViewById(R.id.transaction_date);
        username = findViewById(R.id.username);
        location = findViewById(R.id.user_location);
        grandTotal = findViewById(R.id.transaction_detail_amount);
        orderStatus = findViewById(R.id.order_status);

        //Set views
        transactiondate.setText(String.valueOf(transaction.getPurchaseDate()));
        username.setText(transaction.getName());
        location.setText(transaction.getLocation());
        grandTotal.setText("Rp " + String.valueOf(transaction.getTotalPrice()));
        orderStatus.setText("Pending"); //TO BE CHANGED LATER

        //Adapter for order items list
        OrderDetailAdapter detailAdapter = new OrderDetailAdapter(transaction.getItems());
        itemsRecyclerView = findViewById(R.id.transaction_detail_items);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        itemsRecyclerView.setLayoutManager(layoutManager);
        itemsRecyclerView.setAdapter(detailAdapter);
    }
}
