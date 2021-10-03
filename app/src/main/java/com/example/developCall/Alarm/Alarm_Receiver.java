package com.example.developCall.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ListView;

import java.util.ArrayList;

public class Alarm_Receiver extends BroadcastReceiver {

    ArrayList<Alarm_ListData> alarm_listData;
    ListView alarm_listView;
    Alarm_ListAdapter alarm_listAdapter;
    Alarm_ListData listData;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Intent service_Intent = new Intent(context, Alarm_PlayingService.class);
        String temp = intent.getStringExtra("alarmContent");
        service_Intent.putExtra("alarmContent", temp);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            this.context.startForegroundService(service_Intent);
        }else{
            this.context.startService(service_Intent);
        }
    }
}
