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
                if(validateForm()) {
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
                        return AccountUtil.createStoreOtherInformation(name, phoneNumber, storePictureUri, email, openHour, closeHour, location, bio);
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

    private void createTimePicker(TimePickerFragment.TimeResultHandler handler) {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setHandler(handler);
        timePickerFragment.show(getSupportFragmentManager(), null);
    }

    private void choosePictureFromGallery(){
        if(checkReadExternalStoragePermission(this)) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, CHOOSE_PICTURE_FROM_GALLERY_CODE);
        }
        else {
            requestReadExternalStoragePermission(this, REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    private void changeStorePicture() {
        if(storePictureUri == null) {
            storePictureImageView.setImageResource(R.drawable.logo3);
        }
        else {
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

        if(TextUtils.isEmpty(location)) {
            locationEditText.setError("Phone number is required");
            valid = false;
        }

        if(!isOpenHourSet) {
            setOpenHourButton.setError("Open hour is required");
            valid = false;
        }

        if(!isCloseHourSet) {
            setCloseHourButton.setError("Open hour is required");
            valid = false;
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        if(progressBarLayout.getVisibility() == View.GONE) showCancellationConfirmation();
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
                if(progressBarLayout.getVisibility() == View.GONE) showCancellationConfirmation();
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

    /*    private static final int REQUEST_READ_EXTERNAL_STORAGE = 999;

    ImageView imageView;
    Button button, submitbtn;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    String profPicUrl;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference StoreReferences, walletReferences, roleReferences, emailReferences;
    private DatabaseReference databaseStore;

    //EditText
    EditText usernameET, passwordET, emailET, openhourET, closehourET, locationET;

    //String
    String username, password, email, openh, closeh, location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(com.example.asus.onlinecanteen.R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_store);

        mAuth = FirebaseAuth.getInstance();

        imageView = findViewById(R.id.storeimageinput);
        button =  findViewById(R.id.storebrowse);
        submitbtn = findViewById(R.id.storeregisterbtn);
        usernameET = findViewById(R.id.storenamefill);
        passwordET = findViewById(R.id.storepasswordfill);
        emailET = findViewById(R.id.storeemailfill);
        openhourET = findViewById(R.id.storeopenhour);
        closehourET = findViewById(R.id.storeclosehour);
        locationET = findViewById(R.id.storelocationfill);


        //Browse Image in Gallery & set as Profile Picture
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        //Submit data for Sign Up & Upload to Storage
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = usernameET.getText().toString();
                password = passwordET.getText().toString();
                email = emailET.getText().toString();
                openh = openhourET.getText().toString();
                closeh = closehourET.getText().toString();
                location= locationET.getText().toString();
                submitData();
            }
        });

        databaseStore = FirebaseDatabase.getInstance().getReference("store");
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    //To submit data
    private void submitData() {

        if(!validateRegisterInfo()) {
            // Field is not filled
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailET.getText().toString(),passwordET.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            if(imageUri != null) {
                                if(ContextCompat.checkSelfPermission(RegisterStoreActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    requestReadStoragePermission();
                                }
                            }
                            addAdditionalUserInformation();
                        } else {
                            emailET.setText("");
                            emailET.setError("Email is registered");
                            passwordET.setText("");
                        }
                    }
                });
    }

    private String uid;

    private void addAdditionalUserInformation() {
        mAuth.signInWithEmailAndPassword(emailET.getText().toString(), passwordET.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();

                            uid = user.getUid();
                            String img = uploadImage();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username).build();
                            user.updateProfile(profileUpdates);

                            Store storeInfo = new Store(username, openh, closeh, location, img, email);
                            StoreReferences = FirebaseDatabase.getInstance().getReference("store").child(uid);
                            StoreReferences.setValue(storeInfo);


                            walletReferences = FirebaseDatabase.getInstance().getReference("wallet").child(uid);
                            walletReferences.setValue(0);

                            roleReferences = FirebaseDatabase.getInstance().getReference("role").child(uid);
                            roleReferences.setValue("STORE");

                            String passemail = email.replaceAll(Pattern.quote("."),",");
                            emailReferences = FirebaseDatabase.getInstance().getReference("emailtouid").child(passemail);
                            emailReferences.setValue(uid);

                            backToLoginScreen();

                        }
                    }
                });





    }

    //To upload image
    private String uploadImage() {

        Log.d(TAG, "Uploading...");
        final StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");
        if (imageUri!=null){
            profileImageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl =taskSnapshot.getDownloadUrl();
                    profPicUrl = downloadUrl.toString();
                    Log.d(TAG, "Success in uploading");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Image failed to upload",Toast.LENGTH_LONG).show();
                }
            });
        }
        return profPicUrl;
    }

    private boolean validateRegisterInfo() {
        boolean valid = true;

        if(TextUtils.isEmpty(username)) {
            usernameET.setError("Username required");
            valid = false;
        } else {
            usernameET.setError(null);
        }

        if(TextUtils.isEmpty(password)) {
            passwordET.setError("Password required");
            valid = false;
        } else {
            passwordET.setError(null);
        }

        if(TextUtils.isEmpty(email)) {
            emailET.setError("Email required");
            valid = false;
        } else {
            emailET.setError(null);
        }

        if(TextUtils.isEmpty(openh)) {
            openhourET.setError("Open Hour required");
            valid = false;
        } else {
            openhourET.setError(null);
        }

        if(TextUtils.isEmpty(closeh)) {
            closehourET.setError("Close Hour required");
            valid = false;
        } else {
            closehourET.setError(null);
        }

        if(TextUtils.isEmpty(location)) {
            locationET.setError("Location required");
            valid = false;
        } else {
            locationET.setError(null);
        }

        return valid;
    }


    private void requestReadStoragePermission() {
        ActivityCompat.requestPermissions(RegisterStoreActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addAdditionalUserInformation();
            }
        }
    }

    private void backToLoginScreen() {
        if(mAuth != null) {
            mAuth.signOut();
        }
        // GO TO LOGIN PAGE - after success
        Intent intent = new Intent(RegisterStoreActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }*/
}
