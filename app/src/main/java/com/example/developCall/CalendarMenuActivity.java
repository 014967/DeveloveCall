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

    boolean bl_alarm;

    String datetime;
    ArrayList<CalendarData> dataList;
    Boolean flag;
    String exTime;
    String filterDate;
    String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_menu);

        et_date = findViewById(R.id.et_date);
        tv_date = findViewById(R.id.tv_date);
        sw_alarm = findViewById(R.id.sw_alarm);
        btn_save = findViewById(R.id.btn_save);


        userName= getIntent().getStringExtra("username");


        Date d = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String time = simpleDateFormat.format(d);


        String time2 = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        datetime = time + " / " + time2;
        tv_date.setText(datetime);



        try{
            filterDate = getIntent().getStringExtra("filterDate");
            if(!filterDate.equals(null))
            {
                datetime = filterDate + " / " + time2;
                tv_date.setText(datetime);
            }


        }
        catch(NullPointerException e )
        {
            //e.printStackTrace();

        }



        sw_alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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
                if (et_date.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "제목을 입력해주세요", Toast.LENGTH_LONG).show();
                    return;
                } else {

                    String calJson;
                     flag= false;


                    String dummy[] = datetime.split(" / ");
                    exTime = dummy[1]; //'2021.9.14
                    String[] date = dummy[0].split("\\.");

                    if (date[1].length() == 1) {
                        date[1] = "0" + date[1];
                    }
                    if (date[2].length() == 1) {
                        date[2] = "0" + date[2];
                    }
                    String jsonTitle = date[0] + date[1] + date[2] + ".json";


                    calJson = getJsonString(jsonTitle);

                    if(flag ==true)
                    {
                        System.out.println("false");
                        finish();

                    }
                    else
                    {
                        jsonParsing(calJson);
                        CalendarData data = new CalendarData();
                        data.setTitle(et_date.getText().toString());
                        data.setName(userName);
                        data.setTime(exTime);
                        dataList.add(data);
                        try {
                            writeFile(jsonTitle, dataList);
                            finish();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }



                }



            }
        });


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

        //    Date d = (Date) data.getExtras().get("calendar");
        String time = (String) data.getExtras().get("time").toString();
        //  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        //String date = simpleDateFormat.format(d);

        String date = (String) data.getExtras().get("calendar");


        datetime = date + " / " + time;
        tv_date.setText(datetime);


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


    private String getJsonString(String fileName) {
        String json = "";

        try{
            InputStream calendarStream = getApplicationContext().openFileInput(fileName);
            int fileSize = calendarStream.available();

            byte[] buffer = new byte[fileSize];
            calendarStream.read(buffer);
            calendarStream.close();

            json = new String(buffer, "UTF-8");
        }

        catch(IOException e)
        {
            flag = true;

            dataList= new ArrayList<>();
            CalendarData data = new CalendarData();
            data.setTitle(et_date.getText().toString());
            data.setName(userName);
            data.setTime(exTime);
            dataList.add(data);
            try{
                writeFile(fileName,dataList);
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }


        }


        return json;
    }


    private void jsonParsing(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray dataArray = jsonObject.getJSONArray("Calendar");

            dataList=new ArrayList<>();
            dataList.clear();

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataObject = dataArray.getJSONObject(i);

                CalendarData data = new CalendarData();

                data.setTitle(dataObject.getString("title"));
                data.setName(dataObject.getString("name"));
                try {
                    data.setTime(dataObject.getString("time"));
                } catch (JSONException e) {
                    data.setTime("Every Time");
                }

                dataList.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}





