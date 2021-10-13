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

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Friend;
import com.amplifyframework.datastore.generated.model.Group;
import com.amplifyframework.datastore.generated.model.User;
import com.example.developCall.Object.Ob_lastCall;
import com.example.developCall.R;
import com.example.developCall.Service.serviceImpl;

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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Alarm_Fragment extends Fragment {

    ArrayList<Alarm_ListData> alarm_listData;
    ArrayList<Alarm_ListData> alarm_tempListData;
    ListView alarm_listView;
    Alarm_ListAdapter alarm_listAdapter;
    //Alarm_ListData listData;
    String alarmFileName, alarmTempName, alarmTempFileName;

    String userId;

    AlarmManager alarm_manager;
    Context context;
    View view;


    Button alarmset;
    FragmentManager alarmFragment;
    FragmentTransaction alarmTransaction;
    AlarmSet_Fragment alarmSet_fragment;
    Bundle bundle;
    int onoff, timeresult, cycleresult, calendarresult, check;
    int alarmCount;
    String alarmName, alarmMsg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.alarm_layout, container, false);
        context = container.getContext();


        serviceImpl service = new serviceImpl();


        alarmSet_fragment = new AlarmSet_Fragment();
        alarmFragment = getActivity().getSupportFragmentManager();
        alarmTransaction = alarmFragment.beginTransaction();

        bundle = getArguments();

        alarmset = (Button) view.findViewById(R.id.alarmset);

        final Calendar alarmCalendar = Calendar.getInstance();
        final Intent alarm_intent = new Intent(view.getContext(), Alarm_Receiver.class);
        alarm_manager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

        alarmFileName = "alarmFileName";
        alarmTempFileName = "alarmTempFileName";
        alarmName = "임시 이름";

        userId = Amplify.Auth.getCurrentUser().getUserId();


        alarmset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmTransaction.replace(R.id.home_frame, alarmSet_fragment).commitAllowingStateLoss();
            }
        });


        if(bundle != null){
            onoff = bundle.getInt("onoff");
            check = bundle.getInt("check");

            alarm_tempListData = new ArrayList<Alarm_ListData>();
            alarm_listData = new ArrayList<Alarm_ListData>();
            Alarm_ListData listData = new Alarm_ListData();
            alarmTempName = alarmGetJsonString(alarmFileName);
            alarmJsonParsing(alarmTempName);

            alarm_listView = (ListView) view.findViewById(R.id.alarmlist);
            alarm_listAdapter = new Alarm_ListAdapter(view.getContext(), alarm_listData);
            alarm_listView.setAdapter(alarm_listAdapter);
            setListViewHeightBasedOnItems(alarm_listView);


            if(check == 1){
                /*alarm_listData = new ArrayList<Alarm_ListData>();
                alarmTempName = alarmGetJsonString(alarmFileName);
                alarmJsonParsing(alarmTempName);*/

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


                try {
                    writeAlarmSetting(timeresult, cycleresult, calendarresult);

                } catch (IOException e) {
                    e.printStackTrace();
                }


                ArrayList<Ob_lastCall> ob = new ArrayList<>();
                service.getData(userId).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(data ->
                        {
                            for (User user : data.getData()) {
                                for (Group group : user.getGroup()) {
                                    for (Friend friend : group.getFriend()) {
                                        Ob_lastCall ob_lastCall = new Ob_lastCall();
                                        ob_lastCall.setFriendName(friend.getName());
                                        ob_lastCall.setLastCall(friend.getLastContact());
                                        ob.add(ob_lastCall);
                                    }
                                }
                            }
                            String getDate = "";

                            for(alarmCount = 0; alarmCount < ob.size(); alarmCount++){
                                if(ob.get(alarmCount).getLastCall() == null)
                                {

                                }else
                                {
                                    String tempName = ob.get(alarmCount).getFriendName();
                                    String tempCall = ob.get(alarmCount).getLastCall();
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(view.getContext(), tempName.charAt(0), alarm_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                                    //임시로 현재 날짜를 지정
                                /*long now = System.currentTimeMillis();
                                Date date = new Date(now);
                                SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                                SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                                SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
                                String tempMonth = monthFormat.format(date);
                                String tempDay = dayFormat.format(date);
                                String tempMin = minuteFormat.format(date);*/

                                    String getMonth = tempCall.substring(2,4);
                                    String getDay = tempCall.substring(0,2);
                                    int addMonth = Integer.parseInt(getMonth)+cycleresult-1;
                                    int addOneWeekMonth = Integer.parseInt(getMonth)-1;
                                    int addTwoWeekMonth = Integer.parseInt(getMonth)-1;
                                    int addOneWeek = Integer.parseInt(getDay) + 7;
                                    int addTwoWeek = Integer.parseInt(getDay) + 14;
                                    if(getMonth == "01" || getMonth == "03" || getMonth == "05" || getMonth == "07" || getMonth == "08" || getMonth == "10" || getMonth == "12"){
                                        if(addOneWeek > 31){
                                            addOneWeek = addOneWeek - 31;
                                            addOneWeekMonth++;
                                        }
                                        if(addTwoWeek > 31){
                                            addTwoWeek = addTwoWeek - 31;
                                            addTwoWeekMonth++;
                                        }
                                    }
                                    else if(getMonth == "02"){
                                        if(addOneWeek > 28){
                                            addOneWeek = addOneWeek - 28;
                                            addOneWeekMonth++;
                                        }
                                        if(addTwoWeek > 28){
                                            addTwoWeek = addTwoWeek - 28;
                                            addTwoWeekMonth++;
                                        }
                                    }
                                    else{
                                        if(addOneWeek > 30){
                                            addOneWeek = addOneWeek - 30;
                                            addOneWeekMonth++;
                                        }
                                        if(addTwoWeek > 30){
                                            addTwoWeek = addTwoWeek - 30;
                                            addTwoWeekMonth++;
                                        }
                                    }

                                    if (cycleresult != 0 && cycleresult != 1) {
                                        alarmCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(getDay));
                                        alarmCalendar.set(Calendar.MONTH, addMonth);
                                    } else if (cycleresult == 0) {
                                        alarmCalendar.set(Calendar.DAY_OF_MONTH, addOneWeek);
                                        alarmCalendar.set(Calendar.MONTH, addOneWeekMonth);
                                    } else {
                                        alarmCalendar.set(Calendar.DAY_OF_MONTH, addTwoWeek);
                                        alarmCalendar.set(Calendar.MONTH, addTwoWeekMonth);
                                    }
                                    alarmCalendar.set(Calendar.HOUR_OF_DAY, timeresult);
                                    alarmCalendar.set(Calendar.MINUTE, 0);
                                    alarmCalendar.set(Calendar.SECOND, 0);

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
                                    Date tempDate = new Date(alarmCalendar.getTimeInMillis());
                                    getDate = dateFormat.format(tempDate);

                                    //Toast.makeText(view.getContext(), tempDate + " ", Toast.LENGTH_LONG).show();
                                    Log.d("tag",tempDate + " ");
                                    alarm_tempListData = AddData(alarm_tempListData, listData, alarmName, getDate, 0);

                                    /*if(alarmCount == 0) {
                                        alarm_manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+20000, pendingIntent);
                                    }
                                    else if(alarmCount == 1){
                                        alarm_manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+80000, pendingIntent);
                                    }
                                    else{
                                        alarm_manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+160000, pendingIntent);
                                    }*/
                                    alarm_manager.set(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
                                }

                            }

                            Toast.makeText(view.getContext(),  "알람 설정 완료", Toast.LENGTH_LONG).show();

                            alarm_listView = (ListView) view.findViewById(R.id.alarmlist);
                            alarm_listAdapter = new Alarm_ListAdapter(view.getContext(), alarm_listData);
                            alarm_listView.setAdapter(alarm_listAdapter);
                            setListViewHeightBasedOnItems(alarm_listView);

                            try {
                                writeFile(alarmTempFileName, alarm_tempListData);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }, error ->
                        {

                        });


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

    public ArrayList<Alarm_ListData> AddData(ArrayList<Alarm_ListData> alarm_addListData, Alarm_ListData listData, String Name, String Content, int Profile){
        if(alarmCount == 0){
            alarm_addListData.clear();
        }
        listData.setProfile(Profile);
        listData.setName(Name);
        listData.setContent(Content);

        alarm_addListData.add(0, listData);

        return alarm_addListData;
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


    public void writeAlarmSetting(int timeresult, int cycleresult, int calendarresult) throws IOException {
        JSONObject obj = new JSONObject();
        try {
            JSONArray jArray = new JSONArray();//배열이 필요할때
            JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
            sObject.put("time",timeresult);
            sObject.put("cycle", cycleresult);
            sObject.put("calendar", calendarresult);
            jArray.put(sObject);
            obj.put("AlarmSetting", jArray);//배열을 넣음
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String JsonData = obj.toString();

        OutputStreamWriter calendarWriter = new OutputStreamWriter(view.getContext().openFileOutput("AlarmSetting.json", Context.MODE_PRIVATE));
        calendarWriter.write(JsonData);
        calendarWriter.close();
    }

    /*public void writeLastCall(String userName, String lastCall) throws IOException {
        JSONObject obj = new JSONObject();
        try {
            JSONArray jArray = new JSONArray();//배열이 필요할때
            JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
            sObject.put("lastcall", lastCall);
            jArray.put(sObject);
            obj.put(userName, jArray);//배열을 넣음
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String JsonData = obj.toString();

        OutputStreamWriter calendarWriter = new OutputStreamWriter(getActivity().openFileOutput(userName, Context.MODE_PRIVATE));
        calendarWriter.write(JsonData);
        calendarWriter.close();
    }*/


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


    /*private int alarmJsonLastCallParsing(String json, String userName)
    {
        int tempLastCall = 0;
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray dataArray = jsonObject.getJSONArray(userName);
            JSONObject dataObject = dataArray.getJSONObject(0);
            tempLastCall = dataObject.getInt("lastcall");

        }catch (JSONException e) {
            e.printStackTrace();
        }

        return tempLastCall;
    }

    private void alarmJsonSettingParsing(String json)
    {
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray dataArray = jsonObject.getJSONArray("AlarmSetting");

            JSONObject dataObject = dataArray.getJSONObject(0);
            timeresult = dataObject.getInt("time");
            cycleresult = dataObject.getInt("cycle");
            calendarresult = dataObject.getInt("calendar");
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }*/


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