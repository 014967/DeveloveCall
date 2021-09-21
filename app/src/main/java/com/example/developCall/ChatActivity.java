package com.example.developCall;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    String st_date;

    ProgressBar loading;
    ImageView btn_back;
    ImageView btn_addMemo;

    AmazonTranscription amazonTranscription = new AmazonTranscription();
    Gson gson = new Gson();
    String[][] modifi;

    List<List<String>> list = new ArrayList<>();
    TextView nonechat;
    TextView username;
    TextView tv_date;


    Date date;
    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat newDateFormat;
    String chatDate;

    //pop
    AlertDialog dialog;
    AlertDialog.Builder dialogBuilder;
    TextView popup_tv_date;
    TextView popup_set_date;


    //
    DatePickerDialog datePickerDialog;

    Calendar myCalendar = Calendar.getInstance();


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

        btn_addMemo = findViewById(R.id.addMemo);
        nonechat = findViewById(R.id.noneText);
        btn_back = findViewById(R.id.btn_back);
        username = findViewById(R.id.username);
        tv_date = findViewById(R.id.tv_date);
        String st_name = getIntent().getStringExtra("name");

        username.setText(st_name);


        String url = getIntent().getStringExtra("url");
//developcall-transcribe-output.s3.ap-northeast-2.amazonaws.com/27181b7c-50cc-4cd7-a3f2-c36720eb5e42_f309adee-26e4-4707-a7e7-41588d552b4f_13092021210444.m4a.json

        String expression = "(developcall-transcribe-output.s3.ap-northeast-2.amazonaws.com/)(.*)(.m4a.json)";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            String[] temp = matcher.group(2).split("_");
            st_date = temp[2];
            simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
            newDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

            try {
                date = simpleDateFormat.parse(st_date);
                chatDate = newDateFormat.format(date);
                tv_date.setText(chatDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


        btn_addMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(getApplicationContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.option_menu, popup.getMenu());
                popup.show();

                popup.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        System.out.println(1);

                        //startActivity(intent);
                        Intent intent = new Intent(getApplicationContext(), CalendarMenuActivity.class);
                        startActivity(intent);

                        return false;
                    }
                });
                popup.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        System.out.println(2);
                        return false;
                    }
                });

            }
        });


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
                        //amazonTranscription = download();

                        if (amazonTranscription.getResults().getTranscripts().get(0).getTranscript().equals("")) {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loading.setVisibility(View.GONE);
                                    nonechat.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(), "통화내용이 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }, 0);
                            break;
                        } else {


                            if (amazonTranscription.getResults().getSpeaker_labels().getSpeakers() == 2) {
                                item1 = amazonTranscription.getResults().getSpeaker_labels().toString();
                                item2 = amazonTranscription.getResults().getItems().toString();


                                item1 = item1.replace("[", "").replace("]", "");
                                item2 = item2.replace("[", "").replace("]", "");


                                array1 = service.getTokenizedArray(item1);
                                array2 = service.getTokenizedArray(item2);

                                modifi = service.mergeArray(array1, array2);


                                list = Arrays.stream(modifi)
                                        .map(Arrays::asList)
                                        .collect(Collectors.toList());
                                Observable.just(list)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(addList());
                            } else {
                                // 화자가 1 일때
                                String[] dd = amazonTranscription.getResults().getTranscripts().get(0).getTranscript()
                                        .split("[^가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9|\\s]");
                                String[][] result = new String[dd.length][2];
                                for (int i = 0; i < dd.length; i++) {
                                    result[i][0] = "spk_0";
                                    result[i][1] = dd[i];
                                }

                                list = Arrays.stream(result).map(Arrays::asList)
                                        .collect(Collectors.toList());
                                Observable.just(list)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(addList());

                            }


                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }


            }
        });


    }

    private Observer<? super String> updateLabel(View popView) {
        return new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {
                popup_set_date = popView.findViewById(R.id.set_date);
                popup_set_date.setText(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }


        };
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