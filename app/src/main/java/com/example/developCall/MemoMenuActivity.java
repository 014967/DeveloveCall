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
import com.amplifyframework.datastore.generated.model.User;

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



                Amplify.API.query(
                        ModelQuery.list(User.class, User.ID.contains(userId)),
                        response ->
                        {
                            for (User user : response.getData()) {
                                for (Chat chat : user.getChat()) {
                                    if (chat.getId().equals(chatId)) {
                                        Chat newChat = chat.copyOfBuilder().memo(title + chatId + content).build();
                                        Amplify.DataStore.save(newChat
                                                , this::onSavedSucess,
                                                this::onError);

                                    }
                                }
                            }
                        }, error ->
                        {

                        }
                );



            }

            private void onError(DataStoreException e) {
                System.out.println(e);
            }

            private <T extends Model> void onSavedSucess(DataStoreItemChange<T> tDataStoreItemChange) {

                System.out.println(tDataStoreItemChange.item().toString());
                System.out.println(tDataStoreItemChange.uuid().toString());
                onBackPressed();
            }
        });
    }
}