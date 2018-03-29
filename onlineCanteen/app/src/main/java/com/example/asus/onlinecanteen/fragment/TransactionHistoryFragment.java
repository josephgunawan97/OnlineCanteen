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
import com.example.asus.onlinecanteen.utils.TransactionUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionHistoryFragment extends Fragment implements TransactionHistoryAdapter.TransactionHistoryClickHandler {

    // Recycler View
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    // Adapter of recycler view
    private TransactionHistoryAdapter adapter;
    // Handler of transaction details
    // What will be done to see the details
    private TransactionDetailHandler transactionDetailHandler;
    // Firebase Query
    private Query transactionQuery;
    private ChildEventListener transactionEventListener;

    public interface TransactionDetailHandler {

        void transactionDetailHandler(Transaction transaction);
    }

    public TransactionHistoryFragment() {
        transactionQuery = TransactionUtil.query("uid", FirebaseAuth.getInstance().getUid());
    }

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

        ArrayList<Transaction> transactions = new ArrayList<>();
        adapter = new TransactionHistoryAdapter(this);
        adapter.setTransactionHistory(transactions);

        recyclerView = view.findViewById(R.id.history_recycler_view);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        attachTransactionEventListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        detachTransactionEventListener();
    }

    @Override
    public void onClickHandler(Transaction transaction) {
        transactionDetailHandler.transactionDetailHandler(transaction);
    }

    private void attachTransactionEventListener() {
        if(transactionEventListener == null) {
            transactionEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Transaction newTransaction = dataSnapshot.getValue(Transaction.class);
                    if(newTransaction.getDeliveryStatus()==3)
                    adapter.addTransactionHistory(newTransaction);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };

            transactionQuery.addChildEventListener(transactionEventListener);
        }
    }

    private void detachTransactionEventListener() {
        if(transactionEventListener != null) {
            transactionQuery.removeEventListener(transactionEventListener);
            transactionEventListener = null;
        }
    }
}
