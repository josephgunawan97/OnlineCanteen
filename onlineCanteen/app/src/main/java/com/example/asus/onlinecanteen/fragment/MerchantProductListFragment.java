package com.example.asus.onlinecanteen.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.MenuListAdapter;
import com.example.asus.onlinecanteen.adapter.OrderAdapter;
import com.example.asus.onlinecanteen.adapter.ProductListAdapter;
import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.Transaction;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class MerchantProductListFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener{

    // Product Adapter
    private ProductListAdapter menuListAdapter;
    // List view of products
    private ListView productListView;
    private ChildEventListener productEventListener;

    private DatabaseReference databaseUsers;
    private DatabaseReference databaseProducts;
    private DatabaseReference databaseStore;
    private SwipeRefreshLayout swipeLayout;
    public MerchantProductListFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_activity_merchant, container, false);
        // Inflate the layout for this fragment


        ArrayList<Product> productArrayList = new ArrayList<>();
        menuListAdapter = new ProductListAdapter((Activity) container.getContext(), productArrayList);

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

        // Initialize ListView
       productListView = view.findViewById(R.id.list);
       productListView.setAdapter(menuListAdapter);
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

    public void onRefresh() {

        menuListAdapter.clear();
        detachDatabaseReadListener();
        attachDatabaseReadListener();
        swipeLayout.setRefreshing(false);
    }
}
