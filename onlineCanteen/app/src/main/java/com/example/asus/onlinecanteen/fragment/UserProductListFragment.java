package com.example.asus.onlinecanteen.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.activity.MainUserActivity;
import com.example.asus.onlinecanteen.activity.UserOrderProductActivity;
import com.example.asus.onlinecanteen.adapter.UserProductItemAdapter;
import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.Store;
import com.example.asus.onlinecanteen.utils.ProductsUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.Calendar;

public class UserProductListFragment extends Fragment {

    private static final String TAG = UserProductListFragment.class.getSimpleName();
    public static final String PRODUCT_LIST_KEY = "Products";

    // Store
    private Store currentStore;

    // Floating Action Button
    private FloatingActionButton orderButton;

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

        // Order Floating Action Button
        orderButton = view.findViewById(R.id.order_floating_action_button);
        CardView infoView = view.findViewById(R.id.closed_store_information);
        if(isStoreOpen()) {
            infoView.setVisibility(View.GONE);
            orderButton.setEnabled(true);
            orderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), UserOrderProductActivity.class);
                    intent.putExtra(UserOrderProductActivity.CURRENT_STORE_KEY, currentStore);
                    intent.putExtra(UserOrderProductActivity.PRODUCT_LIST_KEY, userProductItemAdapter.getProducts());
                    startActivity(intent);
                }
            });
        } else {
            infoView.setVisibility(View.VISIBLE);
            orderButton.setEnabled(false);
        }

        // Set up recycler view
        productRecyclerView = view.findViewById(R.id.product_recycler_view);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        productRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        productRecyclerView.addItemDecoration(divider);

        userProductItemAdapter = new UserProductItemAdapter();
        productRecyclerView.setAdapter(userProductItemAdapter);
    }

    private boolean isStoreOpen() {
        if(currentStore == null) return false;
        // Get current time
        Calendar rightNow = Calendar.getInstance();
        int nowHour = rightNow.get(Calendar.HOUR_OF_DAY);
        int nowMinute = rightNow.get(Calendar.MINUTE);
        int nowTime = nowHour * 60 + nowMinute;
        Log.d(TAG, "now: " + nowTime);

        // Get store open and close time
        int openHour = Integer.valueOf(currentStore.getOpenHour().substring(0, 2));
        int openMinute = Integer.valueOf(currentStore.getOpenHour().substring(3));
        int openTime = openHour * 60 + openMinute;
        int closeHour = Integer.valueOf(currentStore.getCloseHour().substring(0, 2));
        int closeMinute = Integer.valueOf(currentStore.getCloseHour().substring(3));
        int closeTime = closeHour * 60 + closeMinute;
        Log.d(TAG, "open: " + currentStore.getOpenHour() + " --> " + openTime);
        Log.d(TAG, "close: " + currentStore.getCloseHour() + " --> " + closeTime);

        return openTime <= nowTime && closeTime > nowTime;
    }

    @Override
    public void onResume() {
        super.onResume();
        attachProductDatabaseListener();
    }

    @Override
    public void onPause() {
        super.onPause();
//        detachProductDatabaseListener();
//        if(userProductItemAdapter != null) userProductItemAdapter.removeAllProducts();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "Item clicked");
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fragmentManager = getFragmentManager();
                if(fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
