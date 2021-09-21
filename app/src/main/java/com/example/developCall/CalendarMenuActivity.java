package com.example.developCall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.developCall.Calendar.CalendarData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;




public class CalendarMenuActivity extends Activity {

    EditText et_date;
    TextView tv_date;
    Switch sw_alarm;
    Calendar calendar = Calendar.getInstance();

    Button btn_save;
    Button btn_return;
    boolean bl_alarm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_menu);

        et_date = findViewById(R.id.et_date);
        tv_date = findViewById(R.id.tv_date);
        sw_alarm = findViewById(R.id.sw_alarm);
        btn_save = findViewById(R.id.btn_save);
        btn_return = findViewById(R.id.btn_return);


        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sw_alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    // Turn the system on.
                    bl_alarm = isChecked;
                } else {
                    // Turn the system off.
                    bl_alarm = isChecked;
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_date.getText().toString() == "")
                {
                    Toast.makeText(getApplicationContext(), "제목을 입력해주세요", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    String st_title = et_date.getText().toString();
                    String st_date =  tv_date.getText().toString();
                    //System.out.println(bl_alarm);
                }


            }
        });
        Date d = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String time = simpleDateFormat.format(d);
        tv_date.setText(time);


        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), CalendarPopUpActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        Date d = (Date) data.getExtras().get("calendar");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = simpleDateFormat.format(d);

        tv_date.setText(date);


    }


    public void writeFile(String fileName, ArrayList<CalendarData> dataList) throws IOException {
        JSONObject obj = new JSONObject();
        try {
            JSONArray jArray = new JSONArray();//배열이 필요할때
            for (int i = 0; i < dataList.size(); i++)//배열
            {
                JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
                sObject.put("title", dataList.get(i).getTitle());
                sObject.put("name", dataList.get(i).getName());
                sObject.put("time", dataList.get(i).getTime());
                jArray.put(sObject);
            }
            obj.put("Calendar", jArray);//배열을 넣음
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String JsonData = obj.toString();

        OutputStreamWriter calendarWriter = new OutputStreamWriter(getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE));
        calendarWriter.write(JsonData);
        calendarWriter.close();
    }


    private String getJsonString(String fileName)
    {
        String json = "";
        try {
            InputStream calendarStream = getApplicationContext().openFileInput(fileName);
            int fileSize = calendarStream.available();

            byte[] buffer = new byte[fileSize];
            calendarStream.read(buffer);
            calendarStream.close();

            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return json;
    }




}





