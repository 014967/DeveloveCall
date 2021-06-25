package com.example.developCall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.auth.AuthException;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.auth.result.AuthSignUpResult;
import com.amplifyframework.core.Amplify;

public class JoinActivity extends AppCompatActivity {

    Button btn_Join;
    EditText sign_Email;
    EditText sign_Password;
    EditText sign_Name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);


        sign_Email = (EditText)findViewById(R.id.sign_Email);
        sign_Password = (EditText)findViewById(R.id.sign_PassWord);
        sign_Name = (EditText)findViewById(R.id.sign_Name);


        btn_Join = (Button)findViewById(R.id.btn_Join);
        btn_Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Amplify.Auth.signUp(
                        sign_Email.getText().toString(),
                        sign_Password.getText().toString(),
                        AuthSignUpOptions.builder().userAttribute(

                                AuthUserAttributeKey.email(), sign_Email.getText().toString()

                        )
                                .userAttribute(AuthUserAttributeKey.name(), sign_Name.getText().toString()

                        ).build(),
                        this::onJoinSuccess,
                        this::onJoinError 

                );
            }

            private void onJoinError(AuthException e) {


                runOnUiThread(()->
                {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                });
            }


            private void onJoinSuccess(AuthSignUpResult authSignUpResult) {
                Intent intent = new Intent(getApplicationContext(), EmailConfirmationActivity.class);
                intent.putExtra("email",sign_Email.getText().toString());
                intent.putExtra("password",sign_Password.getText().toString());
                intent.putExtra("name",sign_Name.getText().toString());
                startActivity(intent);
            }
        });


    }


}