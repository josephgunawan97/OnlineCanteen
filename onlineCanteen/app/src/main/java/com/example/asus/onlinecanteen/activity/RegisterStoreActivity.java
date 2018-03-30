package com.example.asus.onlinecanteen.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.constant.RegistrationConstant;
import com.example.asus.onlinecanteen.fragment.EditCancellationDialogFragment;
import com.example.asus.onlinecanteen.fragment.RegistrationCancellationDialogFragment;
import com.example.asus.onlinecanteen.fragment.TimePickerFragment;
import com.example.asus.onlinecanteen.utils.AccountUtil;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import static com.example.asus.onlinecanteen.utils.ExternalStoragePermissionUtil.checkReadExternalStoragePermission;
import static com.example.asus.onlinecanteen.utils.ExternalStoragePermissionUtil.requestReadExternalStoragePermission;

public class RegisterStoreActivity extends AppCompatActivity
        implements RegistrationCancellationDialogFragment.CancellationHandler {

    private static final String TAG = RegisterStoreActivity.class.getSimpleName();
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 100;
    private static final int CHOOSE_PICTURE_FROM_GALLERY_CODE = 101;

    // Views
    private ViewGroup progressBarLayout;
    // Core views
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    // Store information views
    private ImageView storePictureImageView;
    private TextInputEditText nameEditText;
    private TextInputEditText phoneNumberEditText;
    private TextInputEditText locationEditText;
    private TextInputEditText bioEditText;
    private TextView openHourTextView;
    private TextView closeHourTextView;
    private Button setOpenHourButton;
    private Button setCloseHourButton;
    private Button changePictureButton;
    private Button signUpButton;

    // Store picture Uri
    private Uri storePictureUri;

    // Time Picker Handler
    TimePickerFragment.TimeResultHandler openHourHandler = new TimePickerFragment.TimeResultHandler() {
        @Override
        public void onTimeSet(int hourOfDay, int minute) {
            openHourTextView.setText(String.format("%02d:%02d", hourOfDay, minute));
            isOpenHourSet = true;
        }
    };
    TimePickerFragment.TimeResultHandler closeHourHandler = new TimePickerFragment.TimeResultHandler() {
        @Override
        public void onTimeSet(int hourOfDay, int minute) {
            closeHourTextView.setText(String.format("%02d:%02d", hourOfDay, minute));
            isCloseHourSet = true;
        }
    };

    // isSet
    private boolean isOpenHourSet;
    private boolean isCloseHourSet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_store);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Store Registration");

        isOpenHourSet = false;
        isCloseHourSet = false;

        // Initialize views
        progressBarLayout = findViewById(R.id.progress_bar_layout);
        emailEditText = findViewById(R.id.registration_email_edit_text);
        passwordEditText = findViewById(R.id.registration_password_edit_text);
        storePictureImageView = findViewById(R.id.store_registration_detail_picture);
        nameEditText = findViewById(R.id.store_registration_detail_name);
        phoneNumberEditText = findViewById(R.id.store_registration_detail_phone_number);
        locationEditText = findViewById(R.id.store_registration_detail_location);
        bioEditText = findViewById(R.id.store_registration_detail_bio);
        openHourTextView = findViewById(R.id.store_registration_detail_open_hour);
        closeHourTextView = findViewById(R.id.store_registration_detail_close_hour);
        setOpenHourButton = findViewById(R.id.store_registration_detail_open_button);
        setCloseHourButton = findViewById(R.id.store_registration_detail_close_button);
        changePictureButton = findViewById(R.id.store_registration_detail_picture_button);
        signUpButton = findViewById(R.id.registration_sign_up_button);

        progressBarLayout.setVisibility(View.GONE);

        // Add listener
        changePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePictureFromGallery();
            }
        });

        setOpenHourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimePicker(openHourHandler);
            }
        });

        setCloseHourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimePicker(closeHourHandler);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    progressBarLayout.setVisibility(View.VISIBLE);

                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    String name = nameEditText.getText().toString();
                    String phoneNumber = phoneNumberEditText.getText().toString();
                    String openHour = openHourTextView.getText().toString();
                    String closeHour = closeHourTextView.getText().toString();
                    String location = locationEditText.getText().toString();
                    String bio = bioEditText.getText().toString();

                    registerNewStore(email, password, name, phoneNumber, openHour, closeHour, location, bio);
                }
            }
        });
    }

    private void registerNewStore(final String email, String password, final String name, final String phoneNumber, final String openHour,
                                  final String closeHour, final String location, final String bio) {
        AccountUtil.registerNewAccount(email, password)
                .continueWithTask(new Continuation<AuthResult, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<AuthResult> task) throws Exception {
                        if (task.isSuccessful()) {
                            return AccountUtil.createStoreOtherInformation(name, phoneNumber, storePictureUri, email, openHour, closeHour, location, bio);
                        } else {
                            Exception e = task.getException();
                            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                emailEditText.setError("Email is malformed");
                            } else if (e instanceof FirebaseAuthUserCollisionException) {
                                emailEditText.setError("Email is already used");
                            }
                            return null;
                        }
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            finishRegistration();
                        }
                    }
                });
    }

    private void createTimePicker(TimePickerFragment.TimeResultHandler handler) {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setHandler(handler);
        timePickerFragment.show(getSupportFragmentManager(), null);
    }

    private void choosePictureFromGallery() {
        if (checkReadExternalStoragePermission(this)) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, CHOOSE_PICTURE_FROM_GALLERY_CODE);
        } else {
            requestReadExternalStoragePermission(this, REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    private void changeStorePicture() {
        if (storePictureUri == null) {
            storePictureImageView.setImageResource(R.drawable.logo3);
        } else {
            storePictureImageView.setImageURI(storePictureUri);
        }
    }

    public boolean validateForm() {
        String email = new String(emailEditText.getText().toString());
        String password = new String(passwordEditText.getText().toString());
        String name = new String(nameEditText.getText().toString());
        String phoneNumber = new String(phoneNumberEditText.getText().toString());
        String location = new String(locationEditText.getText().toString());

        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            valid = false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            valid = false;
        }

        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Name is required");
            valid = false;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumberEditText.setError("Phone number is required");
            valid = false;
        }

        if (TextUtils.isEmpty(location)) {
            locationEditText.setError("Phone number is required");
            valid = false;
        }

        if (!isOpenHourSet) {
            setOpenHourButton.setError("Open hour is required");
            valid = false;
        }

        if (!isCloseHourSet) {
            setCloseHourButton.setError("Open hour is required");
            valid = false;
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        if (progressBarLayout.getVisibility() == View.GONE) showCancellationConfirmation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Do something
                choosePictureFromGallery();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_PICTURE_FROM_GALLERY_CODE) {
                storePictureUri = data.getData();
                changeStorePicture();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Cancel register
                if (progressBarLayout.getVisibility() == View.GONE) showCancellationConfirmation();
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