package com.example.asus.onlinecanteen.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.model.Product;

import java.util.ArrayList;

public class UserProductItemAdapter extends RecyclerView.Adapter<UserProductItemAdapter.ViewHolder> {

    private ArrayList<Product> products;

    public UserProductItemAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_adapter_item, parent, false);
        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get product
        Product product = products.get(position);
        // Set on view
        holder.productNameTextView.setText(product.getName());
        holder.productPriceTextView.setText("Rp " + String.valueOf(product.getPrice()));
        // Get image
        if(product.getImageUrl() != null) {
            Glide.with(holder.productImageView.getContext())
                    .load(product.getImageUrl())
                    .into(holder.productImageView);
        }
    }

    @Override
    public int getItemCount() {
        if(products != null) return products.size();
        else return 0;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public void addProduct(Product product) {
        if(this.products == null) {
            this.products = new ArrayList<>();
        }

        this.products.add(product);
        notifyDataSetChanged();
    }

    public void removeAllProducts() {
        if(this.products != null) this.products.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView productImageView;
        public TextView productNameTextView;
        public TextView productPriceTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            productImageView = itemView.findViewById(R.id.product_image_view);
            productNameTextView = itemView.findViewById(R.id.product_name_text_view);
            productPriceTextView = itemView.findViewById(R.id.product_price_text_view);
        }
    }
}
