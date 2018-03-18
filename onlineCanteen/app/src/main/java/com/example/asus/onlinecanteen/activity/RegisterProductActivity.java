package com.example.asus.onlinecanteen.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterProductActivity extends AppCompatActivity {

    private static final String TAG = RegisterProductActivity.class.getSimpleName();
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 999;

    ImageView imageView;
    Button button, submitbtn;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    String profPicUrl;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference userReferences;
    private DatabaseReference databaseProducts;

    //EditText
    EditText productnameET, priceET, quantityET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        mAuth = FirebaseAuth.getInstance();

        imageView = findViewById(R.id.productimageinput);
        button =  findViewById(R.id.productbrowse);
        submitbtn = findViewById(R.id.productregisterbtn);
        productnameET = findViewById(R.id.productnamefill);
        priceET = findViewById(R.id.productpricefill);
        quantityET = findViewById(R.id.productqtyfill);

        //Browse Image in Gallery & set as Profile Picture
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             openGallery();
            }
        });

        databaseProducts = FirebaseDatabase.getInstance().getReference("products");
        //Submit data for Sign Up & Upload to Storage
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
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

    //To submit data
    private void submitData() {

        if (!validateRegisterInfo()) {
            // Field is not filled
            return;
        }
        else
        {
            user = mAuth.getCurrentUser();
            uploadImage();

            Toast.makeText(getApplicationContext(),"Register Success",Toast.LENGTH_SHORT).show();
            backToScreen();
        }

    }


    //To upload image
    private void uploadImage() {

        Log.d(TAG, "Uploading...");
        final StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("product/"+System.currentTimeMillis()+".jpg");
        if (imageUri!=null){
            profileImageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl =taskSnapshot.getDownloadUrl();
                    profPicUrl = downloadUrl.toString();
                    Product productInfo = new Product(user.getUid(), productnameET.getText().toString(), Integer.parseInt(quantityET.getText().toString()) , Integer.parseInt(priceET.getText().toString()), profPicUrl);
                    databaseProducts.push().setValue(productInfo);
                   // profPicUrl = profileImageRef.toString();
                   // Toast.makeText(getApplicationContext(),profPicUrl,Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Success in uploading");
                   // backToScreen();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Image failed to upload",Toast.LENGTH_LONG).show();
                   // backToScreen();
                }
            });
        }
    }

    private boolean validateRegisterInfo() {
        boolean valid = true;

        String productname = productnameET.getText().toString();
        if(TextUtils.isEmpty(productname)) {
            productnameET.setError("Product Name required");
            valid = false;
        } else {
            productnameET.setError(null);
        }

        String price = priceET.getText().toString();
        if(TextUtils.isEmpty(price)) {
            priceET.setError("Price required");
            valid = false;
        } else {
            priceET.setError(null);
        }

        String quantity = quantityET.getText().toString();
        if(TextUtils.isEmpty(quantity)) {
            quantityET.setError("Quantity required");
            valid = false;
        } else {
            quantityET.setError(null);
        }



        return valid;
    }

    //DRAFT
    //private void addUsers() {
       // User user = new User("Jessica","00000013452", "00000013452", "jessicaseaan@gmail.com", "A", null ,"081511030993" );
       // databaseUsers.push().setValue(user);
    //}

    private void requestReadStoragePermission() {
        ActivityCompat.requestPermissions(RegisterProductActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_READ_EXTERNAL_STORAGE);
    }

    private void backToScreen() {
        // GO TO LOGIN PAGE - after success
        Intent intent = new Intent(this, MainActivityMerchant.class);
        startActivity(intent);
        finish();
    }
}
