package com.example.asus.onlinecanteen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.OrderAdapter;
import com.example.asus.onlinecanteen.adapter.TopUpAdapter;
import com.example.asus.onlinecanteen.fragment.MerchantOrderListFragment;
import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.TopUp;
import com.example.asus.onlinecanteen.model.Transaction;
import com.example.asus.onlinecanteen.utils.TopUpUtil;
import com.example.asus.onlinecanteen.utils.TransactionUtil;
import com.example.asus.onlinecanteen.utils.WalletUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class AdminConfirmTopUpActivity extends AppCompatActivity {


    FirebaseAuth firebaseAuth;
    DatabaseReference emailDatabase;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<TopUp> topups;
    private TopUpAdapter adapter;
    private ChildEventListener EventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_top_up_request);

        getSupportActionBar().setTitle("Confirm Top Up");
        topups = new ArrayList<TopUp>();
        adapter = new TopUpAdapter(topups);
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
                    TopUp topUp = dataSnapshot.getValue(TopUp.class);
                    Log.i(AdminConfirmTopUpActivity.class.getSimpleName(),"TES LOG "+ topUp.getUid());
                    adapter.addTopUp(topUp);
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

            TopUpUtil.query().addChildEventListener(EventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if(topups != null) {
            topups = null;
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivityAdmin.class);
        startActivity(intent);
        finish();
    }


}
