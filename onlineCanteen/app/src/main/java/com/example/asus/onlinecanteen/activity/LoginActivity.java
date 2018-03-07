package com.example.asus.onlinecanteen.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    // TAG
    public static final String TAG = LoginActivity.class.getSimpleName();

    // Views
    private EditText loginUsernameEditText;
    private EditText loginPasswordEditText;
    private TextView signUp;
    private Button signIn;
    private String uid;

    private FirebaseUser user;
    private String userRole;

    DatabaseReference userDatabase;

    // Firebase Authentication
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        //Check if user signed-in
        if (user != null) {
            login();
        }
        else {
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }



        // Initialize views
        loginUsernameEditText = findViewById(R.id.loginUsername);
        loginPasswordEditText = findViewById(R.id.loginPass);
        signUp = findViewById(R.id.signUpButton);
        signIn = findViewById(R.id.RegisterStore);

        // Add listener
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationMainActivity.class);
                startActivity(intent);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn.setClickable(false);
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE); //For loading screen purposes
                if(!validateLoginInfo()) {
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    signIn.setClickable(true);
                    Toast.makeText(view.getContext(), "User not Found", Toast.LENGTH_SHORT).show();
                    //Requirements are not fulfilled
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(loginUsernameEditText.getText().toString(), loginPasswordEditText.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        signIn.setClickable(true);
                                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                        firebaseAuth = FirebaseAuth.getInstance();
                                        user = firebaseAuth.getCurrentUser();
                                        login();

                                    } else {
                                        signIn.setClickable(true);
                                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                        Toast.makeText(LoginActivity.this, R.string.authentication_failed, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

            }
        });
    }

    private void login() {
        //Check if User or Store]
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        uid = user.getUid();
        userDatabase = FirebaseDatabase.getInstance().getReference();

        userDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(uid).exists()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        userDatabase.child("store").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(uid).exists()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivityMerchant.class);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private boolean validateLoginInfo() {
        boolean valid = true;

        String username = loginUsernameEditText.getText().toString();
        if(TextUtils.isEmpty(username)) {
            loginUsernameEditText.setError(getString(R.string.requires_email));
            valid = false;
        } else {
            loginUsernameEditText.setError(null);
        }

        String password = loginPasswordEditText.getText().toString();
        if(TextUtils.isEmpty(password)) {
            loginPasswordEditText.setError(getString(R.string.requires_password));
            valid = false;
        } else {
            loginPasswordEditText.setError(null);
        }

        return valid;
    }
}