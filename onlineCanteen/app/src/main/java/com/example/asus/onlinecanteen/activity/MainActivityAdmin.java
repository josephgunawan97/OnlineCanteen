package com.example.asus.onlinecanteen.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.AdminMenuAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivityAdmin extends AppCompatActivity {

    int[] images = {R.drawable.logo, R.drawable.logo};

    String[] name = {"User Top Up", "Store Withdrawal"};

    String[] description = {"Untuk menambahkan saldo user", "Untuk membuat request store untuk menarik uang"};

    String[] TAG = {"TOP_UP","WITHDRAW"};

    ListView lView;
    ListAdapter lAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        lView = (ListView) findViewById(R.id.androidList);

        lAdapter = new AdminMenuAdapter(MainActivityAdmin.this, name, description, images, TAG);

        lView.setAdapter(lAdapter);

        // Fetch the service account key JSON file contents
        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream("path/to/serviceAccount.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent;
                switch (TAG[i]){
                    case "TOP_UP":
                        intent = new Intent(MainActivityAdmin.this, AdminTopUpActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case "WITHDRAW":
                        intent = new Intent(MainActivityAdmin.this, AdminStoreWithdrawal.class);
                        startActivity(intent);
                        finish();
                        break;
                    default: break;
                }

            }
        });

    }
}
