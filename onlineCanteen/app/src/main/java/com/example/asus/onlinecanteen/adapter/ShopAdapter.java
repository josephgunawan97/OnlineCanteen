package com.example.asus.onlinecanteen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.model.Store;

import java.util.ArrayList;

/**
 * Created by Steven Albert on 3/12/2018.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    /**
     * Interface for click listener on caller
     */
    public interface ShopItemClickHandler {

        /**
         * Handle the transaction item which is clicked in the recycler view
         * @param shop clicked transaction item
         */
        void onClickHandler(Store shop);
    }

    private ShopItemClickHandler shopItemClickHandler;

    private ArrayList<Store> shops;

    public ShopAdapter(ShopItemClickHandler shopItemClickHandler) {
        this.shopItemClickHandler = shopItemClickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_adapter_item, parent, false);
        return new ShopAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get current shop item
        Store shop = shops.get(position);
        // Set information on view
        holder.shopNameTextView.setText(shop.getStoreName());
        holder.shopLocationTextView.setText(shop.getLocation());
    }

    @Override
    public int getItemCount() {
        if(shops != null) return shops.size();
        else return 0;
    }

    public void setShops(ArrayList<Store> shops) {
        this.shops = shops;
        notifyDataSetChanged();
    }

    public void addShop(Store shop) {
        if(this.shops == null) {
            this.shops = new ArrayList<>();
        }
        this.shops.add(shop);
        notifyDataSetChanged();
    }

    public void removeAllShops() {
        this.shops.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView shopImageView;
        public TextView shopNameTextView;
        public TextView shopLocationTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            shopImageView = itemView.findViewById(R.id.shop_picture_image_view);
            shopNameTextView = itemView.findViewById(R.id.shop_name_text_view);
            shopLocationTextView = itemView.findViewById(R.id.shop_location_text_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Store store = shops.get(getAdapterPosition());
            if(shopItemClickHandler != null) shopItemClickHandler.onClickHandler(store);
        }
    }
}
