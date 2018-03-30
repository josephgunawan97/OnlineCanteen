package com.example.asus.onlinecanteen.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.activity.LoginActivity;
import com.example.asus.onlinecanteen.activity.MerchantOrderDetailActivity;
import com.example.asus.onlinecanteen.adapter.MenuListAdapter;
import com.example.asus.onlinecanteen.adapter.OrderAdapter;
import com.example.asus.onlinecanteen.adapter.TransactionHistoryAdapter;
import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.PurchasedItem;
import com.example.asus.onlinecanteen.model.Transaction;
import com.example.asus.onlinecanteen.utils.TransactionUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.zip.Inflater;


public class MerchantOrderListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // Product Adapter
    private OrderAdapter adapter ;
    // List view of products


    private FirebaseAuth firebaseAuth;
    private FirebaseUser merchant;
    
    private DatabaseReference databaseUsers;
    private DatabaseReference databaseProducts;
    private DatabaseReference databaseStore;
    private DatabaseReference databaseTransaction;
    SwipeRefreshLayout swipeLayout;
    ArrayList<Transaction> transactions;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ChildEventListener eventListener;
    public MerchantOrderListFragment(){
    }
    
   
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_activity_merchant_order, container, false);
        // Inflate the layout for this fragment


        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimaryLight);

        // Initialize References
        databaseTransaction = FirebaseDatabase.getInstance().getReference("transactions");
        databaseProducts = FirebaseDatabase.getInstance().getReference("products");
        databaseStore = FirebaseDatabase.getInstance().getReference("store");


        transactions = new ArrayList<Transaction>();
        adapter = new OrderAdapter(transactions);

        firebaseAuth = FirebaseAuth.getInstance();
        merchant = firebaseAuth.getCurrentUser();
        
        recyclerView = view.findViewById(R.id.list);
        layoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {

        if(eventListener == null) {
            eventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Transaction newTransaction = dataSnapshot.getValue(Transaction.class);
                    if(merchant.getUid().equals(newTransaction.getSid()))
                        adapter.add(newTransaction);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Transaction newTransaction = dataSnapshot.getValue(Transaction.class);
                    if(merchant.getUid().equals(newTransaction.getSid()))
                    adapter.add(newTransaction);}

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };

            databaseTransaction.addChildEventListener(eventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if(eventListener != null) {
            adapter.notifyDataSetChanged();
            transactions = new ArrayList<Transaction>();
            adapter = new OrderAdapter(transactions);
            databaseProducts.removeEventListener(eventListener);
            eventListener = null;
        }
        }


    @Override
    public void onRefresh() {
        recyclerView.removeAllViews();
        detachDatabaseReadListener();
        attachDatabaseReadListener();
        swipeLayout.setRefreshing(false);
    }
}
