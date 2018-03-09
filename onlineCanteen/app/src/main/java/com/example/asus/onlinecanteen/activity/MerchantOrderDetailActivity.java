package com.example.asus.onlinecanteen.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.TransactionDetailAdapter;
import com.example.asus.onlinecanteen.model.Transaction;

public class MerchantOrderDetailActivity extends AppCompatActivity {

    //Initialize views
    TextView transactiondate, username, location, grandTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_order_detail);

        transactiondate = findViewById(R.id.transaction_date);
        username = findViewById(R.id.username);
        location = findViewById(R.id.user_location);
        grandTotal = findViewById(R.id.transaction_detail_amount);
    }
}
