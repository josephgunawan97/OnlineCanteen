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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven Albert on 2/17/2018.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    // Transaction History Items
    private ArrayList<Transaction> transactionHistory;

    public OrderAdapter(@NonNull List<Transaction> history) {
        this.transactionHistory = new ArrayList<>(history);
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_adapter_item, parent, false);

        return new ViewHolder(layoutView);
    }

    @Override public void onBindViewHolder(final ViewHolder holder, int position) {
        // Get Transaction Item
        final Transaction transaction = transactionHistory.get(position);

        FirebaseDatabase.getInstance().getReference().child("users").child(transaction.getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    transaction.setName(dataSnapshot.child("name").getValue().toString());
                    holder.userNameTextView.setText(dataSnapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.transactionDateTextView.setText(Transaction.getPurchasedDateString(transaction.getPurchaseDate()));
        holder.paymentAmountTextView.setText("Rp " + String.valueOf(transaction.getTotalPrice()));
        holder.locationTextView.setText("Location: "+transaction.getLocation());
    }

    @Override public int getItemCount() {
        return transactionHistory.size();
    }

    public void setTransactionHistory(ArrayList<Transaction> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // TextView of store name
        public TextView userNameTextView;
        // TextView of transaction date
        public TextView transactionDateTextView;
        // TextView of payment amount
        public TextView paymentAmountTextView;
        // TextView of LocationTextView
        public TextView locationTextView;

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
                    intent.putExtra("Transaction", transactionHistory);
                    intent.putExtra("Position", getAdapterPosition());
                    context.startActivity(intent);
                }
            });
        }
    }
}
