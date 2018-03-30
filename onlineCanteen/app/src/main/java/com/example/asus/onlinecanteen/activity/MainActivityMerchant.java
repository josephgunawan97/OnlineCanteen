package com.example.asus.onlinecanteen.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.fragment.MerchantOrderListFragment;
import com.example.asus.onlinecanteen.fragment.MerchantProductListFragment;
import com.example.asus.onlinecanteen.model.Store;
import com.example.asus.onlinecanteen.utils.AccountUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivityMerchant extends AppCompatActivity {

    private static final int MENU_LOGOUT = Menu.FIRST;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager mViewPager;
    TextView title,locate, wallet;
    ImageView image;
    private ChildEventListener eventListener;
    ViewPagerAdapter viewPagerAdapter;
    Store store;


    // Firebase References
    private DatabaseReference databaseUsers;
    private DatabaseReference databaseProducts;
    private DatabaseReference databaseStore;
    private DatabaseReference databaseWallet;

    // Firebase Auth and User
    private FirebaseAuth firebaseAuth;
    private FirebaseUser merchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_merchant);

        locate = (TextView) findViewById(R.id.locate);
        title = (TextView) findViewById(R.id.title) ;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        wallet = (TextView) findViewById(R.id.merchant_wallet);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");

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
        databaseWallet = FirebaseDatabase.getInstance().getReference("wallet");

        databaseWallet.child(merchant.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                wallet.setText("Rp."+dataSnapshot.getValue().toString()+",-");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseStore.child(merchant.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                store = dataSnapshot.getValue(Store.class);
                AccountUtil.setCurrentAccount(store);
                title.setText(store.getStoreName());
                locate.setText(store.getLocation() +" | " + store.getOpenHour() +" - "+ store.getCloseHour());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //title.setText(merchant.getDisplayName());
        if(merchant.getPhotoUrl() != null) {
            //toolbar.setTitle(merchant.getPhotoUrl().toString());
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_merchant, menu);
        //menu.add(0, MENU_LOGOUT, 0, R.string.signOut_title);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                Intent intent = new Intent(this, MerchantSettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_add:
                Intent intent2 = new Intent(this, RegisterProductActivity.class);
                startActivity(intent2);
                return true;
            case R.id.action_delete:
                Intent intent3 = new Intent(this, DeleteProductActivity.class);
                startActivity(intent3);
                return true;
            case R.id.action_edit:
                Intent intent4 = new Intent(this, EditProductActivity.class);
                startActivity(intent4);
                return true;
        }
        return false;
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



