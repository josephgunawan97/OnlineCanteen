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
import android.widget.Button;
import android.widget.ImageView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.fragment.EditCancellationDialogFragment;
import com.example.asus.onlinecanteen.model.User;
import com.example.asus.onlinecanteen.utils.AccountUtil;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static com.example.asus.onlinecanteen.utils.ExternalStoragePermissionUtil.checkReadExternalStoragePermission;
import static com.example.asus.onlinecanteen.utils.ExternalStoragePermissionUtil.requestReadExternalStoragePermission;

public class EditUserProfileActivity extends AppCompatActivity
        implements EditCancellationDialogFragment.CancellationHandler{

    // Request permission code
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 100;

    // Choose image code
    private static final int CHOOSE_PICTURE_FROM_GALLERY_CODE = 101;

    // Profile picture URI
    private Uri profilePictureUri;

    // Views
    private TextInputEditText nameEditText;
    private TextInputEditText phoneNumberEditText;
    private ImageView profilePictureImageView;
    private Button cancelButton;
    private Button saveButton;

    // Boolean for edited
    private boolean isProfilePictureEdited;
    private boolean isEdited;

    // Current existing values
    private String existingName;
    private String existingPhoneNumber;

    // Current User
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        setTitle("Edit Account");

        isProfilePictureEdited = false;
        isEdited = false;

        // Initialize views
        nameEditText = findViewById(R.id.edit_name);
        phoneNumberEditText = findViewById(R.id.edit_phone_number);
        profilePictureImageView = findViewById(R.id.profile_picture);
        cancelButton = findViewById(R.id.edit_cancel_button);
        saveButton = findViewById(R.id.edit_save_button);

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
    }

    private void saveChanges() {
        // Set up all loading screen here //

        // Update information //
        String name = nameEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        currentUser.setName(name);
        currentUser.setPhone(phoneNumber);
        AccountUtil.updateBaseInformation(name).continueWithTask(new Continuation<Void, Task<Void>>() {
            @Override
            public Task<Void> then(@NonNull Task<Void> task) throws Exception {
                if(isProfilePictureEdited) {
                    return AccountUtil.updateUserOtherInformation(currentUser, profilePictureUri);
                }
                else {
                    return AccountUtil.updateUserOtherInformation(currentUser, null);
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Clear all loading screen here //

                // Check status
                if(task.isSuccessful()) {

                }
                else {

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

    private void showCancellationConfirmation() {
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
