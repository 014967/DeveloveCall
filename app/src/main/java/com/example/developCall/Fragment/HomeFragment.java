package com.example.developCall.Fragment;


import static android.content.Context.ALARM_SERVICE;

import android.app.Activity;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;

import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils;
import com.amplifyframework.api.graphql.GraphQLResponse;
import com.amplifyframework.api.graphql.PaginatedResult;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Chat;
import com.amplifyframework.datastore.generated.model.Friend;
import com.amplifyframework.datastore.generated.model.Group;
import com.amplifyframework.datastore.generated.model.User;
import com.example.developCall.Adapter.Home_FriendListAdapter;
import com.example.developCall.Alarm.Alarm_ListData;
import com.example.developCall.Alarm.Alarm_Receiver;
import com.example.developCall.Calendar.CalendarData;
import com.example.developCall.Calendar.Home_Recycler_Adapter;
import com.example.developCall.Calendar.Home_Recycler_Data;
import com.example.developCall.FriendMultiPopUp;
import com.example.developCall.FriendPopUp;
import com.example.developCall.Object.Ob_Chat;
import com.example.developCall.Object.Ob_Friend;
import com.example.developCall.Object.Ob_lastCall;
import com.example.developCall.R;
import com.example.developCall.Search.Search_Fragment;
import com.example.developCall.Service.serviceImpl;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeFragment extends Fragment {


    ImageView total_img;
    TextView total_txt;
    TextView recommend_txt;
    TextView txt_user_name;
    TextView data_keyword;

    String username;
    String userId;

    RecyclerView home_rv_friend;
    Home_FriendListAdapter friendListAdapter;
    List<Ob_Friend> friendListArray;


    AlarmManager alarm_manager;
    ArrayList<Alarm_ListData> alarm_listData;
    ArrayList<Alarm_ListData> alarm_tempListData;
    String alarmName;
    int timeresult, cycleresult, calendarresult, alarmCount;

    String[] choiceNameArray;
    String getYear;
    String getMonth;
    String getDay;
    String recommendFriend;
    int missionCount;


    RecyclerView recyclerView;
    ArrayList<Home_Recycler_Data> recycler_data;
    Home_Recycler_Adapter recycler_adapter;
    int recyleCount;

    View view;


    ImageView searchbtn;

    serviceImpl service = new serviceImpl();

    Search_Fragment search_fragment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);


        searchbtn = (ImageView) view.findViewById(R.id.img_btn_search_white);


        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent searchIntent = new Intent(getActivity().getApplicationContext(), SearchActivity.class);
                //startActivity(searchIntent);

                search_fragment = new Search_Fragment();
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
                bottomNavigationView.setVisibility(View.INVISIBLE);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, search_fragment).addToBackStack(null).commit();

            }
        });


        txt_user_name = view.findViewById(R.id.txt_user_name);
        home_rv_friend = view.findViewById(R.id.home_rv_friend);
        total_img = view.findViewById(R.id.total_img);
        total_txt = view.findViewById(R.id.txt_total);
        recommend_txt = view.findViewById(R.id.txt_recommend);

        userId = Amplify.Auth.getCurrentUser().getUserId();
        friendListArray = new ArrayList<>();
        friendListAdapter = new Home_FriendListAdapter(friendListArray);

        ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            String choiceName = data.getStringExtra("choicename");
                            try {
                                OutputBufferedStreamEX(choiceName);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            choiceNameArray = choiceName.split(" ");
                        }
                    }
                });

        String choiceName = null;
        missionCount = 0;
        try {
            choiceName = InputBufferedStreamEx();
        } catch (IOException e) {
        }
        try {
            choiceNameArray = choiceName.split(" ");
        } catch (Exception e) {
        }

        total_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), FriendMultiPopUp.class);
                startActivityResult.launch(intent);
            }
        });

        service.getFirstKeyWord(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data ->
                        {

                            List<Ob_Chat> firstKeyList = new ArrayList<>();
                            for (Chat chat : data.getData().getChat()) {
                                Ob_Chat ob_chat = new Ob_Chat();
                                ob_chat.setId(chat.getId());
                                ob_chat.setFriendID(chat.getFriendId());
                                ob_chat.setKeyWord(chat.getKeyWord());
                                ob_chat.setMemo(chat.getMemo());
                                ob_chat.setS3_url(chat.getS3Url());
                                ob_chat.setDate(chat.getDate());
                                firstKeyList.add(ob_chat);
                            }
                            Collections.sort(firstKeyList, new Comparator<Ob_Chat>() {
                                @Override
                                public int compare(Ob_Chat o1, Ob_Chat o2) {
                                    SimpleDateFormat beforeFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
                                    Date o1Date = null;
                                    Date o2Date = null;

                                    try {
                                        o1Date = beforeFormat.parse(o1.getDate());
                                        o2Date = beforeFormat.parse(o2.getDate());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    return o1Date.getTime() > o2Date.getTime() ? -1 : o1Date.getTime() < o2Date.getTime() ? 1 : 0;

                                }
                            });

                        }
                        , error ->
                        {

                        });


        final Intent alarm_intent = new Intent(view.getContext(), Alarm_Receiver.class);
        final Calendar alarmCalendar = Calendar.getInstance();
        alarm_manager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        alarmName = "임시 이름";

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat onlyYearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat onlyMonthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat onlyDayFromat = new SimpleDateFormat("dd");
        String todayDate = dateFormat.format(date);
        String onlyYear = onlyYearFormat.format(date);
        String onlyMonth = onlyMonthFormat.format(date);
        String onlyDay = onlyDayFromat.format(date);
        int todayInt = Integer.parseInt(todayDate);
        int yearInt = Integer.parseInt(onlyYear);
        int monthInt = Integer.parseInt(onlyMonth);
        int dayInt = Integer.parseInt(onlyDay);

        firstInit();
        recyleCount = 0;

        for (int i = dayInt; i < 31; i++) {
            String tempDate = Integer.toString(todayInt);
            String tempName = tempDate + ".json";
            String cycleTempName = cycleGetJsonString(tempName);
            cycleJsonParsing(cycleTempName);
            todayInt++;
        }

        recycler_adapter = new Home_Recycler_Adapter(recycler_data);
        recyclerView.setAdapter(recycler_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));


        service.getUserName(userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data ->
                {

                    for (User user : data.getData()) {
                        username = user.getName();
                        txt_user_name.setText(username);
                        for (Group group : user.getGroup()) {
                            for (Friend friend : group.getFriend()) {
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


        alarm_listData = new ArrayList<Alarm_ListData>();
        Alarm_ListData listData = new Alarm_ListData();
        String alarmTempName = alarmGetJsonString("AlarmSetting.json");
        alarmJsonParsing(alarmTempName);


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

                    for (alarmCount = 0; alarmCount < ob.size(); alarmCount++) {
                        if (ob.get(alarmCount).getLastCall() == null) {

                        } else {
                            String tempName = ob.get(alarmCount).getFriendName();
                            String tempCall = ob.get(alarmCount).getLastCall();
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(view.getContext(), tempName.charAt(0), alarm_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                            getYear = tempCall.substring(4, 8);
                            getMonth = tempCall.substring(2, 4);
                            getDay = tempCall.substring(0, 2);
                            int getDayInt = Integer.parseInt(getDay);
                            int twoWeekCheck = getDayInt + 14;
                            int twoWeekSub = dayInt - 14;
                            String fStringDate = getYear + "-" + getMonth + "-" + getDay;
                            Date fDate = new SimpleDateFormat("yyyy-MM-dd").parse(fStringDate);
                            Calendar friendDate = Calendar.getInstance();
                            friendDate.setTime(fDate);

                            long diffSec = (System.currentTimeMillis() - friendDate.getTimeInMillis())/1000;
                            long diffDay = diffSec/(60*60*24);
                            long max = 0;

                            Log.d("tag", "일차이" + " " + diffDay);

                            try {
                                for (int i = 0; i < choiceNameArray.length; i++) {
                                    if (tempName.equals(choiceNameArray[i])) {
                                        if(diffDay < 14){
                                            missionCount++;
                                        }
                                        if(max < diffDay){
                                            max = diffDay;
                                            recommendFriend = tempName;
                                        }
                                        break;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            int addMonth = Integer.parseInt(getMonth) + cycleresult - 1;
                            int addOneWeekMonth = Integer.parseInt(getMonth) - 1;
                            int addTwoWeekMonth = Integer.parseInt(getMonth) - 1;
                            int addOneWeek = Integer.parseInt(getDay) + 7;
                            int addTwoWeek = Integer.parseInt(getDay) + 14;
                            if (getMonth == "01" || getMonth == "03" || getMonth == "05" || getMonth == "07" || getMonth == "08" || getMonth == "10" || getMonth == "12") {
                                if (addOneWeek > 31) {
                                    addOneWeek = addOneWeek - 31;
                                    addOneWeekMonth++;
                                }
                                if (addTwoWeek > 31) {
                                    addTwoWeek = addTwoWeek - 31;
                                    addTwoWeekMonth++;
                                }
                            } else if (getMonth == "02") {
                                if (addOneWeek > 28) {
                                    addOneWeek = addOneWeek - 28;
                                    addOneWeekMonth++;
                                }
                                if (addTwoWeek > 28) {
                                    addTwoWeek = addTwoWeek - 28;
                                    addTwoWeekMonth++;
                                }
                            } else {
                                if (addOneWeek > 30) {
                                    addOneWeek = addOneWeek - 30;
                                    addOneWeekMonth++;
                                }
                                if (addTwoWeek > 30) {
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

                            SimpleDateFormat getDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
                            Date tempDate = new Date(alarmCalendar.getTimeInMillis());
                            getDate = getDateFormat.format(tempDate);

                            alarm_tempListData = AddData(alarm_listData, listData, alarmName, getDate, 0);

                            alarm_manager.set(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
                        }

                    }

                    /*alarm_listData = new ArrayList<Alarm_ListData>();
                    String alarmListName = alarmGetJsonString("alarmTempFileName");
                    alarmListJsonParsing(alarmListName);*/

                    try {
                        writeFile("alarmTempFileName", alarm_tempListData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    recommend_txt.setText(recommendFriend);

                    if(choiceNameArray != null) {
                        double missonResult = (double)missionCount / (double)choiceNameArray.length;
                        double missionResult100 = missonResult * 100;
                        int missionInt = (int)missionResult100;
                        String missionTxt = Integer.toString(missionInt);
                        String missionSetTxt = missionTxt + "%";
                        total_txt.setText(missionSetTxt);
                        if (missonResult <= 0.2) {
                            total_img.setImageResource(R.drawable.graph_1);
                        } else if (missonResult <= 0.34) {
                            total_img.setImageResource(R.drawable.graph_2);
                        } else if (missonResult <= 0.4) {
                            total_img.setImageResource(R.drawable.graph_3);
                        } else if (missonResult <= 0.5) {
                            total_img.setImageResource(R.drawable.graph_4);
                        } else if (missonResult <= 0.6) {
                            total_img.setImageResource(R.drawable.graph_5);
                        } else if (missonResult <= 0.74) {
                            total_img.setImageResource(R.drawable.graph_6);
                        } else if (missonResult <= 0.9) {
                            total_img.setImageResource(R.drawable.graph_7);
                        } else {
                            total_img.setImageResource(R.drawable.graph_8);
                        }
                    } else{
                        total_img.setImageResource(R.drawable.graph_1);
                    }


                }, error ->
                {

                });




        home_rv_friend.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        home_rv_friend.setAdapter(friendListAdapter);
        // setUserAndFriend(userId);


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

    private String cycleGetJsonString(String fileName) {
        String json = "";
        try {
            InputStream calendarStream = getActivity().openFileInput(fileName);
            int fileSize = calendarStream.available();

            byte[] buffer = new byte[fileSize];
            calendarStream.read(buffer);
            calendarStream.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return json;
    }

    private void cycleJsonParsing(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray dataArray = jsonObject.getJSONArray("Calendar");

            if (recyleCount == 0) {
                recycler_data.clear();
            }


            for (int i = 0; i < dataArray.length(); i++) {
                if (recyleCount < 5) {
                    JSONObject dataObject = dataArray.getJSONObject(i);

                    Home_Recycler_Data data = new Home_Recycler_Data();

                    data.setRecycleName(dataObject.getString("title"));
                    data.setRecycleTarget(dataObject.getString("name"));
                    try {
                        data.setRecycleTime(dataObject.getString("time"));
                    } catch (JSONException e) {
                        data.setRecycleTime("Testing");
                    }

                    recycler_data.add(data);
                }
                recyleCount++;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void firstInit() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recycler_data = new ArrayList<>();
    }

    public void addItem(String recycle_Title, String recycle_Target, String recycle_Time) {
        Home_Recycler_Data item = new Home_Recycler_Data();

        item.setRecycleName(recycle_Title);
        item.setRecycleTarget(recycle_Target);
        item.setRecycleTime(recycle_Time);

        recycler_data.add(item);
    }

    public ArrayList<Alarm_ListData> AddData(ArrayList<Alarm_ListData> alarm_addListData, Alarm_ListData listData, String Name, String Content, int Profile) {
        if (alarmCount == 0) {
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
            try {
                for (int i = 0; i < dataList.size(); i++)//배열
                {
                    JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
                    sObject.put("profile", dataList.get(i).getProfile());
                    sObject.put("name", dataList.get(i).getName());
                    sObject.put("content", dataList.get(i).getContent());
                    jArray.put(sObject);
                }
                obj.put("Alarm", jArray);//배열을 넣음
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String JsonData = obj.toString();


        OutputStreamWriter calendarWriter = new OutputStreamWriter(view.getContext().openFileOutput(fileName, Context.MODE_PRIVATE));

        calendarWriter.write(JsonData);
        calendarWriter.close();
    }

    private String alarmGetJsonString(String fileName) {
        String json = "";
        try {

            InputStream calendarStream = view.getContext().openFileInput(fileName);

            int fileSize = calendarStream.available();

            byte[] buffer = new byte[fileSize];
            calendarStream.read(buffer);
            calendarStream.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return json;
    }

    private void alarmJsonParsing(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray dataArray = jsonObject.getJSONArray("AlarmSetting");

            JSONObject dataObject = dataArray.getJSONObject(0);
            timeresult = dataObject.getInt("time");
            cycleresult = dataObject.getInt("cycle");
            calendarresult = dataObject.getInt("calendar");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void OutputBufferedStreamEX(String str) throws IOException {
        File file = new File(view.getContext().getFilesDir(), "MissionList");
        BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream(file));
        bs.write(str.getBytes()); //Byte형으로만 넣을 수 있음
        bs.close();
    }


    private String InputBufferedStreamEx() throws IOException {
        String result = "";
        // 바이트 단위로 파일읽기
        File file = new File(view.getContext().getFilesDir(), "MissionList");
        FileInputStream fileStream = null; // 파일 스트림

        fileStream = new FileInputStream(file);
        byte[] readBuffer = new byte[fileStream.available()];
        while((fileStream.read(readBuffer))!= -1){
        }
        result = new String(readBuffer);
        fileStream.close(); //스트림 닫기
        return result;
    }
}






