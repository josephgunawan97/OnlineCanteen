package com.example.asus.onlinecanteen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.asus.onlinecanteen.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MerchantSettingsActivity extends AppCompatActivity {

    Button logoutButton, editProfileBtn, deliveryFeeBtn, withdrawButton, salesReportBtn;

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
}
