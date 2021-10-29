package com.example.developCall.Function;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.work.Data;


import com.amplifyframework.core.Amplify;

import java.text.SimpleDateFormat;
import java.util.Date;




public class CallReceiver2 extends BroadcastReceiver {


    static String TAG = "CallReceiver";
    static String phonestate;
    static String friendId;
    static String friendNumber;
    static String userId;

    static Date date = new Date();
    static SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
    static String uploadDate = formatter.format(date);

    static Context context;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;


        if (Amplify.Auth.getCurrentUser()==null) {

        } else {


            userId = Amplify.Auth.getCurrentUser().getUserId();
            Bundle extras = intent.getExtras(); // intent에서 state와 incoming_number만 가져올 수 있음
            Log.d("extras : ", extras.toString());
            if (extras != null) {

                String state = extras.getString("state");// 현재 폰 상태 가져옴



                if (!intent.getExtras().containsKey(TelephonyManager.EXTRA_INCOMING_NUMBER)) {
                    Log.i("Call receiver", "skipping intent=" + intent + ", extras=" + intent.getExtras() + " - no number was supplied");
                    return;
                }

//                if (state.equals(phonestate)) {
//                    return;
//                } else {
//                    phonestate = state;
//                }

                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    String phoneNo = "111";
                    Log.d("qqq", phoneNo + "currentNumber");
                    Log.d("qqq", "통화벨 울리는중");


                    //telephonyManager.acceptRingingCall();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                    telephonyManager.endCall(); 전화 끊기, 거절 함수이다.
//                }


                } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    Log.d("qqq", "통화중");
                    //여서 녹음함
                    Log.d("start ", "시작");


                } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    Log.d("qqq", "통화종료 혹은 통화벨 종료");


                    //

                    String callNumber = extras.getString("incoming_number"); // 폰반호~


                    Data.Builder data = new Data.Builder();
                    data.putString("callNumber", callNumber);




                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(new Intent(context, S3uploadService.class)
                                .putExtra("userId", userId)
                                .putExtra("callNumber", callNumber)
                        );
                    } else {
                        context.startService(new Intent(context, S3uploadService.class)
                                .putExtra("userId", userId)
                                .putExtra("callNumber", callNumber)
                        );

                    }


                }

            }

        }


    }



}

