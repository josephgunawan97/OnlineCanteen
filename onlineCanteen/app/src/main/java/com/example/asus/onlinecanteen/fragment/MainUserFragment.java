package com.example.asus.onlinecanteen.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.activity.QrActivity;
import com.example.asus.onlinecanteen.adapter.FeaturedProductAdapter;
import com.example.asus.onlinecanteen.adapter.UserStoreAdapter;
import com.example.asus.onlinecanteen.model.Store;
import com.example.asus.onlinecanteen.utils.StoreUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainUserFragment extends Fragment implements UserStoreAdapter.StoreItemClickHandler {

    private static final String TAG = MainUserFragment.class.getSimpleName();

    private EditText searchBarEditText;
    private RecyclerView featuredRecyclerView;
    private RecyclerView storesRecyclerView;
    private RecyclerView.LayoutManager featuredLayoutManager;
    private RecyclerView.LayoutManager storeLayoutManager;
    private ImageButton searchBarScanQrButton;

    private UserStoreAdapter userStoreAdapter;

    private StoreClickHandler storeClickHandler;

    // Firebase
    private Query storesQuery;
    private ChildEventListener storesEventListener;

    public interface StoreClickHandler {
        void storeClickHandler(Store store);
    }

    public MainUserFragment() {
        // Required empty public constructor
    }

    public void setStoreClickHandler(StoreClickHandler storeClickHandler) {
        this.storeClickHandler = storeClickHandler;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated of " + TAG);

        searchBarEditText = view.findViewById(R.id.search_bar_search_edit_text);
        featuredRecyclerView = view.findViewById(R.id.featured_recycler_view);
        storesRecyclerView = view.findViewById(R.id.stores_recycler_view);
        searchBarScanQrButton = view.findViewById(R.id.search_bar_scan_qr_button);

        searchBarScanQrButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainUserFragment.this.getActivity(),QrActivity.class);
                startActivity(i);
            }
        });


        FeaturedProductAdapter featuredProductAdapter = new FeaturedProductAdapter();
        featuredProductAdapter.setFeaturedProducts(getDummyFeaturedProducts());
        featuredRecyclerView.setAdapter(featuredProductAdapter);

        if(userStoreAdapter == null) {
            userStoreAdapter = new UserStoreAdapter(this);
        }
        storesRecyclerView.setAdapter(userStoreAdapter);

        featuredLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        featuredRecyclerView.setLayoutManager(featuredLayoutManager);

        storeLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        storesRecyclerView.setLayoutManager(storeLayoutManager);

        storesQuery = StoreUtil.query();
    }

    private ArrayList<String> getDummyFeaturedProducts() {
        ArrayList<String> featuredProducts = new ArrayList<>();
        for(int i=0; i<10; i++) {
            featuredProducts.add("Product #" + (i + 1));
        }
        return featuredProducts;
    }

    @Override
    public void onClickHandler(Store store) {
        if(storeClickHandler != null) {
            storeClickHandler.storeClickHandler(store);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume of " + TAG);
        getActivity().setTitle(R.string.app_name);
        attachStoreDatabaseListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause of " + TAG);
//        detachStoreDatabaseListener();
//        if(userStoreAdapter != null) userStoreAdapter.removeAllStores();
    }

    //----------------  FIREBASE CHILD EVENT LISTENER -----------------//
    private void attachStoreDatabaseListener() {
        if (storesEventListener == null) {
            storesEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Store store = dataSnapshot.getValue(Store.class);
                    store.setStoreId(dataSnapshot.getKey());
                    userStoreAdapter.addStore(store);
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

            storesQuery.addChildEventListener(storesEventListener);
        }
    }

    private void detachStoreDatabaseListener() {
        if(storesEventListener != null) {
            storesQuery.removeEventListener(storesEventListener);
            storesEventListener = null;
        }
    }
    //-------------  END OF FIREBASE CHILD EVENT LISTENER --------------//
}
