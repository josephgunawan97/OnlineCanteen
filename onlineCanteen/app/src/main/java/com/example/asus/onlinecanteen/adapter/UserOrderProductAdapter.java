package com.example.asus.onlinecanteen.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.model.Cart;
import com.example.asus.onlinecanteen.model.Product;

import java.util.ArrayList;

/**
 * Created by Steven Albert on 3/17/2018.
 */

public class UserOrderProductAdapter extends RecyclerView.Adapter<UserOrderProductAdapter.ViewHolder> {

    private ArrayList<Product> products;
    private ArrayList<Cart> orders;

    public UserOrderProductAdapter(ArrayList<Product> products) {
        if(products != null) {
            for(Product product : products) {
                addProduct(product);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_order_product_adapter_item, parent, false);
        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get product and cart
        Cart cart = orders.get(position);
        Product product = products.get(position);
        // Set on view
        holder.userOrderProductNameTextView.setText(cart.getProductName());
        holder.userOrderProductPriceTextView.setText("Rp " + String.valueOf(cart.getProductPrice()));
        holder.userOrderProductQuantityTextView.setText(String.valueOf(cart.getQuantity()));
        // Get image
        if(product.getImageUrl() != null) {
            Glide.with(holder.userOrderProductImageView.getContext())
                    .load(product.getImageUrl())
                    .into(holder.userOrderProductImageView);
        }
    }

    @Override
    public int getItemCount() {
        if(orders != null) return orders.size();
        else return 0;
    }

    public void addProduct(Product product) {
        if(product == null) return;
        if(products == null) {
            products = new ArrayList<>();
        }
        if(orders == null) {
            orders = new ArrayList<>();
        }

        products.add(product);
        orders.add(new Cart(product.getName(), product.getPrice(), 0));

        notifyDataSetChanged();
    }

    public ArrayList<Cart> getOrders() {
        return orders;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView userOrderProductImageView;
        public TextView userOrderProductNameTextView;
        public TextView userOrderProductPriceTextView;
        public TextView userOrderProductQuantityTextView;
        public Button userOrderProductIncreaseButton;
        public Button userOrderProductDecreaseButton;

        public ViewHolder(View itemView) {
            super(itemView);

            userOrderProductImageView = itemView.findViewById(R.id.user_order_product_image_view);
            userOrderProductNameTextView = itemView.findViewById(R.id.user_order_product_name_text_view);
            userOrderProductPriceTextView = itemView.findViewById(R.id.user_order_product_price_text_view);
            userOrderProductQuantityTextView = itemView.findViewById(R.id.user_order_product_quantity_text_view);
            userOrderProductIncreaseButton = itemView.findViewById(R.id.user_order_product_inc_button);
            userOrderProductDecreaseButton = itemView.findViewById(R.id.user_order_product_dec_button);

            userOrderProductIncreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Cart order = orders.get(position);
                    Product product = products.get(position);
                    order.increaseQuantity(1);
                    if(order.getQuantity() > product.getStock()) {
                        order.setQuantity(product.getStock());
                    }

                    userOrderProductQuantityTextView.setText(String.valueOf(order.getQuantity()));
                }
            });

            userOrderProductDecreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Cart order = orders.get(position);
                    order.decreaseQuantity(1);
                    if(order.getQuantity() < 0) {
                        order.setQuantity(0);
                    }

                    userOrderProductQuantityTextView.setText(String.valueOf(order.getQuantity()));
                }
            });
        }
    }
}
