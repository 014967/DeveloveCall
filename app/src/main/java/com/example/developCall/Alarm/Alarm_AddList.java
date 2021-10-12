package com.example.developCall.Alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.developCall.HomeActivity;
import com.example.developCall.R;

public class Alarm_AddList extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_addlist);

        Intent intent = getIntent();
        String tag = "0";
        String getContent;
        tag = intent.getStringExtra("tag");
        getContent = intent.getStringExtra("target");
        Intent openMain = new Intent(this, HomeActivity.class);
        if(tag.equals("1")){
            openMain.putExtra("check", "1");
            openMain.putExtra("target", getContent);
        }
        startActivity(openMain);
    }
}
