package com.example.developCall;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.example.developCall.Alarm.Alarm_Fragment;
import com.example.developCall.Fragment.Fragment1;
import com.example.developCall.Fragment.Fragment2;
import com.example.developCall.Fragment.FriendListFragment;
import com.example.developCall.Fragment.GroupListFragment;
import com.example.developCall.Fragment.MainFragment;
import com.example.developCall.Function.CallReceiver2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

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




    //fcm
    public static final String TAG = HomeActivity.class.getSimpleName();
    private static PinpointManager pinpointManager;

    public static PinpointManager getPinpointManager(final Context applicationContext) {
        if (pinpointManager == null) {
            final AWSConfiguration awsConfig = new AWSConfiguration(applicationContext);
            AWSMobileClient.getInstance().initialize(applicationContext, awsConfig, new Callback<UserStateDetails>() {
                @Override
                public void onResult(UserStateDetails userStateDetails) {
                    Log.i("INIT", String.valueOf(userStateDetails.getUserState()));
                }

                @Override
                public void onError(Exception e) {
                    Log.e("INIT", "Initialization error.", e);
                }
            });

            PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                    applicationContext,
                    AWSMobileClient.getInstance(),
                    awsConfig);

            pinpointManager = new PinpointManager(pinpointConfig);

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                return;
                            }
                            final String token = task.getResult();
                            Log.d(TAG, "Registering push notifications token: " + token);
                            pinpointManager.getNotificationClient().registerDeviceToken(token);
                        }
                    });
        }
        return pinpointManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        mReceiveer = new CallReceiver2();

        //fcm
        getPinpointManager(getApplicationContext());
        //

        fragmentManager = getSupportFragmentManager();

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        mainFragment = new MainFragment();
        alarm_fragment = new Alarm_Fragment();
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
                        transaction.replace(R.id.home_frame, fragment1).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_2:
                        transaction.replace(R.id.home_frame, groupListFragment).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_3:
                        transaction.replace(R.id.home_frame, mainFragment).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_4:
                        transaction.replace(R.id.home_frame, alarm_fragment).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_5:
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
