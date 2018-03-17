package com.example.asus.onlinecanteen.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.fragment.MainUserFragment;
import com.example.asus.onlinecanteen.fragment.UserProductListFragment;
import com.example.asus.onlinecanteen.model.Store;
import com.example.asus.onlinecanteen.utils.WalletUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainUserActivity extends AppCompatActivity implements MainUserFragment.StoreClickHandler {

    private NavigationView navigationView;
    private FragmentManager fragmentManager;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference walletRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        // Set up customized toolbar
        Toolbar toolbar = findViewById(R.id.main_user_toolbar);
        setSupportActionBar(toolbar);
        // Add drawer toggle
        DrawerLayout drawer = findViewById(R.id.main_user_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Put default fragment
        MainUserFragment userFragment = new MainUserFragment();
        userFragment.setStoreClickHandler(this);

        fragmentManager = getSupportFragmentManager();
        changeFragment(userFragment);

        // Set up navigation view
        navigationView = findViewById(R.id.main_user_navigation_view);

        //Get User
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        //Set up header navigation
        NavigationView navigationView = (NavigationView) findViewById(R.id.main_user_navigation_view);
        View header=navigationView.getHeaderView(0);
        final TextView userWallet = (TextView)header.findViewById(R.id.user_wallet);
        TextView username = (TextView) header.findViewById(R.id.user_navigation_user_name) ;
        TextView email = (TextView) header.findViewById(R.id.user_navigation_user_email) ;
        //Get wallet amount
        walletRef = FirebaseDatabase.getInstance().getReference().child("wallet");
        walletRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    userWallet.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void storeClickHandler(Store store) {
        UserProductListFragment fragment = new UserProductListFragment();
        fragment.setCurrentStore(store);
        changeFragment(fragment);
    }

    public void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_user_frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
