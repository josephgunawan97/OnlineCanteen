package com.example.asus.onlinecanteen.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.fragment.DeleteProductFragment;
import com.example.asus.onlinecanteen.fragment.TransactionDetailFragment;
import com.example.asus.onlinecanteen.fragment.TransactionHistoryFragment;
import com.example.asus.onlinecanteen.model.Transaction;

public class DeleteProduct extends AppCompatActivity {

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        DeleteProductFragment deleteFragment = new DeleteProductFragment();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.delete_frame_layout, deleteFragment);
        fragmentTransaction.commit();
    }

}