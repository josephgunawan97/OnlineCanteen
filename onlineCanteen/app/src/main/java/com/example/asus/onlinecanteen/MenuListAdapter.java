package com.example.asus.onlinecanteen;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

        txtTitle.setText(product.getProductName());
        extratxt.setText("Rp " + product.getProductPrice());
        seller.setText("Seller : " + product.getTokoId());

        //Initialize Increase Button, Decrease Button, Qty TextView, and Click Listener
        quantityOrder = view.findViewById(R.id.quantityOrder);
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