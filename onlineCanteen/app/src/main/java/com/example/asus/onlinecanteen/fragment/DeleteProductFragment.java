package com.example.asus.onlinecanteen.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.activity.MainActivityMerchant;
import com.example.asus.onlinecanteen.adapter.DeleteProductAdapter;
import com.example.asus.onlinecanteen.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DeleteProductFragment extends Fragment implements DeleteProductAdapter.DeleteClickHandler{

    // Product Adapter
    private DeleteProductAdapter deleteAdapter;
    // List view of products
    private RecyclerView productListView;

    private RecyclerView.LayoutManager layoutManager;
    private ChildEventListener productEventListener;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser merchant;
    private DatabaseReference databaseUsers;
    private DatabaseReference databaseProducts;
    private DatabaseReference databaseStore;
    private Button delete;
    ArrayList<Product> productArrayList;
    public DeleteProductFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delete_product, container, false);
        // Inflate the layout for this fragment

        delete = (Button) view.findViewById(R.id.delete);

        firebaseAuth = FirebaseAuth.getInstance();
        merchant = firebaseAuth.getCurrentUser();

        // Initialize References
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseProducts = FirebaseDatabase.getInstance().getReference("products");
        databaseStore = FirebaseDatabase.getInstance().getReference("store");

        productArrayList = new ArrayList<>();
        deleteAdapter = new DeleteProductAdapter(this);
        deleteAdapter.setProductList(productArrayList);

        // Initialize ListView
       productListView = view.findViewById(R.id.list2);
       layoutManager = new LinearLayoutManager(view.getContext());
       productListView.setLayoutManager(layoutManager);
       productListView.setAdapter(deleteAdapter);

       delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


               deleteAdapter.notifyDataSetChanged();
               new AlertDialog.Builder(getContext())
                       .setTitle("Delete Product")
                       .setMessage("Are you sure to Delete Product?")
                       .setPositiveButton("OK", null)
                       .setNegativeButton("Cancel", null)
                       .setPositiveButton("OK", new DialogInterface.OnClickListener()
                       {
                           @Override
                           public void onClick(DialogInterface dialog, int which)
                           {
                               deleteProduct();
                               Toast.makeText(getContext(), "Delete Succeed", Toast.LENGTH_SHORT).show();
                               Intent intent = new Intent(getActivity(), MainActivityMerchant.class);
                               startActivity(intent);
                           }
                       })
                       .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                       {
                           @Override
                           public void onClick(DialogInterface dialog, int which)
                           {
                               Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                           }
                       }).show();

           }
       });
        return view;
    }
    public void deleteProduct(){
        Query data;
        for (int i =0 ; i< deleteAdapter.getItemCount(); i++){
           data = databaseProducts.orderByChild("name").equalTo(productArrayList.get(i).getName());
           final Product product2 = productArrayList.get(i);
           data.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   for (DataSnapshot productSnapshot: dataSnapshot.getChildren()) {
                       if(merchant.getUid().equals(product2.getTokoId()) && product2.isChecked())
                           productSnapshot.getRef().removeValue();
                   }
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });
        }
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
        if(productEventListener == null) {
            productEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if(merchant.getUid().equals(product.getTokoId()))
                    deleteAdapter.addProductList(product);
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

    @Override
    public void onClickHandler(Product product) {

    }

}
