package com.example.developCall;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        AuthUser currentUser = Amplify.Auth.getCurrentUser();
        Intent intent;
        //Log.d("현재 유저 : " , currentUser.toString());
        if(currentUser == null )
        {
            //Go to the Login Page

             intent = new Intent(getApplicationContext(), LoginActivity.class);

        }
        else
        {
            //Go to the Main Page

             intent = new Intent(getApplicationContext(), HomeActivity.class);

        }

        startActivity(intent);
        finish();
    }
}