package com.example.developCall;


import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.developCall.Fragment.Fragment1;
import com.example.developCall.Fragment.Fragment2;
import com.example.developCall.Fragment.MainFragment;

import com.example.developCall.Function.CallReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Fragment1 fragment1;
    Fragment2 fragment2;
    MainFragment mainFragment;
    FragmentTransaction transaction;
    private BroadcastReceiver mReceiveer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        mReceiveer = new CallReceiver();

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

    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(CallReceiver.MyAction);
        registerReceiver(mReceiveer, filter);
    }

    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiveer);
    }

    public void connect(View view) {
        Intent intent = new Intent(CallReceiver.MyAction);
        sendBroadcast(intent);
    }
}
