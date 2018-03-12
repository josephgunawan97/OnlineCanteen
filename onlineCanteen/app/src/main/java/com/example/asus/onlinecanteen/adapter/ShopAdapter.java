package com.example.asus.onlinecanteen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.model.Store;

import java.util.ArrayList;

/**
 * Created by Steven Albert on 3/12/2018.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    private ArrayList<Store> shops;

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

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView shopNameTextView;
        public TextView shopLocationTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            shopNameTextView = itemView.findViewById(R.id.shop_name_text_view);
            shopLocationTextView = itemView.findViewById(R.id.shop_location_text_view);
        }
    }
}
