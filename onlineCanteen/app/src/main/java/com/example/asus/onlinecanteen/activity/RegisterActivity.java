package com.example.asus.onlinecanteen.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.constant.RegistrationConstant;
import com.example.asus.onlinecanteen.fragment.RegistrationCancellationDialogFragment;
import com.example.asus.onlinecanteen.utils.AccountUtil;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegisterActivity extends AppCompatActivity
        implements RegistrationCancellationDialogFragment.CancellationHandler {

    // TAG
    private static final String TAG = RegisterActivity.class.getSimpleName();

    // Request permission code
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 100;

    // Choose image code
    private static final int CHOOSE_PICTURE_FROM_GALLERY_CODE = 101;

    // Views
    private ViewGroup progressBarLayout;
    // Core views
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    // Personal information views
    private ImageView profilePictureImageView;
    private TextInputEditText nameEditText;
    private TextInputEditText phoneNumberEditText;
    private Button changePictureButton;
    private Button signUpButton;

    // Profile Picture Uri
    private Uri profilePictureUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("User Registration");

        // Initialize views
        progressBarLayout = findViewById(R.id.progress_bar_layout);
        emailEditText = findViewById(R.id.registration_email_edit_text);
        passwordEditText = findViewById(R.id.registration_password_edit_text);
        profilePictureImageView = findViewById(R.id.user_registration_detail_picture);
        nameEditText = findViewById(R.id.user_registration_detail_name);
        phoneNumberEditText = findViewById(R.id.user_registration_detail_phone_number);
        changePictureButton = findViewById(R.id.user_registration_detail_picture_button);
        signUpButton = findViewById(R.id.registration_sign_up_button);

        progressBarLayout.setVisibility(View.GONE);

        // Add listener
        changePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePictureFromGallery();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm()) {
                    progressBarLayout.setVisibility(View.VISIBLE);

                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    String name = nameEditText.getText().toString();
                    String phoneNumber = phoneNumberEditText.getText().toString();

                    registerNewUser(email, password, name, phoneNumber, profilePictureUri);
                }
            }
        });
    }

    private void requestReadExternalStoragePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_READ_EXTERNAL_STORAGE);
    }

    private boolean checkReadExternalStoragePermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    private void choosePictureFromGallery(){
        if(checkReadExternalStoragePermission()) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, CHOOSE_PICTURE_FROM_GALLERY_CODE);
        }
        else {
            requestReadExternalStoragePermission();
        }
    }

    private void changeProfilePicture() {
        if(profilePictureUri == null) {
            profilePictureImageView.setImageResource(R.drawable.logo3);
        }
        else {
            profilePictureImageView.setImageURI(profilePictureUri);
        }
    }

    public boolean validateForm() {
        String email = new String(emailEditText.getText().toString());
        String password = new String(passwordEditText.getText().toString());
        String name = new String(nameEditText.getText().toString());
        String phoneNumber = new String(phoneNumberEditText.getText().toString());

        boolean valid = true;

        if(TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            valid = false;
        }

        if(TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            valid = false;
        }

        if(TextUtils.isEmpty(name)) {
            nameEditText.setError("Name is required");
            valid = false;
        }

        if(TextUtils.isEmpty(phoneNumber)) {
            phoneNumberEditText.setError("Phone number is required");
            valid = false;
        }

        return valid;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Do something
                choosePictureFromGallery();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == CHOOSE_PICTURE_FROM_GALLERY_CODE) {
                profilePictureUri = data.getData();
                changeProfilePicture();
            }
        }
    }

    private void showCancellationConfirmation() {
        DialogFragment dialogFragment = new RegistrationCancellationDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), null);
    }

    private void finishRegistration() {
        progressBarLayout.setVisibility(View.GONE);
        setResult(RegistrationConstant.REGISTER_SUCCESSFUL);
        finish();
    }

    private void registerNewUser(final String email, final String password, final String name, final String phoneNumber, final Uri profilePictureUri) {
        AccountUtil.registerNewAccount(email, password)
                .continueWithTask(new Continuation<AuthResult, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<AuthResult> task) throws Exception {
                        if(task.isSuccessful()) {
                            return AccountUtil.updateBaseInformation(name);
                        }
                        else {
                            Exception e = task.getException();
                            if(e instanceof FirebaseAuthInvalidCredentialsException) {
                                emailEditText.setError("Email is malformed");
                            }
                            else if(e instanceof FirebaseAuthUserCollisionException){
                                emailEditText.setError("Email is already used");
                            }
                            return null;
                        }
                    }
                })
                .continueWithTask(new Continuation<Void, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<Void> task) throws Exception {
                        return AccountUtil.updateUserOtherInformation(phoneNumber, profilePictureUri);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            finishRegistration();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        showCancellationConfirmation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Cancel register
                showCancellationConfirmation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void cancelRegistration() {
        setResult(RegistrationConstant.REGISTER_CANCELLED);
        finish();
    }

    @Override
    public void resumeRegistration() {}
}
