package com.example.asus.onlinecanteen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.model.SalesReport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class MerchantSettingsActivity extends AppCompatActivity {

    private Button logoutButton, editProfileBtn, deliveryFeeBtn, withdrawButton, salesReportBtn, storeQRButton;

    private DatabaseReference database;

    // Firebase Auth and User
    private FirebaseAuth firebaseAuth;
    private FirebaseUser merchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_settings);

        //Click listener for logging out
        logoutButton = findViewById(R.id.signOutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        //Click listener for qr
        storeQRButton = findViewById(R.id.storeQRButton);
        storeQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMerchantQR();
            }
        });

        //Click listener for generating sales report
        salesReportBtn = findViewById(R.id.generateButton);
        salesReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateReport();
            }
        });

        //Click listener to MerchantWithdrawal
        withdrawButton = findViewById(R.id.withdrawButton);
        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MerchantSettingsActivity.this, MerchantWithdrawal.class);
                startActivity(intent);
            }
        });

        // Click listener to edit profile
        editProfileBtn = findViewById(R.id.editProfileButton);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MerchantSettingsActivity.this, EditStoreProfileActivity.class);
                startActivity(intent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        merchant = firebaseAuth.getCurrentUser();
    }

    //User Logout
    public void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.signOut_confirmation)
                .setCancelable(false)
                .setPositiveButton(R.string.signOut_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        firebaseAuth = FirebaseAuth.getInstance();
                        merchant = firebaseAuth.getCurrentUser();
                        firebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MerchantSettingsActivity.this, LoginActivity.class);
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

    public void generateReport(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.salesReport_confirmation)
                .setCancelable(false)
                .setPositiveButton(R.string.salesReport_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        database = FirebaseDatabase.getInstance().getReference("salesreportrequest");
                        SalesReport salesReport= new SalesReport(merchant.getUid());
                        database.push().setValue(salesReport);

                        Toast.makeText(MerchantSettingsActivity.this, R.string.salesReport_reply, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.salesReport_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle(R.string.salesReport_title);
        alert.show();
    }

    private void showMerchantQR() {
        Bitmap bitmap;
        // TODO Auto-generated method stub
        AlertDialog.Builder alertadd = new AlertDialog.Builder(
                this);
        alertadd.setTitle("QR Code");

        LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.qr_layout, null);

        String text2Qr = merchant.getUid().toString();
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE,300,300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ImageView image= (ImageView) view.findViewById(R.id.imageView);
            image.setImageBitmap(bitmap);
        }  catch (WriterException e) {
            e.printStackTrace();
        }

        alertadd.setView(view);
        alertadd.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {

            }
        });

        alertadd.show();

    }
}
