package com.example.asus.onlinecanteen;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.onlinecanteen.model.Product;

import java.util.List;

public class MenuListAdapter extends ArrayAdapter<Product> {

    public MenuListAdapter(Activity context, List<Product> products) {
        super(context, R.layout.list, products);
    }

    public View getView(int position,View view,ViewGroup parent) {

        if(view == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            view = inflater.inflate(R.layout.list, parent,false);
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
        return view;
    }
}