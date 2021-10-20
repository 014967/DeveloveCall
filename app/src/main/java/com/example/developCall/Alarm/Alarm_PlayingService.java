package com.example.developCall.Alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.developCall.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Alarm_PlayingService extends Service {

    ArrayList<Alarm_ListData> alarm_listData;
    String getName, getMsg, getState;
    PendingIntent pendingIntent;
    Intent alarm_serviceIntent;
    View view;
    String[] dateName, dateContent;
    int[] dateProfile;
    long[] dateInt;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

        String CHANNEL_ID = "channel_1";
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Alarm Service", NotificationManager.IMPORTANCE_DEFAULT);

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("")
                .setContentText("")
                .build();
        startForeground(2, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.alarm_layout, null);

        alarm_listData = new ArrayList<Alarm_ListData>();
        Alarm_ListData listData = new Alarm_ListData();

        dateName = new String[100];
        dateContent = new String[100];
        dateInt = new long[100];
        dateProfile = new int[100];

        for(int i = 0; i < 100; i++){
            dateName[i] = "";
            dateContent[i] = "0";
            dateInt[i] = 0;
            dateProfile[i] = 0;
        }

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String nowDate = dateFormat.format(date);
        Log.d("tag",nowDate);
        long nowDateInt = Long.parseLong(nowDate);

        long least = 2100000000;
        long defer = 0;
        long deferabs = 0;
        int result = 2100000000;

        String alarmTempName = alarmGetJsonString("alarmFileName");
        alarmJsonParsing(alarmTempName);
        String alarmTempFileName = alarmGetJsonString("alarmTempFileName");
        alarmAddJsonParsing(alarmTempFileName);


        for(int i = 0; i < dateContent.length; i++){
            dateInt[i] = Long.parseLong(dateContent[i]);
            defer = dateInt[i] - nowDateInt;
            deferabs = Math.abs(defer);
            if(deferabs < least){
                least = defer;
                result = i;
            }
        }

        alarm_listData = AddData(alarm_listData, listData, dateName[result], dateContent[result], dateProfile[result]);
        try {
            writeFile("alarmFileName", alarm_listData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        alarm_serviceIntent = new Intent(this, Alarm_AddList.class);
        alarm_serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        alarm_serviceIntent.putExtra("tag","1");
        alarm_serviceIntent.putExtra("target",dateContent[result]);
        getApplicationContext().sendBroadcast(alarm_serviceIntent);
        pendingIntent = PendingIntent.getActivity(this, 0, alarm_serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        getMsg = "";

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "default";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("알람시작")
                    .setContentText(dateContent[result])
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            notificationManager.notify(1, builder.build());
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

    }

    public ArrayList<Alarm_ListData> AddData(ArrayList<Alarm_ListData> alarm_listData, Alarm_ListData listData, String Name, String Content, int Profile){
        listData.setProfile(Profile);
        listData.setName(Name);
        listData.setContent(Content);

        alarm_listData.add(0, listData);

        return alarm_listData;
    }

    public void writeFile(String fileName, ArrayList<Alarm_ListData> dataList) throws IOException {
        JSONObject obj = new JSONObject();
        try {
            JSONArray jArray = new JSONArray();//배열이 필요할때
            for (int i = 0; i < dataList.size(); i++)//배열
            {
                JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
                sObject.put("profile", dataList.get(i).getProfile());
                sObject.put("name", dataList.get(i).getName());
                sObject.put("content", dataList.get(i).getContent());
                jArray.put(sObject);
            }
            obj.put("Alarm", jArray);//배열을 넣음
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String JsonData = obj.toString();

        OutputStreamWriter calendarWriter = new OutputStreamWriter(view.getContext().openFileOutput(fileName, Context.MODE_PRIVATE));
        calendarWriter.write(JsonData);
        calendarWriter.close();
    }

    private void alarmJsonParsing(String json)
    {
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray dataArray = jsonObject.getJSONArray("Alarm");

            try {
                alarm_listData.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }

            for(int i=0; i<dataArray.length(); i++)
            {
                JSONObject dataObject = dataArray.getJSONObject(i);

                Alarm_ListData data = new Alarm_ListData();

                data.setProfile(dataObject.getInt("profile"));
                data.setName(dataObject.getString("name"));
                data.setContent(dataObject.getString("content"));


                alarm_listData.add(data);

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String alarmGetJsonString(String fileName)
    {
        String json = "";
        try {
            InputStream calendarStream = view.getContext().openFileInput(fileName);
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

    private void alarmAddJsonParsing(String json)
    {
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray dataArray = jsonObject.getJSONArray("Alarm");

            for(int i=0; i<dataArray.length(); i++)
            {
                JSONObject dataObject = dataArray.getJSONObject(i);

                Alarm_ListData data = new Alarm_ListData();

                dateProfile[i] = dataObject.getInt("profile");
                dateName[i] = dataObject.getString("name");
                dateContent[i] = dataObject.getString("content");

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
