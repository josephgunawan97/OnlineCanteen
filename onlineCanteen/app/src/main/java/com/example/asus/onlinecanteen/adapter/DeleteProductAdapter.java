package com.example.asus.onlinecanteen.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.Transaction;

import java.util.ArrayList;
import java.util.List;


public class DeleteProductAdapter extends RecyclerView.Adapter<DeleteProductAdapter.ViewHolder> {

    // Transaction History Items
    private ArrayList<Product> productList;

    public interface DeleteClickHandler {

        /**
         * Handle the transaction item which is clicked in the recycler view
         * @param product clicked transaction item
         */
        void onClickHandler(Product product);
    }

    private DeleteClickHandler clickHandler;
    public DeleteProductAdapter(DeleteProductAdapter.DeleteClickHandler handler) {
        this.clickHandler = handler;
    }

    /**
     * Create {@link ViewHolder} instance of the views
     * @param parent ViewGroup instance in which the View is added to
     * @param viewType the view type of the new View
     * @return new ViewHolder instance
     */
    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_delete_adapter_list, parent, false);

        return new ViewHolder(layoutView);
    }

    /**
     * Bind the view with data at the specified position
     * @param holder ViewHolder which should be updated
     * @param position position of items in the adapter
     */
    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        // Get Transaction Item
        Product product = productList.get(position);
        // Set Information on View
        holder.userNameTextView.setText(product.getName());
        holder.priceTextView.setText("Rp."+String.valueOf(product.getPrice()));
        if(holder.checkBox.isChecked()) product.setChecked(true);
        else product.setChecked(false);
        if(product.getImageUrl() != null) {
            Glide.with(holder.imageView.getContext())
                    .load(product.getImageUrl())
                    .into(holder.imageView);
        }

    }
    /**
     * Retrieved the amount of items in adapter
     * @return amount of items in adapter
     */
    @Override public int getItemCount() {
        return productList.size();
    }

    public boolean getChecked( int pos){
        return productList.get(pos).isChecked();
    }
    public void setProductList(ArrayList<Product> products) {
        this.productList = products;
    }
    public void addProductList(Product product) {
        if(product == null) return;
        this.productList.add(product);
        notifyDataSetChanged();
    }
    /**
     * ViewHolder class of {@link DeleteProductAdapter}
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // TextView of store name
        public TextView userNameTextView;
        // TextView of transaction date
        public TextView priceTextView;
        // TextView of payment amount
        public CheckBox checkBox;
        // ImageView of Product
        public ImageView imageView;

        /**
         * Construct {@link ViewHolder} instance
         * @param view layout view of transaction items
         */
        public ViewHolder(View view) {
            super(view);
            // Set the holder attributes
            userNameTextView = view.findViewById(R.id.itemName);
            priceTextView = view.findViewById(R.id.price);
            checkBox = view.findViewById(R.id.checkBox);
            imageView = view.findViewById(R.id.icon);
        }
    }
}
