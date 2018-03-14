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

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.UserProductItemAdapter;
import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.Store;
import com.example.asus.onlinecanteen.utils.ProductsUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

public class UserProductListFragment extends Fragment {

    // Store
    private Store currentStore;

    // Recycler View
    private RecyclerView productRecyclerView;
    private RecyclerView.LayoutManager layoutManager;

    // Adapter
    private UserProductItemAdapter userProductItemAdapter;

    // Firebase
    private Query productsQuery;
    private ChildEventListener productsEventListener;

    public UserProductListFragment() {
        // Required empty public constructor
    }

    public void setCurrentStore(Store currentStore) {
        this.currentStore = currentStore;
        if(productsQuery != null) {
            if(userProductItemAdapter != null) {
                userProductItemAdapter.removeAllProducts();
                detachProductDatabaseListener();
            }
        }
        productsQuery = ProductsUtil.query(currentStore);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(currentStore != null) getActivity().setTitle(currentStore.getStoreName());

        // Set up recycler view
        productRecyclerView = view.findViewById(R.id.product_recycler_view);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        productRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        productRecyclerView.addItemDecoration(divider);

        userProductItemAdapter = new UserProductItemAdapter();
        productRecyclerView.setAdapter(userProductItemAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        attachProductDatabaseListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        detachProductDatabaseListener();
        if(userProductItemAdapter != null) userProductItemAdapter.removeAllProducts();
    }


    //----------------  FIREBASE CHILD EVENT LISTENER -----------------//
    private void attachProductDatabaseListener() {
        if (productsEventListener == null) {
            productsEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Product product = dataSnapshot.getValue(Product.class);
                    userProductItemAdapter.addProduct(product);
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

            productsQuery.addChildEventListener(productsEventListener);
        }
    }

    private void detachProductDatabaseListener() {
        if(productsEventListener != null) {
            productsQuery.removeEventListener(productsEventListener);
            productsEventListener = null;
        }
    }
    //-------------  END OF FIREBASE CHILD EVENT LISTENER --------------//
}
