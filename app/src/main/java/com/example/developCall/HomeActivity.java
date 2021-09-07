package com.example.developCall;


import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.developCall.Alarm.Alarm_Fragment;
import com.example.developCall.Fragment.Fragment1;
import com.example.developCall.Fragment.Fragment2;
import com.example.developCall.Fragment.FriendListFragment;
import com.example.developCall.Fragment.GroupListFragment;
import com.example.developCall.Fragment.MainFragment;
import com.example.developCall.Function.CallReceiver2;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Fragment1 fragment1;
    Fragment2 fragment2;
    MainFragment mainFragment;
    Alarm_Fragment alarm_fragment;
    FriendListFragment friendListFragment;

    GroupListFragment groupListFragment;
    FragmentTransaction transaction;
    private BroadcastReceiver mReceiveer;



    String check ="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        mReceiveer = new CallReceiver2();

        Intent intent = getIntent();
        String getName, getContent;
        check = intent.getStringExtra("check");

        fragmentManager = getSupportFragmentManager();

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        mainFragment = new MainFragment();

        groupListFragment = new GroupListFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.home_frame, fragment1).commitAllowingStateLoss();



        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                transaction = fragmentManager.beginTransaction();

                switch (item.getItemId()) {
                    case R.id.navigation_1:
                        item.setChecked(true);
                        transaction.replace(R.id.home_frame, fragment1).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_2:
                        item.setChecked(true);
                        transaction.replace(R.id.home_frame, groupListFragment).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_3:
                        item.setChecked(true);
                        transaction.replace(R.id.home_frame, mainFragment).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_4:
                        item.setChecked(true);
                        alarm_fragment = new Alarm_Fragment();
                        try{
                            if(check.equals("1"))
                            {
                                Log.d("tag", "확인 성공");
                                Bundle bundle = new Bundle();
                                bundle.putInt("check", 1);
                                alarm_fragment.setArguments(bundle);
                            }
                        }
                        catch(Exception e )
                        {
                            e.printStackTrace();
                        }
                        transaction.replace(R.id.home_frame, alarm_fragment).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_5:
                        item.setChecked(true);
                        //transaction.replace(R.id.home_frame, fragment2).commitAllowingStateLoss();
                        break;
                }
                return false;
            }
        });
    }

//    protected void onResume() {
//        super.onResume();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_POWER_CONNECTED);
//        filter.addAction(CallReceiver2.MyAction);
//        registerReceiver(mReceiveer, filter);
//    }
//
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(mReceiveer);
//    }
//
//    public void connect(View view) {
//        Intent intent = new Intent(CallReceiver2.MyAction);
//        sendBroadcast(intent);
//    }
}
