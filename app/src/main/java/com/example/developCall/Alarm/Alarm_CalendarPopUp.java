package com.example.developCall.Alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.developCall.R;

public class Alarm_CalendarPopUp extends Activity {

    Button[] alarmcalendar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alarm_calendarpopup);

        alarmcalendar = new Button[7];
        int[] alarmcalendarid = {R.id.alarmcalendar1, R.id.alarmcalendar2, R.id.alarmcalendar3, R.id.alarmcalendar4, R.id.alarmcalendar5, R.id.alarmcalendar6, R.id.alarmcalendar7};

        for(int i = 0; i < 7; i++){
            this.alarmcalendar[i] = (Button)findViewById(alarmcalendarid[i]);
            this.alarmcalendar[i].setTag(i);
        }

        for(int i = 0; i < 7; i++){
            this.alarmcalendar[i].setOnClickListener(btnListener);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            Intent intent = new Intent();
            setResult(4, intent);
            finish();
        }*/
        Intent intent = new Intent();
        setResult(4, intent);
        finish();
        return true;
    }

    public final View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            Button calendarbuttontemp = (Button) findViewById(v.getId());
            String alarmcalendarresult = calendarbuttontemp.getTag().toString();
            String alarmcalendarstr = calendarbuttontemp.getText().toString();
            intent.putExtra("calendarresult", alarmcalendarresult);
            intent.putExtra("calendarstr", alarmcalendarstr);
            setResult(3, intent);

            finish();
        }
    };
}
