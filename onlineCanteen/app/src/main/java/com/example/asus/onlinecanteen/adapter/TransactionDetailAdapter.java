package com.example.asus.onlinecanteen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.asus.onlinecanteen.R;

/**
 * Created by Steven Albert on 3/1/2018.
 */

public class TransactionDetailAdapter extends RecyclerView.Adapter<TransactionDetailAdapter.ViewHolder> {

    private ArrayList<TransactionItem> transactionItems;

    public TransactionDetailAdapter(HashMap<String, HashMap<String, Integer>> details) {
        this.transactionItems = convertTransactionDetails(details);
    }

    private ArrayList<TransactionItem> convertTransactionDetails(HashMap<String, HashMap<String, Integer>> details) {
        ArrayList<TransactionItem> transactionItems = new ArrayList<>();

        Object[] keys = details.keySet().toArray();
        HashMap<String, Integer> desc;
        TransactionItem item;
        for(Object key : keys) {
            desc = details.get(key);
            item = new TransactionItem((String) key, desc.get("quantity"), desc.get("unit_price"));
            transactionItems.add(item);
        }

        return transactionItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_detail_adapter_item, parent, false);

        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TransactionItem item = transactionItems.get(position);

        String quantityFormat = String.valueOf(item.quantity) + " x @Rp " + String.valueOf(item.unitPrice);
        // Fill the TextViews
        holder.itemNameTextView.setText(item.itemName);
        holder.quantityTextView.setText(quantityFormat);
        holder.totalPriceTextView.setText("Rp " + String.valueOf(item.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return transactionItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemNameTextView;
        public TextView quantityTextView;
        public TextView totalPriceTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            itemNameTextView = itemView.findViewById(R.id.transaction_detail_adapter_item_name);
            quantityTextView = itemView.findViewById(R.id.transaction_detail_adapter_item_quantity);
            totalPriceTextView = itemView.findViewById(R.id.transaction_detail_adapter_item_total_price);
        }
    }

    private class TransactionItem {
        public String itemName;
        public int quantity;
        public int unitPrice;

        public TransactionItem(String itemName, int quantity, int unitPrice) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        public int getTotalPrice() {
            return quantity * unitPrice;
        }
    }
}
