package com.example.asus.onlinecanteen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.fragment.MerchantOrderListFragment;
import com.example.asus.onlinecanteen.fragment.MerchantProductListFragment;
import com.example.asus.onlinecanteen.model.Store;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivityMerchant extends AppCompatActivity {

    private static final int MENU_LOGOUT = Menu.FIRST;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager mViewPager;
    ViewPagerAdapter viewPagerAdapter;

    // Firebase References
    private DatabaseReference databaseUsers;
    private DatabaseReference databaseProducts;
    private DatabaseReference databaseStore;

    // Firebase Auth and User
    private FirebaseAuth firebaseAuth;
    private FirebaseUser merchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_merchant);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new MerchantProductListFragment(), "Product");
        viewPagerAdapter.addFragments(new MerchantOrderListFragment(), "Order");

        mViewPager.setAdapter(viewPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        // Get User
        firebaseAuth = FirebaseAuth.getInstance();
        merchant = firebaseAuth.getCurrentUser();

        // Initialize References
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseProducts = FirebaseDatabase.getInstance().getReference("products");
        databaseStore = FirebaseDatabase.getInstance().getReference("store");

        toolbar.setTitle(merchant.getDisplayName());

    }
    public void refreshNow (){
        finish();
        overridePendingTransition( 0, 0);
        startActivity(getIntent());
        overridePendingTransition( 0, 0);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_merchant, menu);
        //menu.add(0, MENU_LOGOUT, 0, R.string.signOut_title);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signout:
                logout();
                return true;
            case R.id.action_add:
                Intent intent = new Intent(this, RegisterProduct.class);
                startActivity(intent);
                return true;
            case R.id.action_delete:
                Intent intent2 = new Intent(this, DeleteProduct.class);
                startActivity(intent2);
                return true;
            case R.id.action_edit:
                Intent intent3 = new Intent(this, EditProductActivity.class);
                startActivity(intent3);
                return true;
        }
        return false;
    }

    //User Logout
    public void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.signOut_confirmation)
                .setCancelable(false)
                .setPositiveButton(R.string.signOut_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        firebaseAuth = FirebaseAuth.getInstance();
                        merchant = firebaseAuth.getCurrentUser();
                        firebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivityMerchant.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.signOut_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle(R.string.signOut_title);
        alert.show();


    }

public class ViewPagerAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> tabTitles = new ArrayList<>();

        public void addFragments (Fragment fragments,String tabTitles){
            this.fragments.add(fragments);
            this.tabTitles.add(tabTitles);
        }

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);
        }
    }

}



