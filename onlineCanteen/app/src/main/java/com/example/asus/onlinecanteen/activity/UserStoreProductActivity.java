package com.example.asus.onlinecanteen.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.fragment.UserProductListFragment;
import com.example.asus.onlinecanteen.model.Store;

import java.util.Calendar;

public class UserStoreProductActivity extends AppCompatActivity {

    private static final String TAG = UserStoreProductActivity.class.getSimpleName();
    public static final String CURRENT_STORE_KEY = "Current store";

    private Store currentStore;

    private FragmentManager fragmentManager;

    private UserProductListFragment userProductListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_store_product);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toolbar toolbar = findViewById(R.id.user_product_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.user_product_collapsing_toolbar_layout);

        if(bundle != null) {
            if(currentStore == null) {
                currentStore = bundle.getParcelable(CURRENT_STORE_KEY);
            }
        }

        if(currentStore != null) {
            toolbarLayout.setTitle(currentStore.getStoreName());
        }

        if(userProductListFragment == null) {
            userProductListFragment = new UserProductListFragment();
            if(currentStore != null) userProductListFragment.setCurrentStore(currentStore);
        }

        // Order Floating Action Button
        final FloatingActionButton orderButton = findViewById(R.id.order_floating_action_button);
        CardView infoView = findViewById(R.id.closed_store_information);
        if(isStoreOpen()) {
            infoView.setVisibility(View.GONE);
            orderButton.setEnabled(true);
            orderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserStoreProductActivity.this, UserOrderProductActivity.class);
                    intent.putExtra(UserOrderProductActivity.CURRENT_STORE_KEY, currentStore);
                    intent.putExtra(UserOrderProductActivity.PRODUCT_LIST_KEY,
                            userProductListFragment.getUserProductItemAdapter().getProducts());
                    startActivity(intent);
                }
            });
        } else {
            infoView.setVisibility(View.VISIBLE);
            orderButton.setEnabled(false);
            orderButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        }

        ImageView storeFront = findViewById(R.id.user_product_toolbar_image);
        if(currentStore.getImg() != null) {
            Glide.with(this).load(currentStore.getImg())
                    .into(storeFront);
        } else {
            storeFront.setImageResource(R.drawable.logo3);
        }

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.user_store_product_frame_layout, userProductListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "Up button() count: " + fragmentManager.getBackStackEntryCount());
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
}
