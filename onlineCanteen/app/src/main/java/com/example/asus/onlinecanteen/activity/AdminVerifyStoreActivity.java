package com.example.asus.onlinecanteen.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.AdminVerifyStoreAdapter;
import com.example.asus.onlinecanteen.model.Store;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminVerifyStoreActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    DatabaseReference emailDatabase;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Store> stores;
    private AdminVerifyStoreAdapter adapter;
    private ChildEventListener EventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_verify_store);

        getSupportActionBar().setTitle("Verify Store");
        stores = new ArrayList<Store>();
        adapter = new AdminVerifyStoreAdapter(stores);
        recyclerView = (RecyclerView) findViewById(R.id.adminverifystore_recycler_view);
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
                    String storeString = dataSnapshot.getValue().toString();
                    Log.d("TEST", storeString);
                    if(storeString.equals("UNVERIFIED_STORE")) adapter.addTopUp(dataSnapshot.getKey());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    String storeString = dataSnapshot.getValue().toString();
                    Log.d("TEST", storeString);
                    if(storeString.equals("UNVERIFIED_STORE")) adapter.addTopUp(dataSnapshot.getKey());
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };

            FirebaseDatabase.getInstance().getReference("role").addChildEventListener(EventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if(stores != null) {
            stores = null;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
