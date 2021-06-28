package com.example.developCall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.auth.AuthException;
import com.amplifyframework.auth.result.AuthSignInResult;
import com.amplifyframework.auth.result.AuthSignUpResult;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.datastore.DataStoreException;
import com.amplifyframework.datastore.DataStoreItemChange;
import com.amplifyframework.datastore.generated.model.User;
import com.amplifyframework.datastore.generated.model.User.Builder;

public class EmailConfirmationActivity extends AppCompatActivity {


    EditText Confirmation_Code;
    Button btn_Confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_confirmation);


        Confirmation_Code = (EditText)findViewById(R.id.Confirmation_Code);
        btn_Confirm = (Button)findViewById(R.id.btn_Confirm);

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
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        });

    }

    private void onSuccess(AuthSignUpResult authSignUpResult)
    {
        reLogin();
    }

    private void reLogin()
    {
      String username = getEmail();
      String password = getPassWord();

      Amplify.Auth.signIn(
              username,
              password,
              this::onLoginSuccess,
              this::onError
      );
    }

    private void onLoginSuccess(AuthSignInResult authSignInResult) {
        String userId = Amplify.Auth.getCurrentUser().getUserId();

        String name= getName();

        Amplify.DataStore.save(
                User.builder().name(name).id(userId).build(),
                this::onSavedSuccess,
                this::onError
        );
    }

    private void onError(DataStoreException e) {
        runOnUiThread(()->
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private <T extends Model> void onSavedSuccess(DataStoreItemChange<T> tDataStoreItemChange)
    {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
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


}