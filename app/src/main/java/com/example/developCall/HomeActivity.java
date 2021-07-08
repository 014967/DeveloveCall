package com.example.developCall;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.developCall.Fragment.Fragment1;
import com.example.developCall.Fragment.Fragment2;
import com.example.developCall.Fragment.MainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Fragment1 fragment1;
    Fragment2 fragment2;
    MainFragment mainFragment;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        fragmentManager = getSupportFragmentManager();

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        mainFragment = new MainFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.home_frame, fragment1).commitAllowingStateLoss();

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_1:
                        transaction.replace(R.id.home_frame, fragment1).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_3:
                        transaction.replace(R.id.home_frame, mainFragment).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_5:
                        transaction.replace(R.id.home_frame, fragment2).commitAllowingStateLoss();
                        break;
                }
                return false;
            }
        });
    }
}
