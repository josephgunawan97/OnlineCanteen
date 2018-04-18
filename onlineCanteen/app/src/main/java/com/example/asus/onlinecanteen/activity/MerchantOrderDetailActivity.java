package com.example.asus.onlinecanteen.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import com.example.asus.onlinecanteen.model.PurchasedItem;
import com.example.asus.onlinecanteen.model.Transaction;
import com.example.asus.onlinecanteen.utils.ProductsUtil;
import com.example.asus.onlinecanteen.utils.WalletUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
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
    DatabaseReference reference,notificationRef, decnotificationRef;

    private ArrayList<PurchasedItem> transactionItems;

    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_order_detail);


        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("transactions");
        notificationRef = firebaseDatabase.getReference("ordernotifications");
        decnotificationRef = firebaseDatabase.getReference("orderdeclinenotifications");

        intent = getIntent();
        pos = intent.getIntExtra("Position", 0);

        //need to be check!
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


        if(transaction.getDeliveryStatus()==0){
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    findViewById(R.id.progress_bar_layout).setVisibility(View.VISIBLE);

                    WalletUtil walletUtilUser = new WalletUtil();
                    walletUtilUser.creditAmount(transaction.getUid(),transaction.getTotalPrice());

                    Query query = reference.orderByChild("purchaseDate").equalTo(transaction.getPurchaseDate());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                Transaction newtrans = child.getValue(Transaction.class);
                                child.getRef().child("deliveryStatus").setValue(1);
                                decreaseStock(transaction.getItems());

                                HashMap<String,String> notificationData = new HashMap<>();
                                notificationData.put("from", newtrans.getSid());
                                notificationData.put("type", "order accepted");
                                notificationRef.child(newtrans.getUid()).push().setValue(notificationData);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

/*                    Intent intent = new Intent(MerchantOrderDetailActivity.this, MainActivityMerchant.class);
                    startActivity(intent);
                    finish();*/
                }
            });

            declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"Order declined",Toast.LENGTH_LONG).show();

                    Query query = reference.orderByChild("purchaseDate").equalTo(transaction.getPurchaseDate());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                Transaction newtrans = child.getValue(Transaction.class);
                                child.getRef().child("deliveryStatus").setValue(4);

                                HashMap<String,String> notificationData = new HashMap<>();
                                notificationData.put("from", newtrans.getSid());
                                notificationData.put("type", "order declined");
                                decnotificationRef.child(newtrans.getUid()).push().setValue(notificationData);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    finish();
                }
            });
        }
        else {
            acceptButton.setClickable(false);
            acceptButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            declineButton.setClickable(false);
            declineButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        }

        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),QrTransactionActivity.class);
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
        orderStatus.setText(statusString(transaction.getDeliveryStatus())); //TO BE CHANGED LATER

        //Adapter for order items list
        detailAdapter = new OrderDetailAdapter(transaction.getItems());
        itemsRecyclerView = findViewById(R.id.transaction_detail_items);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        itemsRecyclerView.setLayoutManager(layoutManager);
        itemsRecyclerView.setAdapter(detailAdapter);

    }

    private void decreaseStock(final HashMap<String, HashMap<String, Integer>> details) {
        final ArrayList<Object> keys = new ArrayList<>(Arrays.asList(details.keySet().toArray()));

        final HashMap<String, Product> products = new HashMap<>();
        Query productQuery = ProductsUtil.query("tokoId", transaction.getSid());
        productQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    for(Object keyObject : keys) {
                        String key = (String) keyObject;
                        if(product.getName().equals(key)) {
                            snapshot.child("stock").getRef().setValue(product.getStock() - details.get(key).get("quantity"));
                            keys.remove(key);
                        }
                    }
                }
                if(keys.size() == 0) {
                    Toast.makeText(MerchantOrderDetailActivity.this, "Order Accepted", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.progress_bar_layout).setVisibility(View.GONE);
                    finish();
                } else {
                    Toast.makeText(MerchantOrderDetailActivity.this, "Not all stock updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private String statusString(int deliveryStatus) {

        switch(deliveryStatus){
            case 0 : return "Waiting order to be accepted!";
            case 1 : return "Order accepted!";
            case 2 : return "Your order is on the way!";
            case 3 : return "Order sent!";
            case 4 : return "Order declined!";
            default : return "Status not found!";
        }

    }

    public void updateOrder(final Transaction transactionlocal){
        final DatabaseReference productDatabase= FirebaseDatabase.getInstance().getReference();
        Log.i(MerchantOrderDetailActivity.class.getSimpleName(), "BEFORE UPDATE TRANS "+ value);

        productDatabase.child("transactions").orderByChild(value).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    Transaction transaction = dataSnapshot.child(value).getValue(Transaction.class);

                    Log.d("TEST",""+transaction.getSid().toString());

                    if(transaction.getPurchaseDate()==transactionlocal.getPurchaseDate()){

                    HashMap<String, Object> result = new HashMap<>();
                    //result.put("imageUrl", );
                    result.put("deliveryStatus", 3);

                    //Merchant only get money when product successfully sent
                    WalletUtil walletUtil = new WalletUtil();
                    walletUtil.debitAmount(transaction.getSid(),transaction.getTotalPrice());

                    Log.i(MerchantOrderDetailActivity.class.getSimpleName(), "UPDATE TRANS1 "+ reference.child(value).getKey());
                    reference.child(value).updateChildren(result);

                    String transtemp = reference.child(value).getKey();

                    productDatabase.child("transactions").child(transtemp).child("deliveryStatus").setValue(3);


                    //Log.i(MerchantOrderDetailActivity.class.getSimpleName(), "UPDATE TRANS "+ reference.child(value).ge);

                }
                else {
                        alert();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        detailAdapter.notifyDataSetChanged();
        Log.i(MerchantOrderDetailActivity.class.getSimpleName(), "UPDATE TRANS "+ transaction.getDeliveryStatus());
    }

    private void alert() {
        new AlertDialog.Builder(this)
                .setTitle("ERROR")
                .setMessage("Transaction not valid")
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(MerchantOrderDetailActivity.class.getSimpleName(), "UPDATE TRANS1 MASUK");
        super.onActivityResult(requestCode, resultCode, data);
                if (resultCode == RESULT_OK && requestCode == SECOND_ACTIVITY_REQUEST_CODE) {

                    if(data.hasExtra("result")){

                        value = data.getStringExtra("result");
                        if(transaction.getDeliveryStatus()==3)
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
                            updateOrder(transaction);
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
        finish();
    }
}

