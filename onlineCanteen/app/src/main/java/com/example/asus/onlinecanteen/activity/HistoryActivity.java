package com.example.asus.onlinecanteen.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.fragment.TransactionDetailFragment;
import com.example.asus.onlinecanteen.fragment.TransactionHistoryFragment;
import com.example.asus.onlinecanteen.model.Transaction;

/**
 * Created by Steven Albert on 10/02/2018.
 */

public class HistoryActivity extends AppCompatActivity implements TransactionHistoryFragment.TransactionDetailHandler {

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        TransactionHistoryFragment historyFragment = new TransactionHistoryFragment();
        historyFragment.setTransactionDetailHandler(this);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.history_frame_layout, historyFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void transactionDetailHandler(Transaction transaction) {
        TransactionDetailFragment detailFragment = new TransactionDetailFragment();
        detailFragment.setTransaction(transaction);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.history_frame_layout, detailFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
