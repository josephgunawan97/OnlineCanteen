package com.example.asus.onlinecanteen;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {
    ImageView imageView;
    Button button, submitbtn;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    String profPicUrl;
    FirebaseAuth mAuth;

    //EditText
    EditText usernameET, passwordET, emailET, nimET, phoneET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        imageView = (ImageView)findViewById(R.id.imageinput);
        button =  (Button)findViewById(R.id.browse);
        submitbtn = (Button) findViewById(R.id.registerbtn);

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
                uploadImage();

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

        usernameET = (EditText) findViewById(R.id.usrnamefill);
        passwordET = (EditText) findViewById(R.id.passwordfill);
        emailET = (EditText) findViewById(R.id.emailfill);
        nimET = (EditText) findViewById(R.id.nimfill);
        phoneET = (EditText) findViewById(R.id.phonefill);

        mAuth.createUserWithEmailAndPassword(emailET.getText().toString(),passwordET.getText().toString());

        //CONNECT USER AUTH WITH DATA (USERNAME AND PHOTO) - belum bisa
        /*
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null && imageUri!=null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(usernameET.getText().toString())
                    .setPhotoUri(Uri.parse(profPicUrl))
                    .build();
        }
       */

        //BACK TO LOGIN PAGE - after success
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    //To upload image
    private void uploadImage() {
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");
        if (imageUri!=null){
            profileImageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    profPicUrl = taskSnapshot.getDownloadUrl().toString();
                    submitData();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Image failed to upload",Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    //DRAFT
    //private void addUsers() {
       // User user = new User("Jessica","00000013452", "00000013452", "jessicaseaan@gmail.com", "A", null ,"081511030993" );
       // databaseUsers.push().setValue(user);
    //}


}
