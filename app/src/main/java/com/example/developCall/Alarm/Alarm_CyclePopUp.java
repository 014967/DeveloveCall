package com.example.developCall.Alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.developCall.R;

public class Alarm_CyclePopUp extends Activity {

    Button[] alarmcycle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alarm_cyclepopup);

        alarmcycle = new Button[8];
        int[] alarmcycleid = {R.id.alarmcycle1, R.id.alarmcycle2, R.id.alarmcycle3, R.id.alarmcycle4, R.id.alarmcycle5, R.id.alarmcycle6, R.id.alarmcycle7, R.id.alarmcycle8};

        for(int i = 0; i < 8; i++){
            this.alarmcycle[i] = (Button)findViewById(alarmcycleid[i]);
            this.alarmcycle[i].setTag(i);
        }

        for(int i = 0; i < 8; i++){
            this.alarmcycle[i].setOnClickListener(btnListener);
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
            Button cyclebuttontemp = (Button) findViewById(v.getId());
            String alarmcycleresult = cyclebuttontemp.getTag().toString();
            String alarmcyclestr = cyclebuttontemp.getText().toString();
            intent.putExtra("cycleresult", alarmcycleresult);
            intent.putExtra("cyclestr", alarmcyclestr);
            setResult(2, intent);

            finish();
        }
    };

}

