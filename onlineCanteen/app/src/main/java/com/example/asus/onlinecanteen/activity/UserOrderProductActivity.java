package com.example.asus.onlinecanteen.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.fragment.UserOrderProductFragment;
import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.Store;

import java.util.ArrayList;

public class UserOrderProductActivity extends AppCompatActivity {

    private static final String TAG = UserOrderProductActivity.class.getSimpleName();

    public static final String CURRENT_STORE_KEY = "Current store";
    public static final String PRODUCT_LIST_KEY = "Product list";

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
}
