package com.example.asus.onlinecanteen.activity;

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
import com.example.asus.onlinecanteen.model.Product;

import java.util.List;

public class MenuListAdapter extends ArrayAdapter<Product> {
    //Increase & Decrease Item's Quantity
    TextView quantityOrder;
    Button decreaseOrder, increaseOrder;
    int qty;

    public MenuListAdapter(Activity context, List<Product> products) {
        super(context, R.layout.menu_adapter_list, products);
    }


    public View getView(int position,View view,ViewGroup parent) {

        if(view == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            view = inflater.inflate(R.layout.menu_adapter_list, parent,false);
        }
        // Get product
        Product product = getItem(position);

        // Initialize views
        TextView txtTitle = view.findViewById(R.id.itemName);
        ImageView imageView = view.findViewById(R.id.icon);
        TextView extratxt = view.findViewById(R.id.price);
        TextView seller = view.findViewById(R.id.seller);


        txtTitle.setText(product.getName());
        extratxt.setText("Rp " + product.getPrice());
        seller.setText("Seller : " + product.getTokoId());

        // Get image
        if(product.getImageUrl() != null) {
            Glide.with(imageView.getContext())
                    .load(product.getImageUrl())
                    .into(imageView);
        }

        //Initialize Increase Button, Decrease Button, Qty TextView, and Click Listener
        quantityOrder = view.findViewById(R.id.quantityOrder);
        quantityOrder.setText(String.valueOf(qty));
        increaseOrder = view.findViewById(R.id.increaseOrder);
        increaseOrder.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){
                qty++;
                quantityOrder.setText(String.valueOf(qty));

            }
        });

        decreaseOrder = view.findViewById(R.id.decreaseOrder);
        decreaseOrder.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){
                if(qty > 0) {
                    qty--;
                    quantityOrder.setText(String.valueOf(qty));
                }
            }
        });

        return view;
    }
}