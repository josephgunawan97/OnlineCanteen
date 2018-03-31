package com.example.asus.onlinecanteen.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.onlinecanteen.R;
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
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {

    // TAG
    public static final String TAG = LoginActivity.class.getSimpleName();

    // Views
    private ViewGroup loadingPanel;
    private EditText loginUsernameEditText;
    private EditText loginPasswordEditText;
    private TextView signUp;
    private Button signIn;
    private String uid;

    private FirebaseUser user;
    private String userRole;

    DatabaseReference userDatabase, mUser, mStore;

    // Firebase Authentication
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadingPanel = findViewById(R.id.loadingPanel);

        getSupportActionBar().hide();

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
//                signIn.setClickable(false);
                loadingPanel.setVisibility(View.VISIBLE); //For loading screen purposes
                loadingPanel.setClickable(true);
                if(!validateLoginInfo()) {
//                    Toast.makeText(view.getContext(), "User not Found", Toast.LENGTH_SHORT).show();
                    //Requirements are not fulfilled
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(loginUsernameEditText.getText().toString(),
                                                        loginPasswordEditText.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        login();
                                    } else {
//                                        signIn.setClickable(true);
                                        Toast.makeText(LoginActivity.this, R.string.authentication_failed, Toast.LENGTH_SHORT).show();
                                    }
                                    loadingPanel.setVisibility(View.GONE);
                                }
                            });

            }
        });
        mUser = FirebaseDatabase.getInstance().getReference().child("users");
        mStore = FirebaseDatabase.getInstance().getReference().child("store");

        loginPasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard(v);
                } else {
                    openKeyboard(v);
                }
            }
        });

        loginUsernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    hideKeyboard(v);
                } else {
                    openKeyboard(v);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        //Check if user signed-in
        if (user != null) {
            login();
        }
        else {
            loadingPanel.setVisibility(View.GONE);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void openKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    private void login() {
        //Check if User or Store]
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        uid = user.getUid();
        userDatabase = FirebaseDatabase.getInstance().getReference();

        userDatabase.child("role").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String role = snapshot.getValue().toString();
                    Intent intent;

                    String currentUser = firebaseAuth.getCurrentUser().getUid();
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
                    switch (role){
                        case "USER":
                            mUser.child(currentUser).child("device_token").setValue(deviceToken);
                            intent = new Intent(LoginActivity.this, MainUserActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case "STORE":
                            mStore.child(currentUser).child("device_token").setValue(deviceToken);
                            intent = new Intent(LoginActivity.this, MainActivityMerchant.class);
                            startActivity(intent);
                            finish();
                            break;
                        case "UNVERIFIED_STORE":
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    LoginActivity.this).create();
                            alertDialog.setTitle("Store not verified!");
                            alertDialog.setMessage("Please wait for our admin to verify your store.");
                            alertDialog.setIcon(R.drawable.logo);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    }
                            });
                            alertDialog.show();
                            FirebaseAuth.getInstance().signOut();
                            loadingPanel.setVisibility(View.INVISIBLE);
                            break;
                        case "ADMIN":
                            intent = new Intent(LoginActivity.this, MainActivityAdmin.class);
                            startActivity(intent);
                            finish();
                            break;
                        default: break;

                    }
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