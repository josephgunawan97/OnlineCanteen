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

import java.util.regex.Pattern;

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

    public static Task<Void> createUserOtherInformation(String name, String phoneNumber, Uri profilePictureUri) {
        return updateUserOtherInformation(name, phoneNumber, profilePictureUri).continueWithTask(new Continuation<Void, Task<Void>>() {
            @Override
            public Task<Void> then(@NonNull Task<Void> task) throws Exception {
                return createRole("USER");
            }
        }).continueWithTask(new Continuation<Void, Task<Void>>() {
            @Override
            public Task<Void> then(@NonNull Task<Void> task) throws Exception {
                return createEmptyWallet();
            }
        }).continueWithTask(new Continuation<Void, Task<Void>>() {
            @Override
            public Task<Void> then(@NonNull Task<Void> task) throws Exception {
                return createMappingEmailToUid();
            }
        });
    }

    public static Task<Void> createStoreOtherInformation(final String name, final String phoneNumber, Uri profilePictureUri,
                                                         final String email, final String openHour, final String closeHour,
                                                         final String location, final String bio) {
        return updateStoreOtherInformation(name, phoneNumber, profilePictureUri, email, openHour, closeHour, location, bio)
                .continueWithTask(new Continuation<Void, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<Void> task) throws Exception {
                        return createRole("STORE");
                    }
                }).continueWithTask(new Continuation<Void, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<Void> task) throws Exception {
                        return createEmptyWallet();
                    }
                }).continueWithTask(new Continuation<Void, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<Void> task) throws Exception {
                        return createMappingEmailToUid();
                    }
                });
    }

    public static Task<Void> updateUserOtherInformation(final String name, final String phoneNumber, Uri profilePictureUri) {
        if(profilePictureUri == null) {
            return updateUserInformationOnDatabase(name, phoneNumber, null);
        }
        else return uploadProfilePicture(profilePictureUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Void>>() {
            @Override
            public Task<Void> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                String url = task.getResult().getDownloadUrl().toString();

                return updateUserInformationOnDatabase(name, phoneNumber, url);
            }
        });
    }

    public static Task<Void> updateUserOtherInformation(final User user, Uri profilePictureUri) {
        if(profilePictureUri == null) {
            return updateUserInformationOnDatabase(user);
        }
        else return uploadProfilePicture(profilePictureUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Void>>() {
            @Override
            public Task<Void> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                String url = task.getResult().getDownloadUrl().toString();
                user.setProfilePictureUrl(url);
                return updateUserInformationOnDatabase(user);
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
    public static Task<Void> updateStoreOtherInformation(final Store store, Uri profilePictureUri) {
        if(profilePictureUri == null) {
            return updateStoreInformationOnDatabase(store);
        }
        else return uploadProfilePicture(profilePictureUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Void>>() {
            @Override
            public Task<Void> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                String url = task.getResult().getDownloadUrl().toString();
                store.setImg(url);
                return updateStoreInformationOnDatabase(store);
            }
        });
    }


    private static Task<Void> updateUserInformationOnDatabase(String name, String phoneNumber, String profilePictureUrl) {
        return updateUserInformationOnDatabase(new User(name, phoneNumber, profilePictureUrl));
    }

    private static Task<Void> updateUserInformationOnDatabase(User user) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("users");

        return reference.child(firebaseUser.getUid()).setValue(user);
    }

    private static Task<Void> updateStoreInformationOnDatabase(String name, String phoneNumber, String email,
                                                               String profilePictureUrl, String openHour,
                                                               String closeHour, String location, String bio) {
        return updateStoreInformationOnDatabase(new Store(name, phoneNumber, email, profilePictureUrl, openHour, closeHour, location, bio));
    }

    private static Task<Void> updateStoreInformationOnDatabase(Store store) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("store");

        return reference.child(firebaseUser.getUid()).setValue(store);
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

    private static Task<Void> createRole(String role) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("role");

        return reference.child(firebaseUser.getUid()).setValue(role);
    }

    private static Task<Void> createEmptyWallet() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("wallet");

        return reference.child(firebaseUser.getUid()).setValue(0);
    }

    private static Task<Void> createMappingEmailToUid() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("emailtouid");

        return reference.child(firebaseUser.getEmail().replaceAll(Pattern.quote("."), ",")).setValue(firebaseUser.getUid());
    }
}