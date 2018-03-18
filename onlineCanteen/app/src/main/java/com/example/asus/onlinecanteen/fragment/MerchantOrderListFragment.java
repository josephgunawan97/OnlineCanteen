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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    public MerchantOrderListFragment(){}

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
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseProducts = FirebaseDatabase.getInstance().getReference("products");
        databaseStore = FirebaseDatabase.getInstance().getReference("store");




        firebaseAuth = FirebaseAuth.getInstance();
        merchant = firebaseAuth.getCurrentUser();
        
        recyclerView = view.findViewById(R.id.list);
        layoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(layoutManager);

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
        transactions = new ArrayList<Transaction>();
        adapter = new OrderAdapter(transactions);
        Log.i(MerchantOrderListFragment.class.getSimpleName(),"Add3");
        databaseTransaction = FirebaseDatabase.getInstance().getReference();
        databaseTransaction.child("transactions").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Transaction trans = productSnapshot.getValue(Transaction.class);
                    Log.i(MerchantOrderListFragment.class.getSimpleName(),"IF+ "+merchant.getDisplayName() +" "+ trans.getSid());

                    if(merchant.getDisplayName().equals(trans.getSid())) {
                        //trans.setNameData();
                        transactions.add(trans);
                        Log.i(MerchantOrderListFragment.class.getSimpleName(),"Add2");
                    }
                }
                adapter.setTransactionHistory(transactions);
                if(adapter!=null)
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void detachDatabaseReadListener() {
        if(transactions != null) {
            transactions = null;
        }
        }


    @Override
    public void onRefresh() {
        swipeLayout.setRefreshing(false);
    }
}
