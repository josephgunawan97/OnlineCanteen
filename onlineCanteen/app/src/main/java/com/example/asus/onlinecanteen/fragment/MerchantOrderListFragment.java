package com.example.asus.onlinecanteen.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.asus.onlinecanteen.R;
<<<<<<< HEAD
import com.example.asus.onlinecanteen.adapter.MenuListAdapter;
=======
import com.example.asus.onlinecanteen.activity.MenuListAdapter;
import com.example.asus.onlinecanteen.adapter.OrderAdapter;
>>>>>>> origin/master
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


public class MerchantOrderListFragment extends Fragment{

    // Product Adapter
    private MenuListAdapter menuListAdapter;
    // List view of products
    private ListView productListView;
    private ChildEventListener productEventListener;

    private DatabaseReference databaseUsers;
    private DatabaseReference databaseProducts;
    private DatabaseReference databaseStore;


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


        // Initialize References
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseProducts = FirebaseDatabase.getInstance().getReference("products");
        databaseStore = FirebaseDatabase.getInstance().getReference("store");

        ArrayList<Transaction> transactions = getDummyTransactions();
        OrderAdapter adapter = new OrderAdapter(transactions);
        adapter.setTransactionHistory(transactions);

        recyclerView = view.findViewById(R.id.list);
        layoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private ArrayList<Transaction> getDummyTransactions() {
        ArrayList<PurchasedItem> items = new ArrayList<>();
        items.add(new PurchasedItem(new Product("A", "Aqua", 10, 3000, null), 5));
        items.add(new PurchasedItem(new Product("A", "Oreo", 20, 2000, null), 10));
        ArrayList<Transaction> transactions = new ArrayList<>();
        for(Integer i=0; i<10; i++) {
            transactions.add(new Transaction("Toko " + ((char) (i + 'A')), "User X", items,i.toString()));
        }

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

}
