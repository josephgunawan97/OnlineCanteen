package com.example.asus.onlinecanteen.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.TransactionDetailAdapter;
import com.example.asus.onlinecanteen.model.Transaction;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionDetailFragment extends Fragment {

    // Transaction which is detailed
    private Transaction transaction;

    // UI Variables
    private TextView transactionDateTextView;
    private TextView storeNameTextView;
    private RecyclerView itemsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView totalAmountTextView;

    public TransactionDetailFragment() {}

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transactionDateTextView = view.findViewById(R.id.transaction_detail_transaction_date);
        storeNameTextView = view.findViewById(R.id.transaction_detail_store_name);
        itemsRecyclerView = view.findViewById(R.id.transaction_detail_items);
        totalAmountTextView = view.findViewById(R.id.transaction_detail_amount);

        transactionDateTextView.setText(Transaction.getPurchasedDateString(transaction.getPurchaseDate()));
        storeNameTextView.setText(transaction.getSid());
        totalAmountTextView.setText("Rp " + String.valueOf(transaction.getTotalPrice()));

        TransactionDetailAdapter detailAdapter = new TransactionDetailAdapter(transaction.getItems());
        layoutManager = new LinearLayoutManager(getContext());

        itemsRecyclerView.setLayoutManager(layoutManager);
        itemsRecyclerView.setAdapter(detailAdapter);
    }
}
