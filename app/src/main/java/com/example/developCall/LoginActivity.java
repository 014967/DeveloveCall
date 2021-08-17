package com.example.developCall;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.amplifyframework.auth.AuthException;
import com.amplifyframework.auth.result.AuthSignInResult;
import com.amplifyframework.core.Amplify;

public class LoginActivity extends AppCompatActivity {



    Button btn_login;
    EditText user_Email;
    EditText user_Password;
    TextView txt_Join;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {

            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.MANAGE_OWN_CALLS,




    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        btn_login = (Button)findViewById(R.id.btn_login);
        txt_Join = (TextView) findViewById(R.id.txt_join);



        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user_Email = (EditText)findViewById(R.id.txt_id);
                user_Password = (EditText)findViewById(R.id.PassWord);

                Amplify.Auth.signIn(
                        user_Email.getText().toString(),
                        user_Password.getText().toString(),
                        this::onLoginSuccess,
                        this::onLoginError
                );

            }

            private void onLoginSuccess(AuthSignInResult authSignInResult) {


                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

                startActivity(intent);
            }

            private void onLoginError(AuthException e )
            {
                runOnUiThread(()->
                {

                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();
                });


            }


        });

        txt_Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(),JoinActivity.class);
                    startActivity(intent);
                }

        });












    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }








}