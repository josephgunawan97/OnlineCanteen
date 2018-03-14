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

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.fragment.MainUserFragment;
import com.example.asus.onlinecanteen.fragment.UserProductListFragment;
import com.example.asus.onlinecanteen.model.Store;

public class MainUserActivity extends AppCompatActivity implements MainUserFragment.StoreClickHandler {

    private NavigationView navigationView;
    private FragmentManager fragmentManager;

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
    }

    @Override
    public void storeClickHandler(Store store) {
        UserProductListFragment fragment = new UserProductListFragment();
        fragment.setCurrentStore(store);
        changeFragment(fragment);
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_user_frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
