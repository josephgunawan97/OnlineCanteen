package com.example.asus.onlinecanteen.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.model.Store;

import java.util.ArrayList;

/**
 * Created by Steven Albert on 3/12/2018.
 */

public class UserStoreAdapter extends RecyclerView.Adapter<UserStoreAdapter.ViewHolder> {

    /**
     * Interface for click listener on caller
     */
    public interface StoreItemClickHandler {

        /**
         * Handle the transaction item which is clicked in the recycler view
         * @param store clicked transaction item
         */
        void onClickHandler(Store store);
    }

    private StoreItemClickHandler storeItemClickHandler;

    private ArrayList<Store> stores;

    public UserStoreAdapter(StoreItemClickHandler storeItemClickHandler) {
        this.storeItemClickHandler = storeItemClickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_adapter_item, parent, false);
        return new UserStoreAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get current store item
        Store store = stores.get(position);
        // Set information on view
        holder.storeNameTextView.setText(store.getStoreName());
        holder.storeLocationTextView.setText(store.getLocation());
        if(store.getImg() != null) {
            Glide.with(holder.storeImageView.getContext())
                    .load(store.getImg())
                    .into(holder.storeImageView);
        }
    }

    @Override
    public int getItemCount() {
        if(stores != null) return stores.size();
        else return 0;
    }

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
        notifyDataSetChanged();
    }

    public void addStore(Store store) {
        if(this.stores == null) {
            this.stores = new ArrayList<>();
        }
        this.stores.add(store);
        notifyDataSetChanged();
    }

    public void removeAllStores() {
        this.stores.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView storeImageView;
        public TextView storeNameTextView;
        public TextView storeLocationTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            storeImageView = itemView.findViewById(R.id.store_picture_image_view);
            storeNameTextView = itemView.findViewById(R.id.store_name_text_view);
            storeLocationTextView = itemView.findViewById(R.id.store_location_text_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Store store = stores.get(getAdapterPosition());
            if(storeItemClickHandler != null) storeItemClickHandler.onClickHandler(store);
        }
    }
}
