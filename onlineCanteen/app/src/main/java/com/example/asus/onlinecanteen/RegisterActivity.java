package com.example.asus.onlinecanteen;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.asus.onlinecanteen.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    ImageView imageView;
    Button button, submitbtn;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    String profPicUrl;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference userReferences;

    //EditText
    EditText usernameET, passwordET, emailET, nimET, phoneET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        imageView = findViewById(R.id.imageinput);
        button =  findViewById(R.id.browse);
        submitbtn = findViewById(R.id.registerbtn);
        usernameET = findViewById(R.id.usrnamefill);
        passwordET = findViewById(R.id.passwordfill);
        emailET = findViewById(R.id.emailfill);
        nimET = findViewById(R.id.nimfill);
        phoneET = findViewById(R.id.phonefill);


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
                submitData();
            }
        });
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
                            addAdditionalUserInformation();
                            // GO TO MAIN PAGE - after success
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            emailET.setText("");
                            emailET.setError("Email is registered");
                            passwordET.setText("");
                        }
                    }
                });
    }

    private void addAdditionalUserInformation() {
        mAuth.signInWithEmailAndPassword(emailET.getText().toString(), passwordET.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            if (user!=null && imageUri!=null) {
                                // MASIH ERROR
/*                                uploadImage();
                                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(usernameET.getText().toString())
                                        .setPhotoUri(Uri.parse(profPicUrl))
                                        .build();
                                user.updateProfile(profile);
*/
                                String uid = user.getUid();
                                Log.d(TAG, uid);
                                User userInfo = new User(nimET.getText().toString(), "USER", phoneET.getText().toString());
                                userReferences = FirebaseDatabase.getInstance().getReference("users").child(uid);
                                userReferences.setValue(userInfo);
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //To upload image
    private void uploadImage() {
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");
        if (imageUri!=null){
            // SecurityException
            profileImageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    profPicUrl = taskSnapshot.getDownloadUrl().toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Image failed to upload",Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean validateRegisterInfo() {
        boolean valid = true;

        String username = usernameET.getText().toString();
        if(TextUtils.isEmpty(username)) {
            usernameET.setError("Username required");
            valid = false;
        } else {
            usernameET.setError(null);
        }

        String password = passwordET.getText().toString();
        if(TextUtils.isEmpty(password)) {
            passwordET.setError("Password required");
            valid = false;
        } else {
            passwordET.setError(null);
        }

        String email = emailET.getText().toString();
        if(TextUtils.isEmpty(email)) {
            emailET.setError("Email required");
            valid = false;
        } else {
            emailET.setError(null);
        }

        String nim = nimET.getText().toString();
        if(TextUtils.isEmpty(nim)) {
            nimET.setError("Email required");
            valid = false;
        } else {
            nimET.setError(null);
        }

        String phone = phoneET.getText().toString();
        if(TextUtils.isEmpty(phone)) {
            phoneET.setError("Email required");
            valid = false;
        } else {
            phoneET.setError(null);
        }

        return valid;
    }

    //DRAFT
    //private void addUsers() {
       // User user = new User("Jessica","00000013452", "00000013452", "jessicaseaan@gmail.com", "A", null ,"081511030993" );
       // databaseUsers.push().setValue(user);
    //}


}
