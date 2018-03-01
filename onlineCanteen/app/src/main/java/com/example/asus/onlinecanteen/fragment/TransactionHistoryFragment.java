package com.example.asus.onlinecanteen.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.TransactionHistoryAdapter;
import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.PurchasedItem;
import com.example.asus.onlinecanteen.model.Transaction;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionHistoryFragment extends Fragment implements TransactionHistoryAdapter.TransactionHistoryClickHandler {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private TransactionDetailHandler transactionDetailHandler;

    public interface TransactionDetailHandler {

        void transactionDetailHandler(Transaction transaction);
    }

    public TransactionHistoryFragment() {}

    public void setTransactionDetailHandler(TransactionDetailHandler transactionDetailHandler) {
        this.transactionDetailHandler = transactionDetailHandler;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.history_title_item);

        ArrayList<Transaction> transactions = getDummyTransactions();
        TransactionHistoryAdapter adapter = new TransactionHistoryAdapter(this);
        adapter.setTransactionHistory(transactions);

        recyclerView = view.findViewById(R.id.history_recycler_view);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<Transaction> getDummyTransactions() {
        ArrayList<PurchasedItem> items = new ArrayList<>();
        items.add(new PurchasedItem(new Product("A", "Aqua", 10, 3000, null), 5));
        items.add(new PurchasedItem(new Product("A", "Oreo", 20, 2000, null), 10));
        ArrayList<Transaction> transactions = new ArrayList<>();
        for(int i=0; i<10; i++) {
            transactions.add(new Transaction("Toko " + ((char) (i + 'A')), "User X", items, "abc"));
        }

        return transactions;
    }

    @Override
    public void onClickHandler(Transaction transaction) {
        transactionDetailHandler.transactionDetailHandler(transaction);
    }
}
