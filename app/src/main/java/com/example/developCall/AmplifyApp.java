package com.example.developCall;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.AmplifyModelProvider;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

public class AmplifyApp extends Application {
    @Override
    public void onCreate() {

        super.onCreate();
        try{
            AmplifyModelProvider modelProvider = AmplifyModelProvider.getInstance();
            Amplify.addPlugin(new AWSDataStorePlugin(modelProvider));
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin((new AWSS3StoragePlugin()));
            Amplify.configure(getApplicationContext());

            Log.i("amplify", "configure []");

        }
        catch (AmplifyException e )
        {
            e.printStackTrace();
        }

    }
}
