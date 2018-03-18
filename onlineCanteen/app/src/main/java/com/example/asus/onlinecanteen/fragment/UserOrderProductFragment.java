package com.example.asus.onlinecanteen.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.activity.CartActivity;
import com.example.asus.onlinecanteen.activity.MainActivity;
import com.example.asus.onlinecanteen.adapter.UserOrderProductAdapter;
import com.example.asus.onlinecanteen.model.Cart;
import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.Store;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserOrderProductFragment extends Fragment {

    private static final String TAG = UserOrderProductFragment.class.getSimpleName();

    // Current Store
    private Store currentStore;

    // Button
    private Button placeOrderButton;

    // Recycler View
    private RecyclerView orderRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private UserOrderProductAdapter orderProductAdapter;

    public UserOrderProductFragment() {
        // Required empty public constructor
    }

    public void setCurrentStore(Store currentStore) {
        this.currentStore = currentStore;
    }

    public void setProductList(ArrayList<Product> products) {
        orderProductAdapter = new UserOrderProductAdapter(products);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_order_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderRecyclerView = view.findViewById(R.id.user_order_product_recycler_view);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        orderRecyclerView.setLayoutManager(layoutManager);

        orderRecyclerView.setAdapter(orderProductAdapter);

        //Cart Button and Click Handler
        placeOrderButton = view.findViewById(R.id.user_order_product_button);
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Cart> carts = orderProductAdapter.getOrders();

                //Check empty cart
                boolean emptyCart = true;
                for (Cart c : carts) {
                    if (c.getQuantity() != 0) {
                        emptyCart = false;
                        break;
                    }
                }

                if (emptyCart == true) {
                    //Alert dialog if there are no items in cart
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("You don't have any items in your cart!")
                    .setCancelable(false)
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    }
                    });

                    AlertDialog alert = builder.create();
                    alert.setTitle("Error");
                    alert.show();
                } else {
                    //Remove item if qty is 0
                    ArrayList<Cart> toRemove = new ArrayList<>();

                    for (Cart c : carts) {
                        if (c.getQuantity() == 0) {
                            toRemove.add(c);
                        }
                    }

                    //Go to cart
                    Intent intent = new Intent(getActivity(), CartActivity.class);
                    ArrayList<Cart> intentCart = new ArrayList<>();
                    intentCart.addAll(carts);
                    intentCart.removeAll(toRemove);
                    intent.putExtra("Cart", intentCart);
                    intent.putExtra("Seller", currentStore.getStoreName());
                    startActivity(intent);
                }
            }
        });
    }
}
