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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.model.Withdraw;
import com.example.asus.onlinecanteen.utils.WalletUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Rickhen Hermawan on 02/04/2018.
 */

public class MerchantWithdrawal extends AppCompatActivity {

    private TextInputEditText amount, bankName, transferName, accountNumber;
    private String storeID;
    private DatabaseReference database;
    private Button submit;

    // Firebase Auth and User
    private FirebaseAuth firebaseAuth;
    private FirebaseUser store;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_withdrawal);

        amount = (TextInputEditText) findViewById(R.id.withdraw_amount);
        bankName = (TextInputEditText) findViewById(R.id.bank_name);
        transferName = (TextInputEditText) findViewById(R.id.bank_acc_name);
        submit = (Button) findViewById(R.id.submit_button);
        accountNumber = (TextInputEditText) findViewById(R.id.account_number);

        // Get User
        firebaseAuth = FirebaseAuth.getInstance();
        store = firebaseAuth.getCurrentUser();
        storeID = store.getUid().toString();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRequest();
            }
        });
    }
    //To submit data
    private void doRequest() {

        if (!validateRegisterInfo()) {
            // Field is not filled
            return;
        }
        else
        {
        generateReport();
            Toast.makeText(getApplicationContext(),"Request Success",Toast.LENGTH_SHORT).show();
            backToScreen();
        }

    }
    public void generateReport(){
        database = FirebaseDatabase.getInstance().getReference("withdrawrequest");
        Withdraw withdraw= new Withdraw(transferName.getText().toString(),storeID,bankName.getText().toString(),Integer.parseInt(amount.getText().toString()), accountNumber.getText().toString());
        database.push().setValue(withdraw);
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

        String banknumber = accountNumber.getText().toString();
        if(TextUtils.isEmpty(banknumber)) {
            accountNumber.setError("Account Number required");
            valid = false;
        } else {
            accountNumber.setError(null);
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




        return valid;
    }

    private void backToScreen() {
        // GO TO LOGIN PAGE - after success
        Intent intent = new Intent(this, MainActivityMerchant.class);
        startActivity(intent);
        finish();
    }
}
