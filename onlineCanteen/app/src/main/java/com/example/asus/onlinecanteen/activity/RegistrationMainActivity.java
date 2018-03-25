package com.example.asus.onlinecanteen.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.asus.onlinecanteen.R;

public class RegistrationMainActivity extends AppCompatActivity {

    private static final int REGISTRATION_CODE = 100;

        private Button storeSignUp, userSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_main);

        storeSignUp = findViewById(R.id.RegisterStoreBtn);
        userSignUp = findViewById(R.id.RegisterUserBtn);

        storeSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationMainActivity.this, RegisterStoreActivity.class);
                startActivity(intent);
            }
        });

        userSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationMainActivity.this, RegisterActivity.class);
                startActivityForResult(intent, REGISTRATION_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REGISTRATION_CODE) {
            if(resultCode == RegisterActivity.REGISTER_SUCCESSFUL) {

            }
        }
    }
}
