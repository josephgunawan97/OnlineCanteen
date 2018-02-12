package com.example.asus.onlinecanteen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.Store;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Constants
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MENU_LOGOUT = Menu.FIRST;

    // Product Adapter
    private MenuListAdapter menuListAdapter;
    // List view of products
    private ListView productListView;

    // Firebase References
    private DatabaseReference databaseUsers;
    private DatabaseReference databaseProducts;
    private DatabaseReference databaseStore;

    // Firebase Listener
    private ChildEventListener productEventListener;

    // Firebase Auth and User
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    //Initialize Button
    Button placeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Get User
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // Initialize References
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseProducts = FirebaseDatabase.getInstance().getReference("products");
        databaseStore = FirebaseDatabase.getInstance().getReference("store");

        // Product List
        ArrayList<Product> productArrayList = new ArrayList<>();
        menuListAdapter = new MenuListAdapter(this, productArrayList);

        // Initialize ListView
        productListView = findViewById(R.id.list);
        productListView.setAdapter(menuListAdapter);

        //Cart Button and Click Handler
        placeOrder = findViewById(R.id.OrderButton);
        placeOrder.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick (View v){
                    Intent intent = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(intent);
                }
            }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_LOGOUT, 0, R.string.signOut_title);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_LOGOUT:
                logout();
                return true;
        }
        return false;
    }

    //User Logout
    public void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.signOut_confirmation)
                .setCancelable(false)
                .setPositiveButton(R.string.signOut_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        firebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.signOut_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle(R.string.signOut_title);
        alert.show();
    }

    private void addProducts() {
        Product product = new Product("Jessica","Nasi", 30, 12000 );
        databaseProducts.push().setValue(product);
    }

    private void addStore() {
        Store store = new Store("TechnoStall", "8:00","12:00","F Building Floor 2" );
        String id = databaseStore.push().getKey();
        databaseStore.child(id).setValue(store);
    }

    @Override
    protected void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        menuListAdapter.clear();
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if(productEventListener == null) {
            productEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Product product = dataSnapshot.getValue(Product.class);
                    menuListAdapter.add(product);
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

            databaseProducts.addChildEventListener(productEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if(productEventListener != null) {
            databaseProducts.removeEventListener(productEventListener);
            productEventListener = null;
        }
    }
}
