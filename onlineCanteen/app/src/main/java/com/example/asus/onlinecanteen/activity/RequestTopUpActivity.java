package com.example.asus.onlinecanteen.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.TopUp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class RequestTopUpActivity extends AppCompatActivity {

    private TextInputEditText amount, bankName, transferName;
    private ImageView transferproof;
    private String userID;
    private DatabaseReference database;
    Uri imageUri;
    private Button browse, submit;
    private static final int PICK_IMAGE = 101;

    // Firebase Auth and User
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup_request);

        amount = (TextInputEditText) findViewById(R.id.topup_amount);
        bankName = (TextInputEditText) findViewById(R.id.bank_name);
        transferName = (TextInputEditText) findViewById(R.id.bank_acc_name);
        browse = (Button) findViewById(R.id.picture_button);
        submit = (Button) findViewById(R.id.submit_button);

        transferproof = findViewById(R.id.payment_proof);
        // Get User
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid().toString();

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRequest();
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
            transferproof.setImageURI(imageUri);
        }
    }

    //To submit data
    private void doRequest() {

        if (!validateRegisterInfo()) {
            // Field is not filled
            return;
        }
        else
        {
            uploadImage();
            Toast.makeText(getApplicationContext(),"Request Success",Toast.LENGTH_SHORT).show();
            backToScreen();
        }

    }

    //To upload image
    private void uploadImage() {
        final Map topUp = new HashMap();
        database = FirebaseDatabase.getInstance().getReference("topuprequest");
        final StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("product/"+System.currentTimeMillis()+".jpg");
        if (imageUri!=null){
            profileImageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl =taskSnapshot.getDownloadUrl();

                    String transferproofUrl = downloadUrl.toString();

                    TopUp topUp= new TopUp(transferName.getText().toString(),userID,transferproofUrl.toString(),bankName.getText().toString(),Integer.parseInt(amount.getText().toString()));
                    database.push().setValue(topUp);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Image failed to upload",Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    private boolean validateRegisterInfo() {
        boolean valid = true;

        String productname = amount.getText().toString();
        if(TextUtils.isEmpty(productname)) {
            amount.setError("Amount required");
            valid = false;
        } else {
            amount.setError(null);
        }

        String price = bankName.getText().toString();
        if(TextUtils.isEmpty(price)) {
            bankName.setError("Bank Name required");
            valid = false;
        } else {
            bankName.setError(null);
        }

        String quantity = transferName.getText().toString();
        if(TextUtils.isEmpty(quantity)) {
            transferName.setError("Name required");
            valid = false;
        } else {
            transferName.setError(null);
        }

        if(imageUri==null)
        {
            valid=false;
            Toast.makeText(this, "No Image", Toast.LENGTH_SHORT).show();
        }



        return valid;
    }

    private void backToScreen() {
        // GO TO LOGIN PAGE - after success
        Intent intent = new Intent(this, MainUserActivity.class);
        startActivity(intent);
        finish();
    }
}
