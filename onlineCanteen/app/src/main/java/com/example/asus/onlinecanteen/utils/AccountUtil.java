package com.example.asus.onlinecanteen.utils;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.asus.onlinecanteen.model.Store;
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

public class AccountUtil {

    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

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

    public static Task<Void> updateUserOtherInformation(final String phoneNumber, Uri profilePictureUri) {
        if(profilePictureUri == null) {
            return updateUserInformationOnDatabase(phoneNumber, null);
        }
        else return uploadProfilePicture(profilePictureUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Void>>() {
            @Override
            public Task<Void> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                String url = task.getResult().getDownloadUrl().toString();

                return updateUserInformationOnDatabase(phoneNumber, url);
            }
        });
    }

    public static Task<Void> updateStoreOtherInformation(final String name, final String phoneNumber, Uri profilePictureUri,
                                                         final String email, final String openHour, final String closeHour,
                                                         final String location, final String bio) {
        if(profilePictureUri == null) {
            return updateStoreInformationOnDatabase(name, phoneNumber, email, null, openHour, closeHour, location, bio);
        }
        else return uploadProfilePicture(profilePictureUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Void>>() {
            @Override
            public Task<Void> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                String url = task.getResult().getDownloadUrl().toString();

                return updateStoreInformationOnDatabase(name, phoneNumber, email, url, openHour, closeHour, location, bio);
            }
        });
    }

    private static Task<Void> updateUserInformationOnDatabase(String phoneNumber, String profilePictureUrl) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("users");

        return reference.child(firebaseUser.getUid()).setValue(new User(phoneNumber, profilePictureUrl));
    }

    private static Task<Void> updateStoreInformationOnDatabase(String name, String phoneNumber, String email,
                                                               String profilePictureUrl, String openHour,
                                                               String closeHour, String location, String bio) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("stores");

        return reference.child(firebaseUser.getUid())
                .setValue(new Store(name, phoneNumber, email, profilePictureUrl, openHour, closeHour, location, bio));
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