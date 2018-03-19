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
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ListView cartList;
    TextView grandTotal, deliveryFee;
    EditText locationEditText;
    Button orderButton;
    CartActivityAdapter cartActivityAdapter;
    ArrayList<Cart> cart;
    int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cart = (ArrayList<Cart>) getIntent().getSerializableExtra("Cart");
        cartActivityAdapter = new CartActivityAdapter(this, cart);

        //Calculating the grand total
        for(Cart c: cart){
            total += c.getTotalPrice(c);
        }

        // Initialize ListView
        cartList = findViewById(R.id.cartList);
        grandTotal = findViewById(R.id.grandTotal);
        deliveryFee = findViewById(R.id.deliveryFee);
        orderButton = findViewById(R.id.OrderButton);
        locationEditText = findViewById(R.id.userLocation);

        //Set views
        grandTotal.setText("TOTAL: Rp " + total);
        deliveryFee.setText("Delivery Fee : Rp ");

        cartList.setAdapter(cartActivityAdapter);

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!locationEditText.getText().toString().isEmpty()) {
                    //Make new transaction
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
                    //Go back to main menu
                    finish();
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
