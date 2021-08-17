package com.example.developCall.Function;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Friend;
import com.amplifyframework.datastore.generated.model.User;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class S3uploadWork extends Worker {


    File file;
    Uri uri;
    String[] format;
    String userId;
    String callNumber;
    String friendId;

    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
    String uploadDate = formatter.format(date);

    Context appContext;

    public S3uploadWork(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {

        super(appContext, workerParams);
        this.appContext = appContext;
    }

    @NonNull
    @Override
    public Result doWork() {

        userId = Amplify.Auth.getCurrentUser().getUserId();

        File directory = new File(Environment.getStorageDirectory().getAbsolutePath() + "/emulated/0/Call");
        File[] files = directory.listFiles();
        sortFileList(files);


        file = files[files.length - 1];
        uri = Uri.fromFile(file);
        format = file.getName().split("\\.");

        callNumber= getInputData().getString("callNumber");
        Amplify.API.query(
                ModelQuery.list(User.class, User.ID.contains(userId)),
                response ->
                {
                    for (User user : response.getData()) {
                        for (Friend friend : user.getFriend()) {
                            try {
                                if (friend.getNumber().equals(callNumber)) {


                                    Observable.just(friend.getId())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(getObserver());


                                } else {

                                }

                            } catch (Exception e) {
                                Log.d("error : ", e.toString());
                            }


                        }
                    }

                },
                error ->
                {
                    Log.d("error : ", error.toString());
                }
        );


        return null;
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

    private void s3UPload(String s) {
        String uploadFilename = userId + "_" + s + "_" + uploadDate + "." + format[1];
        try {
            //background 안
            ContentResolver contentResolver;
            InputStream inputStream = appContext.getContentResolver().openInputStream(uri);
            Amplify.Storage.uploadInputStream(
                    uploadFilename, inputStream,
                    result ->
                    {
                        Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey());

                    },
                    storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)


            );
        } catch (Exception e) {
            e.printStackTrace();
        }
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
