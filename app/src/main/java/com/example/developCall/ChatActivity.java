package com.example.developCall;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.developCall.Adapter.ChatRecyclerAdapter;
import com.example.developCall.Object.AmazonTranscription;
import com.example.developCall.Service.serviceImpl;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import jodd.http.HttpResponse;

public class ChatActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ChatRecyclerAdapter chatRecyclerAdapter;

    serviceImpl service = new serviceImpl();

    String[][] chatArray;
    //merge array
    String item1;
    String item2;
    String[] dummy1;
    String[][] array1;
    String[] dummy2;
    String[][] array2;


    ProgressBar loading;
    ImageView btn_back;

    AmazonTranscription amazonTranscription = new AmazonTranscription();
    Gson gson = new Gson();
    String[][] modifi;

    List<List<String>> list = new ArrayList<>();
    TextView nonechat;
    TextView username;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        loading = findViewById(R.id.loading);

        recyclerView = findViewById(R.id.recylcerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerAdapter = new ChatRecyclerAdapter(this, list);
        recyclerView.setAdapter(chatRecyclerAdapter);

        nonechat = findViewById(R.id.noneText);
        btn_back = findViewById(R.id.btn_back);
        username= findViewById(R.id.username);

        String st_name = getIntent().getStringExtra("name");

        username.setText(st_name);




        String url = getIntent().getStringExtra("url");


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    try {
                        amazonTranscription = download(url);

                        if (amazonTranscription.getResults().getTranscripts().get(0).getTranscript().equals("")) {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run()
                                {
                                    loading.setVisibility(View.GONE);
                                    nonechat.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(), "통화내용이 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }, 0);
                            break;
                        } else {
                            item1 = amazonTranscription.getResults().getSpeaker_labels().toString();
                            item2 = amazonTranscription.getResults().getItems().toString();


                            item1 = item1.replace("[", "").replace("]", "");
                            item2 = item2.replace("[", "").replace("]", "");


                            dummy1 = item1.split(",");
                            array1 = new String[dummy1.length][];
                            int r = 0;
                            for (String row : dummy1) {
                                array1[r++] = row.split("/", 5);

                            }

                            Log.d("item2", item2);
                            dummy2 = item2.split(",");
                            array2 = new String[dummy2.length][];
                            int k = 0;
                            for (String row1 : dummy2) {
                                array2[k++] = row1.split("/", 5);

                            }

                            modifi = service.getModifiedChatArray(array1, array2);
                            list = Arrays.stream(modifi)
                                    .map(Arrays::asList)
                                    .collect(Collectors.toList());
                            Observable.just(list)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(addList());


                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }


            }
        });


    }

    private Observer<? super List<List<String>>> addList() {
        return new Observer<List<List<String>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull List<List<String>> lists) {
                chatRecyclerAdapter.initData(lists);
                chatRecyclerAdapter.notifyDataSetChanged();
                loading.setVisibility(View.GONE);

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }


    private AmazonTranscription download(String uri) throws IOException {


        jodd.http.HttpRequest httpRequest = jodd.http.HttpRequest.get(uri);
        HttpResponse response = httpRequest.get(uri).send();

        String result = response.charset("UTF-8").bodyText();
        Log.d("텍스트", result);


        return gson.fromJson(result, AmazonTranscription.class);
    }


}