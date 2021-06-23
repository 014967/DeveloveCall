package com.example.developCall;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.developCall.RecyclerView.ChatRecyclerAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Chat extends AppCompatActivity {


    RecyclerView recyclerView;
    ChatRecyclerAdapter chatRecyclerAdapter;


    String[][] chatArray;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().hide();


        Bundle b = getIntent().getExtras();
        chatArray = (String[][])b.getSerializable("chatArray");
// TODO: 6/23/21







        List<List<String>> list = Arrays.stream(chatArray)
                                    .map(Arrays::asList)
                                    .collect(Collectors.toList());



        recyclerView = findViewById(R.id.recylcerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerAdapter= new ChatRecyclerAdapter(this, list);
        recyclerView.setAdapter(chatRecyclerAdapter);









    }
}