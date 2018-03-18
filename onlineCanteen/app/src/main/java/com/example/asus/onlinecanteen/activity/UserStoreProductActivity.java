package com.example.asus.onlinecanteen.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.fragment.UserProductListFragment;
import com.example.asus.onlinecanteen.model.Store;

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            if(currentStore == null) {
                currentStore = bundle.getParcelable(CURRENT_STORE_KEY);
            }
        }

        Log.d(TAG, "userProductListFragment: " + userProductListFragment);
        Log.d(TAG, "currentStore: " + currentStore);

        if(userProductListFragment == null) {
            userProductListFragment = new UserProductListFragment();
            if(currentStore != null) userProductListFragment.setCurrentStore(currentStore);
        }

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.user_store_product_frame_layout, userProductListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
