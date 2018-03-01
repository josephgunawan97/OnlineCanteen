package com.example.asus.onlinecanteen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.model.Transaction;

import java.util.ArrayList;

/**
 * Created by Steven Albert on 2/17/2018.
 */

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder> {

    /**
     * Interface for click listener on caller
     */
    public interface TransactionHistoryClickHandler {

        /**
         * Handle the transaction item which is clicked in the recycler view
         * @param transaction clicked transaction item
         */
        void onClickHandler(Transaction transaction);
    }

    // Transaction History Click Listener
    private TransactionHistoryClickHandler clickHandler;
    // Transaction History Items
    private ArrayList<Transaction> transactionHistory;

    /**
     * Construct {@link TransactionHistoryAdapter} instance
     * @param handler listener when the item of transaction is clicked
     */
    public TransactionHistoryAdapter(TransactionHistoryClickHandler handler) {
        this.clickHandler = handler;
    }

    /**
     * Create {@link ViewHolder} instance of the views
     * @param parent ViewGroup instance in which the View is added to
     * @param viewType the view type of the new View
     * @return new ViewHolder instance
     */
    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_adapter_item, parent, false);

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
        holder.storeNameTextView.setText(transaction.getSid());
        holder.transactionDateTextView.setText(String.valueOf(transaction.getPurchaseDate()));
        holder.paymentAmountTextView.setText("Rp " + String.valueOf(transaction.getTotalPrice()));
    }
    /**
     * Retrieved the amount of items in adapter
     * @return amount of items in adapter
     */
    @Override public int getItemCount() {
        return transactionHistory.size();
    }

    /**
     * Change the data of transaction history
     * @param transactionHistory list of transactions
     */
    public void setTransactionHistory(ArrayList<Transaction> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    /**
     * ViewHolder class of {@link TransactionHistoryAdapter}
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // TextView of store name
        public TextView storeNameTextView;
        // TextView of transaction date
        public TextView transactionDateTextView;
        // TextView of payment amount
        public TextView paymentAmountTextView;

        /**
         * Construct {@link ViewHolder} instance
         * @param view layout view of transaction items
         */
        public ViewHolder(View view) {
            super(view);
            // Set the holder attributes
            storeNameTextView = view.findViewById(R.id.history_item_store_name);
            transactionDateTextView = view.findViewById(R.id.history_item_transaction_date);
            paymentAmountTextView = view.findViewById(R.id.history_item_payment_amount);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(clickHandler != null) clickHandler.onClickHandler(transactionHistory.get(position));
        }
    }
}
