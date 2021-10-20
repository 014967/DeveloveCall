package com.example.developCall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.datastore.DataStoreException;
import com.amplifyframework.datastore.DataStoreItemChange;
import com.amplifyframework.datastore.generated.model.Chat;
import com.example.developCall.Service.serviceImpl;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

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

    com.example.developCall.Service.service service = new serviceImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_menu);

        btn_save = findViewById(R.id.btn_save);

        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);


        Intent intent = getIntent();
        username = (String) intent.getExtras().get("username");
        userId = (String) intent.getExtras().get("userId");
        date = (String) intent.getExtras().get("date");
        chatId = (String) intent.getExtras().get("chatId");
        friendId = (String) intent.getExtras().get("friendId");


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                title = et_title.getText().toString();
                content = et_content.getText().toString();

                if (title.equals("") || content.equals("")) {
                    Toast.makeText(getApplicationContext(), "입력칸을 모두 채워주세요", Toast.LENGTH_SHORT).show();
                } else {





                    Amplify.API.query(
                            ModelQuery.list(Chat.class, Chat.ID.contains(chatId)),
                            response ->
                            {

                                String st_memo = title + chatId + content;
                                Chat c = response.getData().getItems().iterator().next().copyOfBuilder().memo(st_memo).build();
                                Log.d("c", c.toString());


                                service.putMemo(c, st_memo)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(data ->
                                                {
                                                    if (data.type().name().equals("UPDATE")) {

                                                        finish();

                                                    }

                                                }
                                                , err ->
                                                {

                                                    runOnUiThread(() ->
                                                    {
                                                        System.out.println(err.toString());
                                                        Toast.makeText(getApplicationContext(), "DataStoreError", Toast.LENGTH_LONG).show();
                                                    });
                                                });

//                                Amplify.DataStore.save(c,
//                                        this::onSavedSucess,
//                                        this::onError);


                                /// lambda로 생성하는 chat와 detail chat의 lastchangedat의 형식이 맞지않음


                            }, error ->
                            {

                            }
                    );


                }


            }


            private void onError(DataStoreException e) {

                e.printStackTrace();

            }

            private <T extends Model> void onSavedSucess(DataStoreItemChange<T> tDataStoreItemChange) {

                if (tDataStoreItemChange.type().name().equals("CREATE")) {
                    finish();
                }


            }
        });
    }
}