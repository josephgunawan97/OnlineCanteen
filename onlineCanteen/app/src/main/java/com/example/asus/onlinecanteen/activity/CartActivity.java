package com.example.asus.onlinecanteen.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.CartActivityAdapter;
import com.example.asus.onlinecanteen.model.Cart;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ListView cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Product List
        ArrayList<Cart> cartItemsList = new ArrayList<>();
        CartActivityAdapter cartActivityAdapter = new CartActivityAdapter (this, cartItemsList);

        // Initialize ListView
        cartList = findViewById(R.id.cartList);
        cartList.setAdapter(cartActivityAdapter);
    }
}
