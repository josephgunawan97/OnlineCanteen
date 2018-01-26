package com.example.asus.onlinecanteen;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    DatabaseReference databaseUsers;
    DatabaseReference databaseProducts;
    private static final int MENU_LOGOUT = Menu.FIRST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseProducts = FirebaseDatabase.getInstance().getReference("products");
        addUsers();
        addProducts();

        String[] values = new String[] { "TEST 1", "TEST 2", "AAAAAA"};
        MenuListAdapter adapter = new MenuListAdapter(this, values, null); //imgid = id gambar. Untuk sekarang null dulu
        listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_LOGOUT, 0, "Log Out");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_LOGOUT:
                logout();
                return true;
        }
        return false;
    }

    public void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle("Logging Out");
        alert.show();
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
