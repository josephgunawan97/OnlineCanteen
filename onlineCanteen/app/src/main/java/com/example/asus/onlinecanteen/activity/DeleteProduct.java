package com.example.asus.onlinecanteen.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.DeleteProductAdapter;
import com.example.asus.onlinecanteen.adapter.ProductListAdapter;
import com.example.asus.onlinecanteen.model.Product;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class DeleteProduct extends AppCompatActivity  {

    // Product Adapter
    private DeleteProductAdapter deleteProductAdapter;
    // List view of products
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ChildEventListener productEventListener;
    private DatabaseReference databaseUsers;
    private DatabaseReference databaseProducts;
    private DatabaseReference databaseStore;
    private Button delete;

    public void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_product);

        ArrayList<Product> productArrayList = new ArrayList<>();
        deleteProductAdapter = new DeleteProductAdapter( this, productArrayList);

        delete = (Button) findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer a=0;
                for (int i=0; i<deleteProductAdapter.getCount(); i++){
                    if(deleteProductAdapter.getChecked())
                        a++;

                }
                Toast.makeText(v.getContext(), a.toString() , Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize References
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseProducts = FirebaseDatabase.getInstance().getReference("products");
        databaseStore = FirebaseDatabase.getInstance().getReference("store");

        // Initialize ListView
        recyclerView = findViewById(R.id.list);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        deleteProductAdapter.clear();
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if(productEventListener == null) {
            productEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Product product = dataSnapshot.getValue(Product.class);
                    deleteProductAdapter.add(product);
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
