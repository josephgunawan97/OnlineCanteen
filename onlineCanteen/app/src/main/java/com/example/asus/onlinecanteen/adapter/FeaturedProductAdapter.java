package com.example.asus.onlinecanteen.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.activity.QrActivity;
import com.example.asus.onlinecanteen.activity.UserStoreProductActivity;
import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.Store;
import com.example.asus.onlinecanteen.utils.AccountUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Steven Albert on 3/12/2018.
 */

public class FeaturedProductAdapter extends RecyclerView.Adapter<FeaturedProductAdapter.ViewHolder> {


    public interface FeaturedProductClickHandler {
        void onFeaturedClickHandler(Store store);
    }

    private ArrayList<Product> featuredProducts;
    private FeaturedProductClickHandler featuredProductClickHandler;

    public FeaturedProductAdapter(FeaturedProductClickHandler featuredProductClickHandler) {
        this.featuredProductClickHandler = featuredProductClickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_product_adapter_item, parent, false);
        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Get Product Featured Item
        final Product product = featuredProducts.get(position);

        FirebaseDatabase.getInstance().getReference("store").child(product.getTokoId()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.featuredProductSellerTextView.setText(dataSnapshot.child("storeName").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // Set information
        holder.featuredProductNameTextView.setText(product.getName());
        if(product.getImageUrl() != null) {
            Glide.with(holder.featuredProductImageView.getContext())
                    .load(product.getImageUrl())
                    .into(holder.featuredProductImageView);
        }
    }

    @Override
    public int getItemCount() {
        if(featuredProducts != null) return featuredProducts.size();
        else return 0;
    }

    public void setFeaturedProducts(ArrayList<Product> featuredProducts) {
        this.featuredProducts = featuredProducts;
        notifyDataSetChanged();
    }

    public void addProduct(Product product) {
        if(this.featuredProducts == null) {
            this.featuredProducts = new ArrayList<>();
        }
        this.featuredProducts.add(product);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView featuredProductImageView;
        public TextView featuredProductNameTextView;
        public TextView featuredProductSellerTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            featuredProductImageView = itemView.findViewById(R.id.featured_product_image);
            featuredProductNameTextView = itemView.findViewById(R.id.featured_product_name);
            featuredProductSellerTextView = itemView.findViewById(R.id.featured_product_seller);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final Product product = featuredProducts.get(getAdapterPosition());
            DatabaseReference tokoRef = FirebaseDatabase.getInstance().getReference("store").child(product.getTokoId());
            tokoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    /*final Context context = itemView.getContext();
                    Store store = snapshot.getValue(Store.class);
                    store.setStoreId(product.getTokoId());
                    AccountUtil.setCurrentAccount(store);
                    Intent intent = new Intent(context, UserStoreProductActivity.class);
                    intent.putExtra(UserStoreProductActivity.CURRENT_STORE_KEY, store);
                    context.startActivity(intent);*/
                    Store store = snapshot.getValue(Store.class);
                    store.setStoreId(product.getTokoId());
                    if(featuredProductClickHandler != null) featuredProductClickHandler.onFeaturedClickHandler(store);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
