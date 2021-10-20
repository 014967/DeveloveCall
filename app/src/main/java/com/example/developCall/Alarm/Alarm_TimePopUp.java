package com.example.developCall.Alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.developCall.R;

public class Alarm_TimePopUp extends Activity {

    Button[] alarmtime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alarm_timepopup);

        alarmtime = new Button[24];
        int[] alarmtimeid = {R.id.alarmtime1, R.id.alarmtime2, R.id.alarmtime3, R.id.alarmtime4, R.id.alarmtime5, R.id.alarmtime6, R.id.alarmtime7, R.id.alarmtime8, R.id.alarmtime9, R.id.alarmtime10, R.id.alarmtime11, R.id.alarmtime12, R.id.alarmtime13, R.id.alarmtime14, R.id.alarmtime15, R.id.alarmtime16, R.id.alarmtime17, R.id.alarmtime18, R.id.alarmtime19, R.id.alarmtime20, R.id.alarmtime21, R.id.alarmtime22, R.id.alarmtime23, R.id.alarmtime24};

        for(int i = 0; i < 24; i++){
            this.alarmtime[i] = (Button)findViewById(alarmtimeid[i]);
            this.alarmtime[i].setTag(i);
        }

        for(int i = 0; i < 24; i++){
            this.alarmtime[i].setOnClickListener(btnListener);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Intent intent = new Intent();
        setResult(4, intent);
        finish();
        return true;
    }

    public final View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            Button timebuttontemp = (Button) findViewById(v.getId());
            String alarmtimeresult = timebuttontemp.getTag().toString();
            intent.putExtra("timeresult", alarmtimeresult);
            setResult(1, intent);

            finish();
        }
    };

}

