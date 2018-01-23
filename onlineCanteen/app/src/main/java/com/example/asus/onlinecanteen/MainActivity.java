package com.example.asus.onlinecanteen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseUsers;
    DatabaseReference databaseProducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseProducts = FirebaseDatabase.getInstance().getReference("products");
        addUsers();
        addProducts();

    }

    private void addProducts() {
        String id = databaseProducts.push().getKey();
        Product product = new Product(id,"Jessica","Nasi", 30, 12000 );
        databaseProducts.setValue(product);
    }


    private void addUsers() {

        String id = databaseUsers.push().getKey();
        User user = new User(id,"Jessica","00000013452", "00000013452", "jessicaseaan@gmail.com", "A", "jess.jpg" ,"081511030993" );
        databaseUsers.setValue(user);

    }


}
