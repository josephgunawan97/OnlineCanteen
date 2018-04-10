package com.example.asus.onlinecanteen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.CartActivityAdapter;
import com.example.asus.onlinecanteen.adapter.MenuListAdapter;
import com.example.asus.onlinecanteen.model.Cart;
import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.PurchasedItem;
import com.example.asus.onlinecanteen.model.Transaction;
import com.example.asus.onlinecanteen.utils.TransactionUtil;
import com.example.asus.onlinecanteen.utils.WalletUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class CartActivity extends AppCompatActivity {

    ListView cartList;
    TextView grandTotal, deliveryFee;
    EditText locationEditText;
    Button orderButton;
    CartActivityAdapter cartActivityAdapter;
    ArrayList<Cart> cart;
    int total;
    FirebaseUser user;
    WalletUtil walletUtil;
    FirebaseAuth mAuth;
    DatabaseReference walletRef, merchantRef, notificationRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cart = (ArrayList<Cart>) getIntent().getSerializableExtra("Cart");
        cartActivityAdapter = new CartActivityAdapter(this, cart);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        walletUtil = new WalletUtil();

        notificationRef = FirebaseDatabase.getInstance().getReference().child("notifications");

        //Calculating the grand total
        for(Cart c: cart){
            total += c.getTotalPrice(c);
        }
        int deliveryfeevalue=0;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            deliveryfeevalue = bundle.getInt("deliveryfee");
        }

        // Initialize ListView
        cartList = findViewById(R.id.cartList);
        grandTotal = findViewById(R.id.grandTotal);
        deliveryFee = findViewById(R.id.deliveryFee);
        orderButton = findViewById(R.id.OrderButton);
        locationEditText = findViewById(R.id.userLocation);

        //Set views
        grandTotal.setText("TOTAL: Rp " + total);
        deliveryFee.setText("Delivery Fee : Rp " + deliveryfeevalue);

        cartList.setAdapter(cartActivityAdapter);

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!locationEditText.getText().toString().isEmpty()) {

                    //Make new transaction
                    walletRef = FirebaseDatabase.getInstance().getReference().child("wallet");
                    walletRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists())
                            {
                                String valueString = dataSnapshot.getValue().toString();
                                int walletCash = Integer.parseInt(valueString);

                                //if wallet money >= total
                                if(walletCash>=total)
                                {
                                    ArrayList<PurchasedItem> items = new ArrayList<>();
                                    for (Cart c : cart) {
                                        PurchasedItem item = new PurchasedItem(c.getProductName(), c.getProductPrice(), c.getQuantity());
                                        items.add(item);
                                    }

                                    Intent intent = getIntent();
                                    Transaction transaction = new Transaction(intent.getStringExtra("Seller"), FirebaseAuth.getInstance().getUid(), items, locationEditText.getText().toString());
                                    TransactionUtil.insert(transaction);

                                    Toast.makeText(getApplicationContext(), "Transcation done", Toast.LENGTH_SHORT).show();
                                    setResult(RESULT_OK);

                                    //Make notification to the seller
                                    HashMap<String,String> notificationData = new HashMap<>();
                                    notificationData.put("from", transaction.getUid());
                                    notificationData.put("type", "new order");
                                    notificationRef.child(transaction.getSid()).push().setValue(notificationData);

                                    //Go back to main menu
                                    finish();
                                }
                                else {

                                    Toast.makeText(getApplicationContext(), "Not enough wallet cash, please top-up at our counter",
                                            Toast.LENGTH_SHORT).show();
                                    finish();

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }else{
                    //Alert dialog if the location is not filled in
                    AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                    builder.setMessage("Please enter your location!")
                            .setCancelable(false)
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.setTitle("Error");
                    alert.show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
