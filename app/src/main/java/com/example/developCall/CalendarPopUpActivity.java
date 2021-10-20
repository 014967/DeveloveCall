package com.example.developCall;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Calendar;

public class CalendarPopUpActivity extends Activity {

    DatePicker datePicker;
    TimePicker timePicker;
    Calendar calendar= Calendar.getInstance();
    Button btn_save;
    Button btn_return;
    int hour;
    int minute;
    String hourMinute;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_calendar);

        datePicker = findViewById(R.id.datePicker);
        btn_save = findViewById(R.id.btn_save);
        btn_return = findViewById(R.id.btn_return);
        timePicker= findViewById(R.id.timePicker);



        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Log.d("Date", "Year=" + year + " Month=" + month + " day=" + dayOfMonth);
                calendar.set(year, month, dayOfMonth);
            }
        });

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        hourMinute = hour + ":" + minute;
        System.out.println(hourMinute);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int min) {
                hour = hourOfDay;
                minute = min;
                hourMinute = hour + ":"+ minute;
                System.out.println(hourMinute);
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int month = calendar.get(Calendar.MONTH)+1;
                String date = calendar.get(Calendar.YEAR) +"." + month + "." + calendar.get(Calendar.DAY_OF_MONTH);

                setResult(Activity.RESULT_OK,new Intent().putExtra("calendar", date).putExtra("time",hourMinute ));
                finish();
            }
        });
    }
}
