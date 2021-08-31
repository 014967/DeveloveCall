package com.example.developCall.Function;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

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

public class S3uploadService extends Service {


    File file;
    Uri uri;
    String[] format;
    Date date = new Date();
    String friendId;
    SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
    String uploadDate = formatter.format(date);
    String userId;


    int NOTIFICATION_ID = 10;
    String CHANNEL_ID = "primary_notification_channel";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("MyService is running")
                    .setContentText("MyService is running")
                    .build();
            Log.d("Test", "start foreground");
            startForeground(NOTIFICATION_ID, notification);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {

        NotificationChannel notificationChannel = new  NotificationChannel(
                CHANNEL_ID,
                "MyApp notification",
                NotificationManager.IMPORTANCE_HIGH
        );
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(
                notificationChannel);
    }



    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        userId = intent.getExtras().get("userId").toString();
        String callNumber = intent.getExtras().get("callNumber").toString();

        File directory = new File(Environment.getStorageDirectory().getAbsolutePath() + "/emulated/0/Call");
        File[] files = directory.listFiles();
        sortFileList(files);

        file = files[files.length - 1];
        uri = Uri.fromFile(file);
        format = file.getName().split("\\.");


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


        return super.onStartCommand(intent, flags, startId);
    }


    private void s3UPload(String s) {
        String uploadFilename = userId + "_" + s + "_" + uploadDate + "." + format[1];
        try {
            //background 안

            TransferUtility transferUtility =
                    TransferUtility.builder()
                            .context(this)
                            .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                            .s3Client(new AmazonS3Client(AWSMobileClient.getInstance()))
                            .build();


            TransferObserver uploadObserver =
                    transferUtility.upload(
                            "public/" + uploadFilename,
                            file);


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

                    Log.d("S3UPLOADSERVICE", "ID:" + id + " bytesCurrent: " + bytesCurrent
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
                stopSelf();
                NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(10);
            }

            Log.d("S3UPLOADSERVICE", "Bytes Transferred: " + uploadObserver.getBytesTransferred());
            Log.d("S3UPLOADSERVICE", "Bytes Total: " + uploadObserver.getBytesTotal());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Observer<? super String> getObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {

                friendId = s;
                s3UPload(friendId);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }

            @Override
            public void onComplete() {


            }
        };
    }


    public File[] sortFileList(File[] files) {

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

        return files;
    }

}
