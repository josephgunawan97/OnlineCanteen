package com.example.asus.onlinecanteen.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.model.Cart;
import com.example.asus.onlinecanteen.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuListAdapter extends ArrayAdapter<Product> {

    HashMap<String, Integer> Order = new HashMap<>();
    ArrayList<Cart> cart = new ArrayList<>();;
    Product product;
    Cart cartItem;
    DatabaseReference storeDatabase;
    String value;

    public MenuListAdapter(Activity context, List<Product> products) {
        super(context, R.layout.menu_adapter_list, products);
    }

    public View getView(final int position,View view,ViewGroup parent) {
        final OrderHolder holder;
        if(view == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            view = inflater.inflate(R.layout.menu_adapter_list, parent,false);

            holder = new OrderHolder();

            // Initialize views
            holder.txtTitle = view.findViewById(R.id.itemName);
            holder.imageView = view.findViewById(R.id.icon);
            holder.extratxt = view.findViewById(R.id.price);
            holder.seller = view.findViewById(R.id.seller);
            holder.quantityOrder = view.findViewById(R.id.quantityOrder);
            holder.increaseOrder = view.findViewById(R.id.increaseOrder);
            holder.decreaseOrder = view.findViewById(R.id.decreaseOrder);
            holder.increaseOrder.setTag(position);

            view.setTag(holder);
        }else{
            holder = (OrderHolder)view.getTag();
        }
        // Get product
        product = getItem(position);

        //Initialize HashMap for Order Quantity
        if(Order.get(product.getName()) == null) {
            Order.put(product.getName(), 0);
        }

        //Make Array List for Cart
        cartItem = new Cart(product.getName(), product.getPrice(), 0);
        if(!cart.contains(cartItem)){
            cart.add(cartItem);
        }

        //Set Texts
        holder.txtTitle.setText(product.getName());
        holder.price = product.getPrice();
        holder.extratxt.setText("Rp " + product.getPrice());
        holder.quantityOrder.setText(String.valueOf(Order.get(holder.txtTitle.getText().toString())));
        
        //Set seller Text
        String id = product.getTokoId();
        String name = null;
        storeDatabase = FirebaseDatabase.getInstance().getReference();
        storeDatabase.child("store").child(id).child("storeName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                value = (String) snapshot.getValue();
                holder.seller.setText("Seller : " + value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        // Get image
        if(product.getImageUrl() != null) {
            Glide.with(holder.imageView.getContext())
                    .load(product.getImageUrl())
                    .into(holder.imageView);
        }

        //Increase Order
        holder.increaseOrder.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){
                //Change item's quantity in array list
                int qty = Order.get(holder.txtTitle.getText().toString()) + 1;
                Cart cartItem = new Cart(holder.txtTitle.getText().toString(), holder.price, qty);
                cart.set(position, cartItem);

                //Change views
                Order.put(cartItem.getProductName(), cartItem.getQuantity());
                holder.quantityOrder.setText(String.valueOf(Order.get(cartItem.getProductName())));
            }
        });

        //Decrease Order
        holder.decreaseOrder.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){
                if(Order.get(holder.txtTitle.getText().toString()) > 0) {
                    //Change item's quantity in array list
                    int qty = Order.get(holder.txtTitle.getText().toString()) - 1;
                    Cart cartItem = new Cart(holder.txtTitle.getText().toString(), holder.price, qty);
                    cart.set(position, cartItem);

                    //Change views
                    Order.put(cartItem.getProductName(), cartItem.getQuantity());
                    holder.quantityOrder.setText(String.valueOf(Order.get(cartItem.getProductName())));
                }
            }
        });
        return view;
    }


    //Return cart arraylist
    public ArrayList<Cart> getList(){
        return cart;
    }

    public String getSeller() {return value;}

    static class OrderHolder {
        TextView quantityOrder;
        Button decreaseOrder;
        Button increaseOrder;
        TextView txtTitle;
        ImageView imageView;
        TextView extratxt;
        TextView seller;
        int price;
    }
}