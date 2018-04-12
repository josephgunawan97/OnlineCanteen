package com.example.asus.onlinecanteen.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.asus.onlinecanteen.model.Store;
import com.example.asus.onlinecanteen.model.Transaction;
import com.example.asus.onlinecanteen.model.User;
import com.example.asus.onlinecanteen.utils.AccountUtil;
import com.example.asus.onlinecanteen.utils.WalletUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Rickhen Hermawan on 12/04/2018.
 */

public class QrTopupAdmin extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private String result, value;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    ArrayList<Transaction> transactionHistory;
    int pos;
    String idresult;
    private WalletUtil walletUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);


        Intent intent = getIntent();
        if(intent.hasExtra("Location"))
        {
            value = intent.getStringExtra("Location");
            if(value.equals("order"))
            {
                pos = intent.getIntExtra("Position", 0);
                transactionHistory =(ArrayList<Transaction>) intent.getSerializableExtra("Transaction");
            }

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.v("TAG", rawResult.getText()); // Prints scan results
        Log.v("TAG", rawResult.getBarcodeFormat().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setMessage("SCAN SUCCESS");
        result = rawResult.getText();
        //AlertDialog alert1 = builder.create();
        //alert1.show();
        idresult = rawResult.getText();



        DatabaseReference databaseUser = FirebaseDatabase.getInstance().getReference("wallet").child(idresult);
        databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //walletUtil = dataSnapshot.getValue(WalletUtil.class);
                Log.d("QrActivity", "User is " + (idresult));
                Intent intent = new Intent(QrTopupAdmin.this, AdminTopUpActivity.class);
                intent.putExtra("ID",idresult);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}