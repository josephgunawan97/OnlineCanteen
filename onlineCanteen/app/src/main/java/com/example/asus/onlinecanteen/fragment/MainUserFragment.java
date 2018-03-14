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
import android.widget.EditText;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.FeaturedProductAdapter;
import com.example.asus.onlinecanteen.adapter.ShopAdapter;
import com.example.asus.onlinecanteen.model.Store;
import com.example.asus.onlinecanteen.utils.ShopUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainUserFragment extends Fragment implements ShopAdapter.ShopItemClickHandler {

    private EditText searchBarEditText;
    private RecyclerView featuredRecyclerView;
    private RecyclerView shopsRecyclerView;
    private RecyclerView.LayoutManager featuredLayoutManager;
    private RecyclerView.LayoutManager shopLayoutManager;

    private ShopAdapter shopAdapter;

    private ShopClickHandler shopClickHandler;

    // Firebase
    private Query shopsQuery;
    private ChildEventListener shopsEventListener;

    public interface ShopClickHandler {
        void shopClickHandler(Store shop);
    }

    public MainUserFragment() {
        // Required empty public constructor
    }

    public void setShopClickHandler(ShopClickHandler shopClickHandler) {
        this.shopClickHandler = shopClickHandler;
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

        searchBarEditText = view.findViewById(R.id.search_bar_search_edit_text);
        featuredRecyclerView = view.findViewById(R.id.featured_recycler_view);
        shopsRecyclerView = view.findViewById(R.id.shops_recycler_view);

        FeaturedProductAdapter featuredProductAdapter = new FeaturedProductAdapter();
        featuredProductAdapter.setFeaturedProducts(getDummyFeaturedProducts());
        featuredRecyclerView.setAdapter(featuredProductAdapter);

        shopAdapter = new ShopAdapter(this);
        shopsRecyclerView.setAdapter(shopAdapter);

        featuredLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        featuredRecyclerView.setLayoutManager(featuredLayoutManager);

        shopLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        shopsRecyclerView.setLayoutManager(shopLayoutManager);

        shopsQuery = ShopUtil.query();
    }

    private ArrayList<String> getDummyFeaturedProducts() {
        ArrayList<String> featuredProducts = new ArrayList<>();
        for(int i=0; i<10; i++) {
            featuredProducts.add("Product #" + (i + 1));
        }
        return featuredProducts;
    }

    @Override
    public void onClickHandler(Store shop) {
        if(shopClickHandler != null) {
            shopClickHandler.shopClickHandler(shop);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.app_name);
        attachProductDatabaseListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        detachProductDatabaseListener();
        if(shopAdapter != null) shopAdapter.removeAllShops();
    }

    //----------------  FIREBASE CHILD EVENT LISTENER -----------------//
    private void attachProductDatabaseListener() {
        if (shopsEventListener == null) {
            shopsEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Store shop = dataSnapshot.getValue(Store.class);
                    shop.setStoreId(dataSnapshot.getKey());
                    shopAdapter.addShop(shop);
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

            shopsQuery.addChildEventListener(shopsEventListener);
        }
    }

    private void detachProductDatabaseListener() {
        if(shopsEventListener != null) {
            shopsQuery.removeEventListener(shopsEventListener);
            shopsEventListener = null;
        }
    }
    //-------------  END OF FIREBASE CHILD EVENT LISTENER --------------//
}
