package com.example.asus.onlinecanteen.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.fragment.TransactionDetailFragment;
import com.example.asus.onlinecanteen.fragment.TransactionCurrentOrderFragment;
import com.example.asus.onlinecanteen.model.Transaction;


public class UserCurrentOrderActivity extends AppCompatActivity implements TransactionCurrentOrderFragment.TransactionDetailHandler {

    private static final String TAG = UserCurrentOrderActivity.class.getSimpleName();

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TransactionCurrentOrderFragment onProgressFragment = new TransactionCurrentOrderFragment();
        onProgressFragment.setTransactionDetailHandler(this);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.current_frame_layout, onProgressFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed() count: " + fragmentManager.getBackStackEntryCount());
        super.onBackPressed();
        if(fragmentManager.getBackStackEntryCount() == 0) {
            finish();
        }
    }

    @Override
    public void transactionDetailHandler(Transaction transaction) {
        TransactionDetailFragment detailFragment = new TransactionDetailFragment();
        detailFragment.setTransaction(transaction);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.current_frame_layout, detailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}