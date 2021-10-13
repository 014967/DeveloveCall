package com.example.developCall.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import java.util.Locale;

public class Calendar_Fragment extends Fragment {
    ArrayList<CalendarData> dataList;
    CalendarAdapter calendarAdapter;

    Button[] button = null;
    Button addbtn;
    Button oldbtn;
    ListView listView;
    TextView day;
    TextView weekDay;
    TextView monthYear;

    String oldFileName = "";
    String oldName = "";
    String fileName = "";
    String getMonth;
    String getDay;
    String getYear;
    String getWeek;
    int numDay;

    Calendar tempCalendar;

    Context context;


    public Calendar_Fragment(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_layout, container, false);
        context = container.getContext();

        ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            String titleresult = data.getStringExtra("title");
                            String nameresult = data.getStringExtra("name");
                            String timeresult = data.getStringExtra("time");

                            if(!nameresult.equals("") && !titleresult.equals("")) {
                                CalendarData adddata = new CalendarData();

                                adddata.setTitle(titleresult);
                                adddata.setName(nameresult);
                                adddata.setTime(timeresult);

                                dataList.add(adddata);

                                try {
                                    if (!oldFileName.equals("")) {
                                        writeFile(oldFileName, dataList);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            listView.setAdapter(calendarAdapter);
                        }
                        else if (result.getResultCode() == 1){
                            Intent data = result.getData();
                            String retitle = data.getStringExtra("title");
                            String rename = data.getStringExtra("name");
                            String retime = data.getStringExtra("time");
                            int position = data.getIntExtra("position",0);

                            CalendarData redata = new CalendarData();

                            redata.setTitle(retitle);
                            redata.setName(rename);
                            redata.setTime(retime);

                            dataList.set(position, redata);
                            listView.setAdapter(calendarAdapter);

                            try {
                                if (!oldFileName.equals("")) {
                                    writeFile(oldFileName, dataList);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else if (result.getResultCode() == 2){
                            Intent data = result.getData();
                            int position = data.getIntExtra("position",0);

                            dataList.remove(position);
                            listView.setAdapter(calendarAdapter);

                            String Name = "";

                            try {
                                if (!oldFileName.equals("")) {
                                    writeFile(oldFileName, dataList);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        View.OnClickListener btnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button tempbtn = (Button)v.findViewById(v.getId());
                String btnday = tempbtn.getText().toString();
                int max = btnday.length();

                ListView listView = (ListView) getActivity().findViewById(R.id.listview);
                final CalendarAdapter calendarAdapter = new CalendarAdapter(v.getContext(), dataList);
                listView.setAdapter(calendarAdapter);

                if(v.getId() == R.id.add) {
                    Intent intent = new Intent(v.getContext(), AddPopUpActivity.class);
                    startActivityResult.launch(intent);
                }
                else {
                    String str1 = btnday.substring(0, btnday.indexOf(" "));
                    String [] dummy = btnday.split(" ");

                    String str2 = dummy[1];
                    if(str2.length() == 1)
                    {
                        str2 = "0"+str2;
                    }

                    fileName = getYear + getMonth + str2+ ".json";
                    String Name = "";

                    day.setText(str2);
                    weekDay.setText(str1);

                    try {
                        if (!oldFileName.equals("")) {
                            writeFile(oldFileName, dataList);
                        }
                        Name = getJsonString(fileName);
                        jsonParsing(Name);
                        oldFileName = fileName;
                        oldName = Name;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

        button = new Button[31];
        int[] btnid = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.button10, R.id.button11, R.id.button12, R.id.button13, R.id.button14, R.id.button15, R.id.button16, R.id.button17, R.id.button18, R.id.button19, R.id.button20, R.id.button21, R.id.button22, R.id.button23, R.id.button24, R.id.button25, R.id.button26, R.id.button27, R.id.button28, R.id.button29, R.id.button30, R.id.button31};

        addbtn = (Button)view.findViewById(R.id.add);

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat weekFormat = new SimpleDateFormat("EE", Locale.getDefault());
        getMonth = monthFormat.format(date);
        getDay = dayFormat.format(date);
        getYear = yearFormat.format(date);
        getWeek = weekFormat.format(date);
        numDay = Integer.parseInt(getDay);

        tempCalendar = Calendar.getInstance();
        tempCalendar.set(Calendar.YEAR, Integer.parseInt(getYear));
        tempCalendar.set(Calendar.MONTH, Integer.parseInt(getMonth)-1);

        String setMonthYear = getMonth + " " + getYear;

        day = (TextView)view.findViewById(R.id.day);
        weekDay = (TextView) view.findViewById(R.id.weekday);
        monthYear = (TextView) view.findViewById(R.id.monthyear);

        weekDay.setText(getWeek);
        monthYear.setText(setMonthYear);
        day.setText(getDay);

        for(int i = 0; i < 31; i++){
            this.button[i] = (Button)view.findViewById(btnid[i]);
        }

        for(int i = 0; i < 31; i++){
            this.button[i].setOnClickListener(btnListener);
            this.button[i].setOnTouchListener(btnTouchListener);
            this.button[i].setBackgroundColor(getResources().getColor(R.color.white));
            this.button[i].setTextColor(getResources().getColor(R.color.text_dark_gray));
        }

        for(int i = 0; i < 31; i++){
            tempCalendar.set(Calendar.DAY_OF_MONTH, i+1);
            Date tempDate = new Date(tempCalendar.getTimeInMillis());
            SimpleDateFormat tempWeekFormat = new SimpleDateFormat("EE", Locale.getDefault());

            String tempweek = tempWeekFormat.format(tempDate);
            String btnNum = Integer.toString(i+1);
            String btnText = tempweek + " " + btnNum;
            this.button[i].setText(btnText);
        }

        addbtn.setOnClickListener(btnListener);


        if(numDay == 4 || numDay == 6 || numDay == 9 || numDay == 11){
            button[30].setVisibility(View.INVISIBLE);
        }
        else if(numDay == 2){
            button[28].setVisibility(view.INVISIBLE);
            button[29].setVisibility(View.INVISIBLE);
            button[30].setVisibility(View.INVISIBLE);
        }

        String dayToString = Integer.toString(button[numDay-1].getId());

        dataList = new ArrayList<CalendarData>();
        String calendarTempName = getJsonString(dayToString+".json");
        jsonParsing(calendarTempName);

        listView = (ListView)view.findViewById(R.id.listview);
        calendarAdapter = new CalendarAdapter(view.getContext(),dataList);
        listView.setAdapter(calendarAdapter);

        setListViewHeightBasedOnItems(listView);

        oldFileName = "";

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Intent intent = new Intent(view.getContext(), ListPopUpActivity.class);
                intent.putExtra("title", calendarAdapter.getItem(position).getTitle());
                intent.putExtra("name", calendarAdapter.getItem(position).getName());
                intent.putExtra("position", position);
                startActivityResult.launch(intent);
            }
        });

        return view;
    }

    public final View.OnTouchListener btnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent)  {
            Button tempbtn = (Button)v.findViewById(v.getId());
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                try {
                    oldbtn.setBackgroundColor(getResources().getColor(R.color.white));
                    oldbtn.setTextColor(getResources().getColor(R.color.text_dark_gray));
                } catch (Exception e) {
                }
                tempbtn.setBackgroundColor(getResources().getColor(R.color.pink_orange));
                tempbtn.setTextColor(getResources().getColor(R.color.white));
                tempbtn.setBackground(getResources().getDrawable(R.drawable.calendar_date));
            }
            oldbtn = tempbtn;
            return false;
        }
    };

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

        OutputStreamWriter calendarWriter = new OutputStreamWriter(getActivity().openFileOutput(fileName, Context.MODE_PRIVATE));
        calendarWriter.write(JsonData);
        calendarWriter.close();
    }

    private String getJsonString(String fileName)
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

    private void jsonParsing(String json)
    {
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray dataArray = jsonObject.getJSONArray("Calendar");

            dataList.clear();

            for(int i=0; i<dataArray.length(); i++)
            {
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
            //params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            params.height = 1200;
            listView.setLayoutParams(params);
            listView.requestLayout();
            //setDynamicHeight(listView);
            return true;

        } else {
            return false;
        }
    }
}