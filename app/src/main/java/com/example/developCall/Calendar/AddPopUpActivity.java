package com.example.developCall.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class AddPopUpActivity extends Activity {


    EditText title;
    TextView name;
    TimePicker time;
    Button finish, addclose;
    String addname, addtitle;
    int hour, min;
    String hourmin;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_layout);

        title = (EditText)findViewById(R.id.title);

        name = (TextView) findViewById(R.id.content);
        finish = (Button)findViewById(R.id.finish);
        addclose = (Button)findViewById(R.id.addclose);
        time = (TimePicker)findViewById(R.id.addTimePicker);

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat hourFormat = new SimpleDateFormat("hh");
        SimpleDateFormat minFormat = new SimpleDateFormat("mm");
        String tempHour = hourFormat.format(date);
        String tempMin = minFormat.format(date);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FriendPopUp.class);
                startActivityForResult(intent,1);
            }
        });

        hourmin = tempHour + ":" + tempMin;

        time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                hour = i;
                min = i1;
                hourmin = hour + ":" + min;
            }
        });


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtitle = title.getText().toString();
                addname = name.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("title", addtitle);
                intent.putExtra("name", addname);
                intent.putExtra("time", hourmin);
                setResult(RESULT_OK, intent);

                finish();
            }
        });

        addclose.setOnClickListener(new View.OnClickListener() {
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