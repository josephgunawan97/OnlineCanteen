package com.example.asus.onlinecanteen.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.fragment.MainUserFragment;

public class MainUserActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        MainUserFragment userFragment = new MainUserFragment();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_user_frame_layout, userFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
