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
import com.example.asus.onlinecanteen.adapter.FeaturedProductAdapter;
import com.example.asus.onlinecanteen.adapter.ShopAdapter;
import com.example.asus.onlinecanteen.model.Store;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainUserFragment extends Fragment {

    private RecyclerView featuredRecyclerView;
    private RecyclerView shopsRecyclerView;
    private RecyclerView.LayoutManager featuredLayoutManager;
    private RecyclerView.LayoutManager shopLayoutManager;

    public MainUserFragment() {
        // Required empty public constructor
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

        featuredRecyclerView = view.findViewById(R.id.featured_recycler_view);
        shopsRecyclerView = view.findViewById(R.id.shops_recycler_view);

        FeaturedProductAdapter featuredProductAdapter = new FeaturedProductAdapter();
        featuredProductAdapter.setFeaturedProducts(getDummyFeaturedProducts());
        featuredRecyclerView.setAdapter(featuredProductAdapter);

        ShopAdapter shopAdapter = new ShopAdapter();
        shopAdapter.setShops(getDummyShops());
        shopsRecyclerView.setAdapter(shopAdapter);

        featuredLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        featuredRecyclerView.setLayoutManager(featuredLayoutManager);

        shopLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        shopsRecyclerView.setLayoutManager(shopLayoutManager);
    }

    private ArrayList<Store> getDummyShops() {
        ArrayList<Store> shops = new ArrayList<>();
        for(int i=0; i<10; i++) {
            shops.add(new Store("Store #" + (i + 1), "08.00", "16.00", "UPH"));
        }
        return shops;
    }

    private ArrayList<String> getDummyFeaturedProducts() {
        ArrayList<String> featuredProducts = new ArrayList<>();
        for(int i=0; i<10; i++) {
            featuredProducts.add("Product #" + (i + 1));
        }
        return featuredProducts;
    }
}
