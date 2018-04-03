package com.example.asus.onlinecanteen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.WithdrawAdapter;
import com.example.asus.onlinecanteen.model.Withdraw;
import com.example.asus.onlinecanteen.utils.WithdrawUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by Rickhen Hermawan on 03/04/2018.
 */

public class AdminConfirmWithdrawActivity extends AppCompatActivity {


    FirebaseAuth firebaseAuth;
    DatabaseReference emailDatabase;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Withdraw> withdraws;
    private WithdrawAdapter adapter;
    private ChildEventListener EventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_withdraw_request);

        getSupportActionBar().setTitle("Confirm Withdraw");
        withdraws = new ArrayList<Withdraw>();
        adapter = new WithdrawAdapter(withdraws);
        recyclerView = (RecyclerView) findViewById(R.id.request_recycler_view);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
    }
    private void attachDatabaseReadListener() {
        if (EventListener == null) {
            EventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Withdraw withdraw = dataSnapshot.getValue(Withdraw.class);
                    Log.i(AdminConfirmWithdrawActivity.class.getSimpleName(),"TES LOG "+ withdraw.getUid());
                    adapter.addWithdraw(withdraw);
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

            WithdrawUtil.query().addChildEventListener(EventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if(withdraws != null) {
            withdraws = null;
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivityAdmin.class);
        startActivity(intent);
        finish();
    }


}
