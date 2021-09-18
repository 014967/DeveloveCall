package com.example.developCall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.auth.AuthException;
import com.amplifyframework.auth.result.AuthSignInResult;
import com.amplifyframework.auth.result.AuthSignUpResult;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.datastore.DataStoreException;
import com.amplifyframework.datastore.DataStoreItemChange;
import com.amplifyframework.datastore.generated.model.User;

public class EmailConfirmationActivity extends AppCompatActivity {


    EditText Confirmation_Code;
    Button btn_Confirm;

    String username;
    String password;
    String name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_confirmation);



        Confirmation_Code = (EditText)findViewById(R.id.Confirmation_Code);
        btn_Confirm = (Button)findViewById(R.id.btn_Confirm);






        btn_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amplify.Auth.confirmSignUp(
                        getEmail(),
                        Confirmation_Code.getText().toString(),
                        this::onSuccess,
                        this::onError
                );
            }
            private void onError(AuthException e )
            {

                runOnUiThread(()->
                {
                    Toast.makeText(getApplicationContext(),"Email Auth error",Toast.LENGTH_LONG).show();
                });

            }
            private void onSuccess(AuthSignUpResult authSignUpResult)
            {
                reLogin();
            }

            private void reLogin()
            {
                username = getEmail();
                password = getPassWord();

                Amplify.Auth.signIn(
                        username,
                        password,
                        this::onLoginSuccess,
                        this::onError
                );
            }
            private void onLoginSuccess(AuthSignInResult authSignInResult) {

                if(!Amplify.Auth.getCurrentUser().getUserId().equals(""))
                {
                    String userId = Amplify.Auth.getCurrentUser().getUserId();  //name = null userId="asfasdf"

                    name = getName();





                    User user = User.builder().name(name).id(userId).owner(userId).userImg("").build();



                    Amplify.DataStore.save(
                            user,
                            this::onSavedSuccess,
                            this::onError
                    );



                }



            }

            private void onError(DataStoreException e) {
                runOnUiThread(()->
                        {
                            System.out.println(e.toString());
                            Toast.makeText(getApplicationContext(), "DataStoreError", Toast.LENGTH_LONG).show();
                        });


            }

            private <T extends Model> void onSavedSuccess(DataStoreItemChange<T> tDataStoreItemChange)
            {



                Intent intent = new Intent(getApplicationContext(),AssginCompleteActivity.class);

                startActivity(intent);
            }
            private String getEmail(){
                return getIntent().getStringExtra("email");
            }
            private String getPassWord(){
                return getIntent().getStringExtra("password");
            }
            private String getName(){
                return getIntent().getStringExtra("name");
            }
        });



    }




}