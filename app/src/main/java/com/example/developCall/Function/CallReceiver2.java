package com.example.developCall.Function;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.work.Data;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.User;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

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
                String callNumber = extras.getString("incoming_number"); // 폰반호~


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
                    //여서 녹음함
                    Log.d("start ", "시작");


                } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    Log.d("qqq", "통화종료 혹은 통화벨 종료");


                    //
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


    public static void getFriendId(String userId, String callNumber) {

        Amplify.API.query(
                ModelQuery.list(User.class, User.ID.contains(userId)),
                response ->
                {
                    for (User user : response.getData()) {
//                        for (Friend friend : user.getFriend()) {
//                            try {
//                                if (friend.getNumber().equals(callNumber)) {
//
//
//                                    Observable.just(friend.getId())
//                                            .observeOn(AndroidSchedulers.mainThread())
//                                            .subscribe(getObserver());
//
//
//                                } else {
//
//                                }
//
//                            } catch (Exception e) {
//                                Log.d("error : ", e.toString());
//                            }
//
//
//                        }
                    }

                },
                error ->
                {
                    Log.d("error : ", error.toString());
                }
        );
    }


    public static Observer<? super String> getObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {

                friendId = s;
                uploadWithTransferUtility(friendId);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }

            @Override
            public void onComplete() {


            }
        };


    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void uploadWithTransferUtility(String friendId) {


        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(context)
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance()))
                        .build();


        File directory = new File(Environment.getStorageDirectory().getAbsolutePath() + "/emulated/0/Call");
        File[] files = directory.listFiles();
        sortFileList(files);


        File file = files[files.length - 1];
        String[] format = file.getName().split("\\.");
        String userId = Amplify.Auth.getCurrentUser().getUserId();

        TransferObserver uploadObserver =
                transferUtility.upload(
                        "public/" + userId + "_" + friendId + "_" + uploadDate + "." + format[1],
                        file);


        // Attach a listener to the observer to get state update and progress notifications
        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;

                Log.d(TAG, "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
            }

        });

        // If you prefer to poll for the data, instead of attaching a
        // listener, check for the state and progress in the observer.
        if (TransferState.COMPLETED == uploadObserver.getState()) {
            // Handle a completed upload.
        }

        Log.d(TAG, "Bytes Transferred: " + uploadObserver.getBytesTransferred());
        Log.d(TAG, "Bytes Total: " + uploadObserver.getBytesTotal());
    }


    public static void sortFileList(File[] files) {

        Arrays.sort(files,
                new Comparator<Object>() {
                    @Override
                    public int compare(Object object1, Object object2) {

                        String s1 = "";
                        String s2 = "";


                        s1 = ((File) object1).lastModified() + "";
                        s2 = ((File) object2).lastModified() + "";


                        return s1.compareTo(s2);

                    }
                });


    }
}

