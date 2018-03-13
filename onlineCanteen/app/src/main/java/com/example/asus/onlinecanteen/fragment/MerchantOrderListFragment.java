package com.example.asus.onlinecanteen.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.activity.MerchantOrderDetailActivity;
import com.example.asus.onlinecanteen.adapter.MenuListAdapter;
import com.example.asus.onlinecanteen.adapter.OrderAdapter;
import com.example.asus.onlinecanteen.adapter.TransactionHistoryAdapter;
import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.PurchasedItem;
import com.example.asus.onlinecanteen.model.Transaction;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.zip.Inflater;


public class MerchantOrderListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // Product Adapter
    private MenuListAdapter menuListAdapter;
    // List view of products
    private ChildEventListener productEventListener;

    private DatabaseReference databaseUsers;
    private DatabaseReference databaseProducts;
    private DatabaseReference databaseStore;
    SwipeRefreshLayout swipeLayout;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public MerchantOrderListFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_activity_merchant_order, container, false);
        // Inflate the layout for this fragment

        ArrayList<Product> productArrayList = new ArrayList<>();
        menuListAdapter = new MenuListAdapter((Activity) container.getContext(), productArrayList);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimaryLight);

        // Initialize References
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseProducts = FirebaseDatabase.getInstance().getReference("products");
        databaseStore = FirebaseDatabase.getInstance().getReference("store");

        ArrayList<Transaction> transactions = getTransactions();
        OrderAdapter adapter = new OrderAdapter(transactions);
        adapter.setTransactionHistory(transactions);

        recyclerView = view.findViewById(R.id.list);
        layoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private ArrayList<Transaction> getTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        return transactions;
    }

    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        menuListAdapter.clear();
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if(productEventListener == null) {
            productEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Product product = dataSnapshot.getValue(Product.class);
                    menuListAdapter.add(product);
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

            databaseProducts.addChildEventListener(productEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if(productEventListener != null) {
            databaseProducts.removeEventListener(productEventListener);
            productEventListener = null;
        }
    }

    @Override
    public void onRefresh() {
        ArrayList<Transaction> transactions = getTransactions();
        OrderAdapter adapter = new OrderAdapter(transactions);
        adapter.setTransactionHistory(transactions);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        swipeLayout.setRefreshing(false);
    }
}
