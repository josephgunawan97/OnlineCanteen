package com.example.asus.onlinecanteen.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.DeleteProductAdapter;
import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.Store;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class EditProductActivity extends AppCompatActivity implements DeleteProductAdapter.DeleteClickHandler{

    private static final String TAG = EditProductActivity.class.getSimpleName();
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 999;

    ImageView imageView;
    Button button, submitbtn;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    String profPicUrl;
    FirebaseAuth mAuth;
    FirebaseUser merchant;
    String choose;
    private ChildEventListener productEventListener;
    Spinner spinner;
    ArrayList<String> productArrayList;
    private DatabaseReference databaseStore ,databaseUsers, databaseProducts;

    //EditText
    EditText productName, productPrice, productQty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product);

        mAuth = FirebaseAuth.getInstance();
        merchant = mAuth.getCurrentUser();

        spinner = findViewById(R.id.spinner);
        imageView = findViewById(R.id.productimageinput);
        button =  findViewById(R.id.productbrowse);
        submitbtn = findViewById(R.id.producteditbtn);
        productName = findViewById(R.id.productnamefill);
        productPrice = findViewById(R.id.productpricefill);
        productQty = findViewById(R.id.productqtyfill);

        //Browse Image in Gallery & set as Profile Picture
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        //Submit data for Sign Up & Upload to Storage
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseProducts = FirebaseDatabase.getInstance().getReference("products");
        databaseStore = FirebaseDatabase.getInstance().getReference("store");
        productArrayList = new ArrayList<>();
        getProduct();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                choose =  parent.getItemAtPosition(position).toString();
                getDataContent();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void getDataContent(){
        //Log.i(TAG,"TEST@@");
        DatabaseReference productDatabase= FirebaseDatabase.getInstance().getReference();

        productDatabase.child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i(TAG,"TEST@3");
                for(DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    //Log.i(TAG,"TEST PRD "+ product.toString());
                    if (merchant.getUid().equals(product.getTokoId()) && product.getName().equals(choose.toString())) {
                        Log.i(TAG,"TEST "+product.getName() +" "+product.getPrice()+" "+product.getStock() );
                        productName.setText(product.getName().toString());
                        productQty.setText(product.getStock().toString());
                        productPrice.setText(product.getPrice().toString());

                        if(product.getImageUrl() != null) {
                            Glide.with(imageView.getContext())
                                    .load(product.getImageUrl())
                                    .into(imageView);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getProduct(){

        if(productEventListener == null) {
            productEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Product product = dataSnapshot.getValue(Product.class);
                        if(merchant.getUid().equals(product.getTokoId()))
                        {
                            productArrayList.add(product.getName().toString());

                        }


                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, productArrayList);
                    spinner.setAdapter(dataAdapter);
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
    //To submit data
    private void submitData() {

        if(!validateRegisterInfo()) {
            return;
        }
        else{

        }
    }

    private boolean validateRegisterInfo() {
        boolean valid = true;

        String productname = productName.getText().toString();
        if(TextUtils.isEmpty(productname)) {
            productName.setError("product name required");
            valid = false;
        } else {
            productName.setError(null);
        }

        String price = productPrice.getText().toString();
        if(TextUtils.isEmpty(price)) {
            productPrice.setError("price required");
            valid = false;
        } else {
            productPrice.setError(null);
        }

        String qty = productQty.getText().toString();
        if(TextUtils.isEmpty(qty)) {
            productQty.setError("quantity required");
            valid = false;
        } else {
            productQty.setError(null);
        }


        return valid;
    }


    private void requestReadStoragePermission() {
        ActivityCompat.requestPermissions(EditProductActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //addAdditionalUserInformation();
            }
        }
    }

    private void backToScreen() {

        // GO TO LOGIN PAGE - after success
        Intent intent = new Intent(EditProductActivity.this, MainActivityMerchant.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClickHandler(Product product) {

    }
}
