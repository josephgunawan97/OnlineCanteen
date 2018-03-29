package com.example.asus.onlinecanteen.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.OrderDetailAdapter;
import com.example.asus.onlinecanteen.adapter.TransactionDetailAdapter;
import com.example.asus.onlinecanteen.fragment.MainUserFragment;
import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.Transaction;
import com.example.asus.onlinecanteen.utils.WalletUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MerchantOrderDetailActivity extends AppCompatActivity {

    //Initialize views
    TextView transactiondate, username, location, grandTotal, orderStatus;

    Transaction transaction;
    ArrayList<Transaction> transactionHistory;
    RecyclerView itemsRecyclerView;
    OrderDetailAdapter orderDetailAdapter;
    RecyclerView.LayoutManager layoutManager;
    int pos;
    Button acceptButton, declineButton;
    FirebaseAuth mAuth;
    String value;
    ImageButton scanQR;
    Intent intent;
    OrderDetailAdapter detailAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_order_detail);


        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("transactions");
        intent = getIntent();
        pos = intent.getIntExtra("Position", 0);
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
        scanQR = findViewById(R.id.scan_qr);

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

        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),QrActivity.class);
                i.putExtra("Location", "order");
                i.putExtra("Transaction", transactionHistory);
                i.putExtra("Position", pos);

                startActivityForResult(i, SECOND_ACTIVITY_REQUEST_CODE);
            }
        });

        //Set views
        transactiondate.setText(Transaction.getPurchasedDateString(transaction.getPurchaseDate()));
        username.setText(transaction.getName());
        location.setText(transaction.getLocation());
        grandTotal.setText("Rp " + String.valueOf(transaction.getTotalPrice()));
        orderStatus.setText(String.valueOf(transaction.getDeliveryStatus())); //TO BE CHANGED LATER

        //Adapter for order items list
        detailAdapter = new OrderDetailAdapter(transaction.getItems());
        itemsRecyclerView = findViewById(R.id.transaction_detail_items);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        itemsRecyclerView.setLayoutManager(layoutManager);
        itemsRecyclerView.setAdapter(detailAdapter);

    }

    public void updateOrder(){
        DatabaseReference productDatabase= FirebaseDatabase.getInstance().getReference();
        Log.i(MerchantOrderDetailActivity.class.getSimpleName(), "BEFORE UPDATE TRANS "+ value);

        productDatabase.child("transactions").orderByChild(value).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Transaction transaction = dataSnapshot.getValue(Transaction.class);

                HashMap<String, Object> result = new HashMap<>();
                //result.put("imageUrl", );
                result.put("deliveryStatus", 1);
                Log.i(MerchantOrderDetailActivity.class.getSimpleName(), "UPDATE TRANS1 "+ reference.child(value).getKey());
                reference.child(value).updateChildren(result);
                //Log.i(MerchantOrderDetailActivity.class.getSimpleName(), "UPDATE TRANS "+ reference.child(value).ge);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        detailAdapter.notifyDataSetChanged();
        Log.i(MerchantOrderDetailActivity.class.getSimpleName(), "UPDATE TRANS "+ transaction.getDeliveryStatus());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(MerchantOrderDetailActivity.class.getSimpleName(), "UPDATE TRANS1 MASUK");
        super.onActivityResult(requestCode, resultCode, data);
                if (resultCode == RESULT_OK && requestCode == SECOND_ACTIVITY_REQUEST_CODE) {

                    if(data.hasExtra("result")){

                        value = data.getStringExtra("result");
                        if(transaction.getDeliveryStatus()==1)
                        {
                            new AlertDialog.Builder(this)
                                    .setTitle("Transaction")
                                    .setMessage("Transaction already confirmed")
                                    .setPositiveButton("OK", null)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            backToScreen();
                                        }
                                    })
                                    .show();
                        }
                        else{
                            updateOrder();
                            new AlertDialog.Builder(this)
                                    .setTitle("Transaction")
                                    .setMessage("Transaction confirm success")
                                    .setPositiveButton("OK", null)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            backToScreen();
                                        }
                                    })
                                    .show();
                        }
                    }
                }

        }
    private void backToScreen() {
        // GO TO LOGIN PAGE - after success
        Intent intent = new Intent(this, MainActivityMerchant.class);
        startActivity(intent);
        finish();
    }
}

