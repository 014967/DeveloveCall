package com.example.developCall.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.developCall.FriendPopUp;
import com.example.developCall.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ListPopUpActivity extends Activity {

    EditText title;
    TextView name;
    Button rewrite, delete, close;
    String listtitle, listname;
    int listposition;
    TimePicker timePicker;
    String hourmin;
    int hour, min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.listview_popup);

        Intent intent = getIntent();
        listtitle = intent.getStringExtra("title");
        listname = intent.getStringExtra("name");
        listposition = intent.getIntExtra("position",0);

        title = (EditText)findViewById(R.id.listview_title);
        name = (TextView) findViewById(R.id.listview_content);
        rewrite = (Button)findViewById(R.id.rewrite);
        delete = (Button)findViewById(R.id.delete);
        close = (Button)findViewById(R.id.close);
        timePicker = (TimePicker)findViewById(R.id.reTimePicker);

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat hourFormat = new SimpleDateFormat("hh");
        SimpleDateFormat minFormat = new SimpleDateFormat("mm");
        String tempHour = hourFormat.format(date);
        String tempMin = minFormat.format(date);

        hourmin = tempHour + ":" + tempMin;

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                hour = i;
                min = i1;
                hourmin = hour + ":" + min;
            }
        });

        String test = String.valueOf(listposition);
        Log.d("tag",test);

        title.setText(listtitle);
        name.setText(listname);


        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FriendPopUp.class);
                startActivityForResult(intent,1);
            }
        });

        rewrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtitle = title.getText().toString();
                listname = name.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("title", listtitle);
                intent.putExtra("name", listname);
                intent.putExtra("time", hourmin);
                intent.putExtra("position", listposition);
                setResult(1, intent);

                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(2, new Intent().putExtra("position", listposition));
                finish();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode != Activity.RESULT_OK) {
            return;
        }


        String friendName = (String) data.getExtras().get("friendName").toString();
        name.setText(friendName);
        System.out.println(friendName);






    }

}