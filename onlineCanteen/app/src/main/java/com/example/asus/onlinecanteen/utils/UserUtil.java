package com.example.asus.onlinecanteen.utils;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.asus.onlinecanteen.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UserUtil {

    public enum UserType {
        USER,  STORE;
    }

    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private static Uri latestProfilePhotoUri;

    @NonNull
    public static Task<AuthResult> registerNewAccount(String email, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password);
    }

    @NonNull
    public static Task<Void> updateBaseInformation(String name) {
        // Check current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null) return null;

        // Add update information
        UserProfileChangeRequest.Builder requestBuilder = new UserProfileChangeRequest.Builder();
        requestBuilder.setDisplayName(name);

        return firebaseUser.updateProfile(requestBuilder.build());
    }

    public static Task<Void> updateOtherInformation(final UserType userType, final String phoneNumber, Uri profilePictureUri) {
        return uploadProfilePicture(profilePictureUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Void>>() {
            @Override
            public Task<Void> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                String url = task.getResult().getDownloadUrl().toString();
                return updateInformationOnDatabase(userType, phoneNumber, url);
            }
        });
    }

    private static Task<Void> updateInformationOnDatabase(UserType userType, String phoneNumber, String profilePictureUrl) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("all_users");

        Task<Void> returnValue = null;
        switch (userType) {
            case USER:
                returnValue = reference.child(firebaseUser.getUid()).setValue(new User(phoneNumber, profilePictureUrl));
                break;
            case STORE:
                break;
            default:
                break;
        }

        return returnValue;
    }

    private static UploadTask uploadProfilePicture(Uri profilePictureUri) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference directoryReference = storageReference.child("profilepics");
        StorageReference profilePictureReference = directoryReference.child(firebaseUser.getUid() + ".jpg");

        if (profilePictureUri != null){
            return profilePictureReference.putFile(profilePictureUri);
        }
        else return null;
    }
}