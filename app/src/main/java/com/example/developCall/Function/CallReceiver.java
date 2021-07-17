package com.example.developCall.Function;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


//전화 중이면 전화를 받고 아니면 메세지
public class CallReceiver extends BroadcastReceiver {//현재 전화가 오는지 받는지 끊는지 기본인지

    public final static String MyAction = "hello.world.broadcastreceiverexam.ACTION_MY_BROADCAST";
    String phonestate;

    //@RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {


            TelecomManager telephonyManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);

            Bundle extras = intent.getExtras();
            if (extras != null) {

                String state = extras.getString(TelephonyManager.EXTRA_STATE); // 현재 폰 상태 가져옴

                if (state.equals(phonestate)) {
                    return;
                } else {
                    phonestate = state;
                }
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

                } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    Log.d("qqq", "통화종료 혹은 통화벨 종료");
                }

                Log.d("qqq", "phone state : " + state);

            }

        }

        if(MyAction.equals(intent.getAction())) {
            Toast.makeText(context, "연결", Toast.LENGTH_LONG).show();
        }

    }
}
