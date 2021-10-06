package com.example.developCall.Fragment;


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
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;




import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils;
import com.amplifyframework.api.graphql.GraphQLResponse;
import com.amplifyframework.api.graphql.PaginatedResult;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Friend;
import com.amplifyframework.datastore.generated.model.Group;
import com.amplifyframework.datastore.generated.model.User;
import com.example.developCall.Adapter.Home_FriendListAdapter;

import com.example.developCall.Alarm.Alarm_ListData;
import com.example.developCall.Alarm.Alarm_Receiver;
import com.example.developCall.Object.Ob_Friend;
import com.example.developCall.Object.Ob_lastCall;

import com.example.developCall.R;
import com.example.developCall.Search.SearchActivity;
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

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeFragment extends Fragment {


    ImageView imageView;
    TextView txt_user_name;
    String username;
    String userId;

    RecyclerView home_rv_friend;
    Home_FriendListAdapter friendListAdapter;
    List<Ob_Friend> friendListArray;


    AlarmManager alarm_manager;
    ArrayList<Alarm_ListData> alarm_listData;
    String alarmName;
    int timeresult, cycleresult, calendarresult;


    View view;


    ImageView searchbtn;

    serviceImpl service = new serviceImpl();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);


        searchbtn = (ImageView) view.findViewById(R.id.img_btn_search_white);

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(getActivity().getApplicationContext(), SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        imageView = view.findViewById(R.id.img_btn_search_white);
        txt_user_name = view.findViewById(R.id.txt_user_name);
        home_rv_friend = view.findViewById(R.id.home_rv_friend);
        userId = Amplify.Auth.getCurrentUser().getUserId();
        friendListArray = new ArrayList<>();
        friendListAdapter = new Home_FriendListAdapter(friendListArray);


        final Intent alarm_intent = new Intent(view.getContext(), Alarm_Receiver.class);
        final Calendar alarmCalendar = Calendar.getInstance();
        alarm_manager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        alarmName = "임시 이름";


        service.getUserName(userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data ->
                {

                    for(User user : data.getData())
                    {
                        username = user.getName();
                        txt_user_name.setText(username);
                        for(Group group : user.getGroup())
                        {
                            for(Friend friend: group.getFriend())
                            {
                                Ob_Friend ob_friend = new Ob_Friend();

                                ob_friend.setId(friend.getId());
                                ob_friend.setName(friend.getName());
                                ob_friend.setNumber(friend.getNumber());
                                ob_friend.setGroupId(friend.getGroupId());
                                ob_friend.setGroupName(group.getName());
                                ob_friend.setRemindDate(friend.getLastContact());
                                ob_friend.setFriendImg(friend.getFriendImg());
                                if (friend.getFavorite() == null) {
                                    ob_friend.setFavorite(false);
                                } else {
                                    ob_friend.setFavorite(friend.getFavorite());
                                }


                                ob_friend.setFriendImg(friend.getFriendImg());
                                friendListArray.add(ob_friend);
                            }

                        }

                    }
                    friendListAdapter.setFriendListArray(friendListArray);
                    friendListAdapter.notifyDataSetChanged();

                }, error ->
                {
                    System.out.println("유저 이름이 없습니다");
                });


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

                    for(int i = 0; i < ob.size(); i++){
                        if(ob.get(i).getLastCall() == null)
                        {

                        }else
                        {
                            String tempName = ob.get(i).getFriendName();
                            String tempCall = ob.get(i).getLastCall();
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(view.getContext(), tempName.charAt(0), alarm_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                            //임시로 현재 날짜를 지정
                                /*long now = System.currentTimeMillis();
                                Date date = new Date(now);
                                SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                                SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                                String getMonth = monthFormat.format(date);
                                String getDay = dayFormat.format(date);*/

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

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            Date tempDate = new Date(alarmCalendar.getTimeInMillis());
                            getDate = dateFormat.format(tempDate);

                            Intent intent = new Intent(view.getContext(), Alarm_Receiver.class);
                            intent.putExtra("alarmContent", getDate);

                            Toast.makeText(view.getContext(), tempDate + " ", Toast.LENGTH_LONG).show();


                            alarm_manager.set(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
                        }

                    }



                    alarm_listData = new ArrayList<Alarm_ListData>();
                    Alarm_ListData listData = new Alarm_ListData();
                    String alarmTempName = alarmGetJsonString("AlarmSetting.json");
                    alarmJsonParsing(alarmTempName);
                    String alarmListName = alarmGetJsonString("alarmFileName");
                    alarmListJsonParsing(alarmListName);
                    try {
                        writeFile("alarmFileName", alarm_listData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }, error ->
                {

                });


        home_rv_friend.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        home_rv_friend.setAdapter(friendListAdapter);
       // setUserAndFriend(userId);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), SearchActivity.class);
                startActivity(in);

            }
        });


        return view;
    }


    public void setUserAndFriend(String userId) {


        Amplify.API.query(
                ModelQuery.list(User.class, User.ID.contains(userId)),
                response ->
                {
                    friendListArray = new ArrayList<>();
                    friendListArray = addFriendList(response);
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            txt_user_name.setText(username);
                            friendListAdapter.setFriendListArray(friendListArray);
                            friendListAdapter.notifyDataSetChanged();


                        }
                    });

                }
                , error ->
                {

                }

        );

    }

    private List<Ob_Friend> addFriendList(GraphQLResponse<PaginatedResult<User>> response) {
        for (User user : response.getData()) {
            username = user.getName();
            for (Group group : user.getGroup()) {
                for (Friend friend : group.getFriend()) {
                    Ob_Friend ob_friend = new Ob_Friend();
                    ob_friend.setName(friend.getName());
                    ob_friend.setFriendImg(friend.getFriendImg());
                    friendListArray.add(ob_friend);
                }
            }

        }

        return friendListArray;
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

            JSONArray dataArray = jsonObject.getJSONArray("AlarmSetting");

            JSONObject dataObject = dataArray.getJSONObject(0);
            timeresult = dataObject.getInt("time");
            cycleresult = dataObject.getInt("cycle");
            calendarresult = dataObject.getInt("calendar");

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void alarmListJsonParsing(String json)
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

}



