package com.example.developCall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.datastore.DataStoreException;
import com.amplifyframework.datastore.DataStoreItemChange;
import com.amplifyframework.datastore.generated.model.Chat;

public class MemoMenuActivity extends Activity {


    EditText et_title;
    EditText et_content;

    String title;
    String content;

    Button btn_save;
    Button btn_return;

    String username;
    String userId;
    String date;
    String chatId;
    String friendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_menu);

        btn_save = findViewById(R.id.btn_save);
        btn_return = findViewById(R.id.btn_return);
        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);


        Intent intent = getIntent();
        username = (String) intent.getExtras().get("username");
        userId = (String) intent.getExtras().get("userId");
        date = (String) intent.getExtras().get("date");
        chatId = (String) intent.getExtras().get("chatId");
        friendId = (String) intent.getExtras().get("friendId");


        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                title = et_title.getText().toString();
                content = et_content.getText().toString();

                /*Amplify.DataStore.query(Chat.class, Where.id(chatId),
                        matches -> {
                            if (matches.hasNext()) {
                                Chat original = matches.next();
                                Chat edited = original.copyOfBuilder()
                                        .memo(title + chatId + content)
                                        .build();
                                Amplify.DataStore.save(edited,
                                        updated -> Log.i("MyAmplifyApp", "Updated a post."),
                                        failure -> Log.e("MyAmplifyApp", "Update failed.", failure)
                                );
                            }
                        },
                        failure -> Log.e("MyAmplifyApp", "Query failed.", failure)
                );
*/
                Amplify.API.query(
                        ModelQuery.list(Chat.class, Chat.ID.contains(chatId)),
                        response ->
                        {
                            Chat original = response.getData().iterator().next();
                            Chat newChat = original.copyOfBuilder()
                                    .memo(title + chatId + content).build();
                            Amplify.DataStore.save(newChat
                            ,this::onSavedSucess
                            ,this::onError);
                        }, error ->
                        {

                        }
                );

            }

            private void onError(DataStoreException e) {
            }

            private <T extends Model> void onSavedSucess(DataStoreItemChange<T> tDataStoreItemChange) {

            }
        });
    }
}