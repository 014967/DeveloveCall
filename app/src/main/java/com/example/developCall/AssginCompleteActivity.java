package com.example.developCall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AssginCompleteActivity extends AppCompatActivity {

    Button btn_Join;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assign_complete);

        btn_Join = findViewById(R.id.btn_Join);


        btn_Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(in);
            }
        });
    }
}