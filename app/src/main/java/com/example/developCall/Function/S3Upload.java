package com.example.developCall.Function;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.amplifyframework.core.Amplify;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;


public class S3Upload  {




    public static  void upload( String filename, InputStream inputStream) {

        try
        {
            Amplify.Storage.uploadInputStream(
                    filename,
                    inputStream,
                    result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                    storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)

            );
        }
        catch(Exception e )
        {
            Log.d("error " ,String.valueOf(e));
        }




    }
}
