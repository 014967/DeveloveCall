package com.example.developCall.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.util.ArrayList;

public class Calendar_Fragment extends Fragment {
    ArrayList<CalendarData> dataList;
    Button[] button = null;
    String oldFileName = "";
    String oldName = "";
    Button addbtn;
    CalendarAdapter calendarAdapter;
    ListView listView;
    TextView day;
    Context context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_layout, container, false);
        context = container.getContext();

        button = new Button[7];
        int[] btnid = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7};

        addbtn = (Button)view.findViewById(R.id.add);
        day = (TextView)view.findViewById(R.id.day);

        for(int i = 0; i < 7; i++){
            this.button[i] = (Button)view.findViewById(btnid[i]);
        }


        for(int i = 0; i < 7; i++){
            this.button[i].setOnClickListener(btnListener);
        }

        addbtn.setOnClickListener(btnListener);

        this.InitializeData();

        listView = (ListView)view.findViewById(R.id.listview);
        calendarAdapter = new CalendarAdapter(view.getContext(),dataList);

        listView.setAdapter(calendarAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Intent intent = new Intent(view.getContext(), ListPopUpActivity.class);
                intent.putExtra("name", calendarAdapter.getItem(position).getName());
                intent.putExtra("number", calendarAdapter.getItem(position).getNumber());
                intent.putExtra("position", position);
                startActivityResult.launch(intent);
            }
        });

        return view;
    }

    public void InitializeData()
    {
        dataList = new ArrayList<CalendarData>();
        CalendarData data1 = new CalendarData();
        CalendarData data2 = new CalendarData();
        CalendarData data3 = new CalendarData();

        data1.setName("김현국");
        data1.setNumber("111-1111-1111");

        dataList.add(data1);

        data2.setName("김용학");
        data2.setNumber("222-2222-2222");

        dataList.add(data2);

        data3.setName("박하나");
        data3.setNumber("333-3333-3333");

        dataList.add(data3);
    }

    public final View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ListView listView = (ListView) getActivity().findViewById(R.id.listview);
            final CalendarAdapter calendarAdapter = new CalendarAdapter(v.getContext(), dataList);
            listView.setAdapter(calendarAdapter);

            if(v.getId() == R.id.add) {
                Intent intent = new Intent(v.getContext(), AddPopUpActivity.class);
                startActivityResult.launch(intent);
            }
            else {
                String fileName = v.getId() + ".json";
                String Name = "";
                Button tempbtn = (Button)v.findViewById(v.getId());
                String btnday = tempbtn.getText().toString();

                day.setText(btnday+"일");

                try {
                    if (oldFileName != "") {
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

    public void writeFile(String fileName, ArrayList<CalendarData> dataList) throws IOException {
        JSONObject obj = new JSONObject();
        try {
            JSONArray jArray = new JSONArray();//배열이 필요할때
            for (int i = 0; i < dataList.size(); i++)//배열
            {
                JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
                sObject.put("profile", dataList.get(i).getProfile());
                sObject.put("name", dataList.get(i).getName());
                sObject.put("number", dataList.get(i).getNumber());
                jArray.put(sObject);
            }
            obj.put("Friend", jArray);//배열을 넣음
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

            JSONArray dataArray = jsonObject.getJSONArray("Friend");

            dataList.clear();

            for(int i=0; i<dataArray.length(); i++)
            {
                JSONObject dataObject = dataArray.getJSONObject(i);

                CalendarData data = new CalendarData();

                data.setProfile(dataObject.getInt("profile"));
                data.setName(dataObject.getString("name"));
                data.setNumber(dataObject.getString("number"));

                dataList.add(data);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String nameresult = data.getStringExtra("name");
                        String numberresult = data.getStringExtra("number");

                        if(!nameresult.equals("") && !numberresult.equals("")) {
                            CalendarData adddata = new CalendarData();

                            adddata.setName(nameresult);
                            adddata.setNumber(numberresult);

                            dataList.add(adddata);

                            try {
                                if (oldFileName != "") {
                                    writeFile(oldFileName, dataList);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else if (result.getResultCode() == 1){
                        Intent data = result.getData();
                        String rename = data.getStringExtra("name");
                        String renumber = data.getStringExtra("number");
                        int position = data.getIntExtra("position",0);

                        CalendarData redata = new CalendarData();

                        redata.setName(rename);
                        redata.setNumber(renumber);

                        dataList.set(position, redata);
                        listView.setAdapter(calendarAdapter);

                        try {
                            if (oldFileName != "") {
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

                        try {
                            if (oldFileName != "") {
                                writeFile(oldFileName, dataList);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
}
