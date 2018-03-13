package com.example.asus.onlinecanteen.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.activity.MerchantOrderDetailActivity;
import com.example.asus.onlinecanteen.fragment.MerchantOrderListFragment;
import com.example.asus.onlinecanteen.model.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven Albert on 2/17/2018.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    // Transaction History Items
    private ArrayList<Transaction> transactionHistory;

    /**
     * Construct {@link OrderAdapter} instance
     *
     * @param history list of transaction history
     */
    public OrderAdapter(@NonNull List<Transaction> history) {
        this.transactionHistory = new ArrayList<>(history);
    }

    /**
     * Create {@link ViewHolder} instance of the views
     * @param parent ViewGroup instance in which the View is added to
     * @param viewType the view type of the new View
     * @return new ViewHolder instance
     */
    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_adapter_item, parent, false);

        return new ViewHolder(layoutView);
    }

    /**
     * Bind the view with data at the specified position
     * @param holder ViewHolder which should be updated
     * @param position position of items in the adapter
     */
    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        // Get Transaction Item
        Transaction transaction = transactionHistory.get(position);
        // Set Information on View
        holder.userNameTextView.setText(transaction.getName());
        Log.i(OrderAdapter.class.getSimpleName(),"NAMA ORDER= "+transaction.getName());
        holder.transactionDateTextView.setText(String.valueOf(transaction.getPurchaseDate()));
        holder.paymentAmountTextView.setText("Rp " + String.valueOf(transaction.getTotalPrice()));
        holder.locationTextView.setText("Location: "+transaction.getLocation());
    }
    /**
     * Retrieved the amount of items in adapter
     * @return amount of items in adapter
     */
    @Override public int getItemCount() {
        return transactionHistory.size();
    }

    public void setTransactionHistory(ArrayList<Transaction> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    /**
     * ViewHolder class of {@link OrderAdapter}
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // TextView of store name
        public TextView userNameTextView;
        // TextView of transaction date
        public TextView transactionDateTextView;
        // TextView of payment amount
        public TextView paymentAmountTextView;
        // TextView of LocationTextView
        public TextView locationTextView;

        /**
         * Construct {@link ViewHolder} instance
         * @param view layout view of transaction items
         */
        public ViewHolder(View view) {
            super(view);
            final Context context = itemView.getContext();
            // Set the holder attributes
            userNameTextView = view.findViewById(R.id.order_name);
            transactionDateTextView = view.findViewById(R.id.order_item_transaction_date);
            paymentAmountTextView = view.findViewById(R.id.order_item_payment_amount);
            locationTextView = view.findViewById(R.id.order_location);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent intent = new Intent(context, MerchantOrderDetailActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
