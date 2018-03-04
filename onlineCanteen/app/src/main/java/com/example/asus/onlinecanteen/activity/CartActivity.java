package com.example.asus.onlinecanteen.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.CartActivityAdapter;
import com.example.asus.onlinecanteen.adapter.MenuListAdapter;
import com.example.asus.onlinecanteen.model.Cart;
import com.example.asus.onlinecanteen.model.Product;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ListView cartList;
    TextView grandTotal, deliveryFee;
    CartActivityAdapter cartActivityAdapter;
    ArrayList<Cart> cart;
    int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

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

        //Set views
        grandTotal.setText("Grand Total : Rp " + total);
        deliveryFee.setText("Delivery Fee :         Rp ");

        cartList.setAdapter(cartActivityAdapter);
    }
}
