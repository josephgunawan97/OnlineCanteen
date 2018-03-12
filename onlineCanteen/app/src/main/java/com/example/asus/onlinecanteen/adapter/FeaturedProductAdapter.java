package com.example.asus.onlinecanteen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.onlinecanteen.R;

import java.util.ArrayList;

/**
 * Created by Steven Albert on 3/12/2018.
 */

public class FeaturedProductAdapter extends RecyclerView.Adapter<FeaturedProductAdapter.ViewHolder> {

    private ArrayList<String> featuredProducts;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_product_adapter_item, parent, false);
        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get Product Featured Item
        String product = featuredProducts.get(position);
        // Set information
        holder.featuredProductNameTextView.setText(product);
    }

    @Override
    public int getItemCount() {
        if(featuredProducts != null) return featuredProducts.size();
        else return 0;
    }

    public void setFeaturedProducts(ArrayList<String> featuredProducts) {
        this.featuredProducts = featuredProducts;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView featuredProductImageView;
        public TextView featuredProductNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            featuredProductImageView = itemView.findViewById(R.id.featured_product_image);
            featuredProductNameTextView = itemView.findViewById(R.id.featured_product_name);
        }
    }
}
