package com.example.developCall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.auth.AuthException;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.result.AuthSignInResult;
import com.amplifyframework.core.Amplify;

public class LoginActivity extends AppCompatActivity {



    Button btn_login;
    EditText user_Email;
    EditText user_Password;
    TextView txt_Join;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        btn_login = (Button)findViewById(R.id.btn_login);
        txt_Join = (TextView) findViewById(R.id.txt_join);

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
}