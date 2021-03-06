package com.example.developCall;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.amplifyframework.auth.AuthException;
import com.amplifyframework.auth.result.AuthSignInResult;
import com.amplifyframework.core.Amplify;

public class LoginActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 22;
    private boolean is_Permission = false;
    private static final String TAG = "LoginActivity";

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
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.CALL_PHONE,


    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        btn_login = (Button) findViewById(R.id.btn_login);
        txt_Join = (TextView) findViewById(R.id.txt_join);

        is_Permission = chkPermission();






        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user_Email = (EditText) findViewById(R.id.txt_id);
                user_Password = (EditText) findViewById(R.id.PassWord);

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


            private void onLoginError(AuthException e) {
                e.printStackTrace();

                runOnUiThread(() ->
                {

                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                });


            }


        });

        txt_Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AssignActivity.class);
                startActivity(intent);
            }

        });


    }

    /*public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {

                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            } else {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PRECISE_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }

        }
        return true;
    }*/


    public boolean chkPermission() {
        // ?????? ????????? ?????? ??????????????? ??????
        boolean mPermissionsGranted = false;
        String[] mRequiredPermissions = {
                Manifest.permission.READ_PHONE_NUMBERS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_CALL_LOG,

                Manifest.permission.MANAGE_OWN_CALLS,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.CALL_PHONE,


        };
        // ?????? ?????? ?????? ?????? ??????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //mRequiredPermissions[0] = Manifest.permission.READ_PHONE_NUMBERS;
            mRequiredPermissions[0] = Manifest.permission.READ_PHONE_STATE;
        } else {
            mRequiredPermissions[0] = Manifest.permission.READ_PHONE_STATE;
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // ?????? ????????? ????????? ????????? ????????????.
            mPermissionsGranted = hasPermissions(mRequiredPermissions);

            // ?????? ?????? ?????? ??? ????????? ?????? ??????
            if (!mPermissionsGranted) {
                // ????????? ????????????.
                ActivityCompat.requestPermissions(this, mRequiredPermissions, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            mPermissionsGranted = true;
        }
        Log.d(TAG, "?????? ????????? ???" + mPermissionsGranted);

        return mPermissionsGranted;
    }


    public boolean hasPermissions(String[] permissions) {
        // ?????? ????????? ????????? ????????? ????????????.
        for (String permission : permissions) {
            if (checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            // ????????? ?????? ??????????????? ??????
            boolean chkFlag = false;
            // ????????? ????????? 0 ???, ?????? ?????? ????????? -1??? ????????? ?????????.
            for (int g : grantResults) {
                if (g == -1) {
                    chkFlag = true;
                    break;
                }
            }

            // ?????? ??? ??? ????????? ?????? ??? ??? ??????
            if (chkFlag) {
                chkPermission();
            }
        }
    }


}