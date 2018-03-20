package com.example.asus.onlinecanteen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.fragment.UserOrderProductFragment;
import com.example.asus.onlinecanteen.model.Cart;
import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.Store;

import java.util.ArrayList;

public class UserOrderProductActivity extends AppCompatActivity implements UserOrderProductFragment.PlaceOrderHandler {

    private static final String TAG = UserOrderProductActivity.class.getSimpleName();

    public static final String CURRENT_STORE_KEY = "Current store";
    public static final String PRODUCT_LIST_KEY = "Product list";

    public static final int PLACE_ORDER_CODE = 100;

    private Store currentStore;

    private ArrayList<Product> products;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_product);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            if(currentStore == null)
                currentStore = bundle.getParcelable(CURRENT_STORE_KEY);
            if(products == null)
                products = bundle.getParcelableArrayList(PRODUCT_LIST_KEY);
        }

        UserOrderProductFragment orderProductFragment = new UserOrderProductFragment();
        orderProductFragment.setCurrentStore(currentStore);
        orderProductFragment.setProductList(products);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.user_order_product_frame_layout, orderProductFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "UP BUTTON PRESSED");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(fragmentManager.getBackStackEntryCount() == 1) {
            finish();
        }
        else super.onBackPressed();
    }

    @Override
    public void onClickPlaceOrder(ArrayList<Cart> carts) {
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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            Intent intent = new Intent(this, CartActivity.class);
            ArrayList<Cart> intentCart = new ArrayList<>();
            intentCart.addAll(carts);
            intentCart.removeAll(toRemove);
            intent.putExtra("Cart", intentCart);
            intent.putExtra("Seller", currentStore.getStoreName());
            intent.putExtra("SellerEmail",currentStore.getEmail());
            startActivityForResult(intent, PLACE_ORDER_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PLACE_ORDER_CODE) {
            if(resultCode == RESULT_OK) {
                finish();
            }
        }
    }
}
