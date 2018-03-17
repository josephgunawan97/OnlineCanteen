package com.example.asus.onlinecanteen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.utils.WalletUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminTopUpActivity extends AppCompatActivity {

    EditText emailET;
    EditText passwordET;
    EditText amountET;
    Button topUpButton;

    String email, password;
    int amount;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_top_up);

        firebaseAuth = FirebaseAuth.getInstance();

        emailET = (EditText) findViewById(R.id.topUpEmail);
        passwordET = (EditText) findViewById(R.id.topUpPassword);
        amountET = (EditText) findViewById(R.id.topUpAmount);
        topUpButton = (Button) findViewById(R.id.topUpButton);


        topUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailET.getText().toString();
                password = passwordET.getText().toString();
                amount = Integer.parseInt(amountET.getText().toString());

                WalletUtil walletUtil = new WalletUtil();

                Intent intent;
                walletUtil.debitAmount("DUG8BCUpmpbjYj769HQ9mGMs1J13",amount);



            }
        });
    }
}
