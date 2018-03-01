package com.example.asus.onlinecanteen.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.model.Cart;

import java.util.List;

/**
 * Created by ASUS on 3/2/2018.
 */

public class CartActivityAdapter extends ArrayAdapter<Cart> {

    public CartActivityAdapter(Activity context, List<Cart> cart) {
        super(context, R.layout.cart_adapter_list, cart);
    }

    public View getView(int position, View view, ViewGroup parent) {
        final CartActivityAdapter.CartHolder holder;
        if (view == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            view = inflater.inflate(R.layout.cart_adapter_list, parent, false);
            holder = new CartHolder();

            // Initialize views
            holder.productName = view.findViewById(R.id.itemName);
            holder.qtyXprice = view.findViewById(R.id.priceXqty);
            holder.total = view.findViewById(R.id.totalPrice);
            holder.cancelOrder = view.findViewById(R.id.deleteItem);


            view.setTag(holder);
        }else{
            holder = (CartHolder)view.getTag();
        }

        // Get product
        final Cart cartItems = getItem(position);

        //Set Texts
        holder.productName.setText(cartItems.getProductName());
        holder.qtyXprice.setText(cartItems.getQuantity() + "x Rp " + cartItems.getProductPrice());
        holder.total.setText("Rp " + cartItems.getTotalPrice(cartItems));

        //Cancel Order
        holder.cancelOrder.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){
                cartItems.setQuantity(0);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    static class CartHolder {
        TextView productName;
        TextView qtyXprice;
        TextView total;
        Button cancelOrder;
    }
}
