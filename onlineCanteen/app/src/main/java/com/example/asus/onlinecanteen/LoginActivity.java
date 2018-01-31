package com.example.asus.onlinecanteen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    // TAG
    public static final String TAG = LoginActivity.class.getSimpleName();

    // Views
    private EditText loginUsernameEditText;
    private EditText loginPasswordEditText;
    private TextView signUp;
    private Button signIn;

    // Firebase Authentication
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize views
        loginUsernameEditText = findViewById(R.id.loginUsername);
        loginPasswordEditText = findViewById(R.id.loginPass);
        signUp = findViewById(R.id.signUpButton);
        signIn = findViewById(R.id.signInButton);

        // Add listener
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateLoginInfo()) {
                    // Requirements are not fulfilled
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(loginUsernameEditText.getText().toString(), loginPasswordEditText.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    private boolean validateLoginInfo() {
        boolean valid = true;

        String username = loginUsernameEditText.getText().toString();
        if(TextUtils.isEmpty(username)) {
            loginUsernameEditText.setError("Username required");
            valid = false;
        } else {
            loginUsernameEditText.setError(null);
        }

        String password = loginPasswordEditText.getText().toString();
        if(TextUtils.isEmpty(password)) {
            loginPasswordEditText.setError("Password required");
            valid = false;
        } else {
            loginPasswordEditText.setError(null);
        }

        return valid;
    }
}
