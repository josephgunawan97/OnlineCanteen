package com.example.asus.onlinecanteen.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.fragment.EditCancellationDialogFragment;
import com.example.asus.onlinecanteen.fragment.TimePickerFragment;
import com.example.asus.onlinecanteen.model.Store;
import com.example.asus.onlinecanteen.utils.AccountUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static com.example.asus.onlinecanteen.utils.ExternalStoragePermissionUtil.checkReadExternalStoragePermission;
import static com.example.asus.onlinecanteen.utils.ExternalStoragePermissionUtil.requestReadExternalStoragePermission;

public class EditStoreProfileActivity extends AppCompatActivity
        implements EditCancellationDialogFragment.CancellationHandler {

    private static final String TAG = EditStoreProfileActivity.class.getSimpleName();

    // Request permission code
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 100;

    // Choose image code
    private static final int CHOOSE_PICTURE_FROM_GALLERY_CODE = 101;

    // Profile picture URI
    private Uri profilePictureUri;

    // Views
    private ViewGroup progressBarLayout;
    private TextInputEditText nameEditText;
    private TextInputEditText phoneNumberEditText;
    private ImageView profilePictureImageView;
    private TextInputEditText locationEditText;
    private TextInputEditText bioEditText;
    private TextView openHourTextView;
    private TextView closeHourTextView;
    private Button setOpenHourButton;
    private Button setCloseHourButton;
    private Button cancelButton;
    private Button saveButton;

    // Boolean for edited
    private boolean isProfilePictureEdited;

    // Current Store
    private Store currentStore;

    // Time Picker Handler
    TimePickerFragment.TimeResultHandler openHourHandler = new TimePickerFragment.TimeResultHandler() {
        @Override
        public void onTimeSet(int hourOfDay, int minute) {
            openHourTextView.setText(String.format("%02d:%02d", hourOfDay, minute));
        }
    };
    TimePickerFragment.TimeResultHandler closeHourHandler = new TimePickerFragment.TimeResultHandler() {
        @Override
        public void onTimeSet(int hourOfDay, int minute) {
            closeHourTextView.setText(String.format("%02d:%02d", hourOfDay, minute));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_store_profile);

        setTitle("Edit Account");

        isProfilePictureEdited = false;

        // Initialize views
        progressBarLayout = findViewById(R.id.progress_bar_layout);
        nameEditText = findViewById(R.id.edit_store_name);
        phoneNumberEditText = findViewById(R.id.edit_store_phone_number);
        profilePictureImageView = findViewById(R.id.profile_picture);
        locationEditText = findViewById(R.id.edit_store_location);
        bioEditText = findViewById(R.id.edit_store_bio);
        openHourTextView = findViewById(R.id.edit_store_open_hour);
        closeHourTextView = findViewById(R.id.edit_store_close_hour);
        setOpenHourButton = findViewById(R.id.edit_store_open_button);
        setCloseHourButton = findViewById(R.id.edit_store_close_button);
        cancelButton = findViewById(R.id.edit_cancel_button);
        saveButton = findViewById(R.id.edit_save_button);

        progressBarLayout.setVisibility(View.GONE);

        profilePictureImageView.setOnClickListener(new View.OnClickListener() {
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

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancellationConfirmation();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        currentStore = AccountUtil.getCurrentStore();
        populateFields();
    }

    private void createTimePicker(TimePickerFragment.TimeResultHandler handler) {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setHandler(handler);
        timePickerFragment.show(getSupportFragmentManager(), null);
    }

    private void populateFields() {
        if(currentStore != null) {
            nameEditText.setText(currentStore.getStoreName());
            phoneNumberEditText.setText(currentStore.getPhoneNumber());
            locationEditText.setText(currentStore.getLocation());
            bioEditText.setText(currentStore.getBio());
            openHourTextView.setText(currentStore.getOpenHour());
            closeHourTextView.setText(currentStore.getCloseHour());

            if(currentStore.getImg() != null) {
                Glide.with(this)
                        .load(currentStore.getImg())
                        .into(profilePictureImageView);
            }
        }
    }

    private void saveChanges() {
        // Set up all loading screen here //
        progressBarLayout.setVisibility(View.VISIBLE);

        if(!isEdited()) {
            Toast.makeText(this, "Nothing changed", Toast.LENGTH_SHORT).show();
            return;
        }
        // Update information //
        String name = nameEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String openHour = openHourTextView.getText().toString();
        String closeHour = closeHourTextView.getText().toString();
        String location = locationEditText.getText().toString();
        String bio = bioEditText.getText().toString();

        currentStore.setStoreName(name);
        currentStore.setPhoneNumber(phoneNumber);
        currentStore.setOpenHour(openHour);
        currentStore.setCloseHour(closeHour);
        currentStore.setLocation(location);
        currentStore.setBio(bio);

        Task<Void> updateTask;
        if(isProfilePictureEdited) {
            updateTask = AccountUtil.updateStoreOtherInformation(currentStore, profilePictureUri);
        } else {
            updateTask = AccountUtil.updateStoreOtherInformation(currentStore, null);
        }

        updateTask.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Clear all loading screen here //
                progressBarLayout.setVisibility(View.GONE);

                // Check status
                if(task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Profile saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Failed to saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private void changeProfilePicture() {
        if(profilePictureUri == null) {
            profilePictureImageView.setImageResource(R.drawable.logo3);
        }
        else {
            profilePictureImageView.setImageURI(profilePictureUri);
        }
    }

    private boolean isEdited() {
        String name = nameEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String openHour = openHourTextView.getText().toString();
        String closeHour = closeHourTextView.getText().toString();
        String location = locationEditText.getText().toString();
        String bio = bioEditText.getText().toString();
        return !(name.equals(currentStore.getStoreName()) && phoneNumber.equals(currentStore.getPhoneNumber())
                && openHour.equals(currentStore.getOpenHour()) && closeHour.equals(currentStore.getCloseHour())
                && location.equals(currentStore.getLocation()) && bio.equals(currentStore.getBio())
                && !isProfilePictureEdited);
    }

    private void showCancellationConfirmation() {
        if(progressBarLayout.getVisibility() == View.VISIBLE) return;
        if(!isEdited()) {
            finish();
            return;
        }
        DialogFragment dialogFragment = new EditCancellationDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), null);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == CHOOSE_PICTURE_FROM_GALLERY_CODE) {
                isProfilePictureEdited = true;
                profilePictureUri = data.getData();
                changeProfilePicture();
            }
        }
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
    public void onBackPressed() {
        showCancellationConfirmation();
    }

    @Override
    public void cancelRegistration() {
        finish();
    }

    @Override
    public void resumeRegistration() {}
}
