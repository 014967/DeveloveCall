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

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.User;
import com.example.developCall.Alarm.Alarm_Fragment;
import com.example.developCall.Calendar.Calendar_Fragment;
import com.example.developCall.Fragment.Fragment1;
import com.example.developCall.Fragment.Fragment2;
import com.example.developCall.Fragment.FriendListFragment;
import com.example.developCall.Fragment.GroupListFragment;
import com.example.developCall.Fragment.HomeFragment;
import com.example.developCall.Fragment.MainFragment;
import com.example.developCall.Fragment.MyPageFragment;
import com.example.developCall.Fragment.TabFragment1;
import com.example.developCall.Fragment.TabFragment2;
import com.example.developCall.Fragment.TabFragment3;
import com.example.developCall.Function.CallReceiver2;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Fragment1 fragment1;
    Fragment2 fragment2;
    MainFragment mainFragment;
    Alarm_Fragment alarm_fragment;
    HomeFragment home_fragment;
    Calendar_Fragment calendar_fragment;

    MyPageFragment myPageFragment;
    FriendListFragment friendListFragment;
    TabFragment1 tabFragment1;
    TabFragment2 tabFragment2;
    TabFragment3 tabFragment3;

    GroupListFragment groupListFragment;
    FragmentTransaction transaction;
    private BroadcastReceiver mReceiveer;


    String check ="";


    String username;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        mReceiveer = new CallReceiver2();

        userId = Amplify.Auth.getCurrentUser().getUserId();
        getUser(userId);


        Intent intent = getIntent();
        check = intent.getStringExtra("check");

        fragmentManager = getSupportFragmentManager();

        //fragment1 = new Fragment1();

        fragment2 = new Fragment2();
        mainFragment = new MainFragment();
        home_fragment = new HomeFragment();
        myPageFragment = new MyPageFragment();
        calendar_fragment = new Calendar_Fragment();
        groupListFragment = new GroupListFragment();

        tabFragment1 = new TabFragment1();
        tabFragment2 = new TabFragment2();
        tabFragment3 = new TabFragment3();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.home_frame, home_fragment).commitAllowingStateLoss();


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        bottomNavigationView.getMenu().getItem(2).setChecked(true);




        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                transaction = fragmentManager.beginTransaction();

                switch (item.getItemId()) {
                    case R.id.navigation_1:
                        item.setChecked(true);
                        transaction.replace(R.id.home_frame, calendar_fragment).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_2:
                        item.setChecked(true);
                        transaction.replace(R.id.home_frame, groupListFragment).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_3:
                        item.setChecked(true);
                        transaction.replace(R.id.home_frame, home_fragment).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_4:
                        item.setChecked(true);
                        alarm_fragment = new Alarm_Fragment();
                        try {
                            if (check.equals("1")) {
                                Log.d("tag", "확인 성공");
                                Bundle bundle = new Bundle();
                                bundle.putInt("check", 1);
                                alarm_fragment.setArguments(bundle);
                            }
                        } catch (Exception e) {

                        }
                        transaction.replace(R.id.home_frame, alarm_fragment).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_5:
                        item.setChecked(true);
                        transaction.replace(R.id.home_frame, myPageFragment).commitAllowingStateLoss();
                        break;
                }
                return false;
            }
        });
    }

    public void getUser(String userId)
    {
        Amplify.API.query(
                ModelQuery.list(User.class, User.ID.contains(userId)),
                response ->
                {
                    for(User user : response.getData())
                    {
                        username = user.getName();
                    }
                },
                error ->
                {

                }

        );
    }





}
