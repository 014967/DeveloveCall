package com.example.developCall;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MemoActivity extends Activity {


    String title;
    String content;


    TextView tv_title;
    TextView tv_content;


    Button btn_return;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");

        tv_title = findViewById(R.id.tv_title);
        tv_content = findViewById(R.id.tv_content);
        btn_return = findViewById(R.id.btn_return);


        tv_title.setText(title);
        tv_content.setText(content);


        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });





    }
}