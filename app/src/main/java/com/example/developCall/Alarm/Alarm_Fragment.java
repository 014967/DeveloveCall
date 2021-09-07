package com.example.developCall.Alarm;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.developCall.R;

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

public class Alarm_Fragment extends Fragment {

    ArrayList<Alarm_ListData> alarm_listData;
    ListView alarm_listView;
    Alarm_ListAdapter alarm_listAdapter;
    //Alarm_ListData listData;
    String alarmFileName, alarmTempName;

    AlarmManager alarm_manager;
    Context context;
    PendingIntent pendingIntent;

    Button alarmset;
    FragmentManager alarmFragment;
    FragmentTransaction alarmTransaction;
    AlarmSet_Fragment alarmSet_fragment;
    Bundle bundle;
    int onoff, timeresult, cycleresult, calendarresult, check;
    String alarmName, alarmMsg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_layout, container, false);
        context = container.getContext();

        alarmSet_fragment = new AlarmSet_Fragment();
        alarmFragment = getActivity().getSupportFragmentManager();
        alarmTransaction = alarmFragment.beginTransaction();

        bundle = getArguments();

        alarmset = (Button) view.findViewById(R.id.alarmset);

        final Calendar alarmCalendar = Calendar.getInstance();
        final Intent alarm_intent = new Intent(view.getContext(), Alarm_Receiver.class);
        alarm_manager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

        alarmFileName = "alarmFileName";
        alarmName = "임시 이름";

        alarmset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmTransaction.replace(R.id.home_frame, alarmSet_fragment).commitAllowingStateLoss();
            }
        });

        if(bundle != null){
            onoff = bundle.getInt("onoff");
            check = bundle.getInt("check");
            pendingIntent = PendingIntent.getBroadcast(view.getContext(), 0, alarm_intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if(check == 1){
                alarm_listData = new ArrayList<Alarm_ListData>();
                alarmTempName = alarmGetJsonString(alarmFileName);
                alarmJsonParsing(alarmTempName);

                alarm_listView = (ListView) view.findViewById(R.id.alarmlist);
                alarm_listAdapter = new Alarm_ListAdapter(view.getContext(), alarm_listData);
                alarm_listView.setAdapter(alarm_listAdapter);
                setListViewHeightBasedOnItems(alarm_listView);
                Log.d("tag", "체크 성공");
            }

            if(onoff == 1) {
                timeresult = bundle.getInt("timeresult");
                cycleresult = bundle.getInt("cycleresult");
                calendarresult = bundle.getInt("calendarresult");
                check = bundle.getInt("check");

                //임시로 현재 날짜를 지정
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                SimpleDateFormat monthFormat = new SimpleDateFormat("mm");
                String getMonth = monthFormat.format(date);
                String getDay = dayFormat.format(date);
                int addMonth = Integer.parseInt(getMonth)+cycleresult-1;

                if (cycleresult != 0 && cycleresult != 1) {
                    alarmCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(getDay));
                    alarmCalendar.set(Calendar.MONTH, addMonth);
                } else if (cycleresult == 0) {
                    alarmCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(getDay) + 7);
                } else {
                    alarmCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(getDay) + 14);
                }
                alarmCalendar.set(Calendar.HOUR_OF_DAY, timeresult);
                alarmCalendar.set(Calendar.MINUTE, 0);
                alarmCalendar.set(Calendar.SECOND, 0);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date tempDate = new Date(alarmCalendar.getTimeInMillis());
                String getDate = dateFormat.format(tempDate);

                Intent intent = new Intent(view.getContext(), Alarm_Receiver.class);
                intent.putExtra("alarmContent", getDate);

                pendingIntent = PendingIntent.getBroadcast(view.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Toast.makeText(view.getContext(), tempDate + " ", Toast.LENGTH_LONG).show();

                alarm_listData = new ArrayList<Alarm_ListData>();
                Alarm_ListData listData = new Alarm_ListData();
                alarmTempName = alarmGetJsonString(alarmFileName);
                alarmJsonParsing(alarmTempName);

                alarm_listData = AddData(alarm_listData, listData, alarmName, getDate, 0);

                try {
                    writeFile(alarmFileName, alarm_listData);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                alarm_manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
                //alarm_manager.set(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
            }
        }
        else {
            alarm_listData = new ArrayList<Alarm_ListData>();
            alarmTempName = alarmGetJsonString(alarmFileName);
            alarmJsonParsing(alarmTempName);

            alarm_listView = (ListView) view.findViewById(R.id.alarmlist);
            alarm_listAdapter = new Alarm_ListAdapter(view.getContext(), alarm_listData);
            alarm_listView.setAdapter(alarm_listAdapter);
            setListViewHeightBasedOnItems(alarm_listView);
        }

        return view;
    }

    public ArrayList<Alarm_ListData> AddData(ArrayList<Alarm_ListData> alarm_listData, Alarm_ListData listData, String Name, String Content, int Profile){
        listData.setProfile(Profile);
        listData.setName(Name);
        listData.setContent(Content);
        alarm_listData.add(listData);
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

        OutputStreamWriter calendarWriter = new OutputStreamWriter(getActivity().openFileOutput(fileName, Context.MODE_PRIVATE));
        calendarWriter.write(JsonData);
        calendarWriter.close();
    }

    private String alarmGetJsonString(String fileName)
    {
        String json = "";
        try {
            InputStream calendarStream = getActivity().openFileInput(fileName);
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

                alarm_listData.add(0, data);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            //setDynamicHeight(listView);
            return true;

        } else {
            return false;
        }
    }

}
