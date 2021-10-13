package com.example.developCall.Search;


import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.developCall.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class Search_Fragment extends Fragment implements Search_HistoryFragment.OnHistoryClickListener {

    FragmentManager searchFragment;
    FragmentTransaction searchTransaction;
    Search_HistoryFragment search_historyFragment;
    Search_AddressFragment search_addressFragment;

    ArrayList<Search_HistoryListData> dataList;
    Search_HistoryListAdapter search_historyListAdapter;

    int check;


    ImageView iv_back;

    View view;

    EditText searchContents;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //setContentView(R.layout.search_result);


        view = inflater.inflate(R.layout.search_result, container, false);


        searchFragment = getActivity().getSupportFragmentManager();
        searchTransaction = searchFragment.beginTransaction();
        search_historyFragment = new Search_HistoryFragment(this::onHistoryClick);
        search_addressFragment = new Search_AddressFragment();

        Bundle bundle = new Bundle();
        check = 0;

        dataList = new ArrayList<Search_HistoryListData>();
        String searchTempName = getJsonString("SearchHistory.json");
        jsonParsing(searchTempName);



        searchTransaction.replace(R.id.search_frame, search_historyFragment).commitAllowingStateLoss(); // 검색 목록 보여주는 frame

        searchContents = (EditText) view.findViewById(R.id.search_contents);

        iv_back = view.findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
                bottomNavigationView.setVisibility(View.VISIBLE);
                getActivity().onBackPressed();
            }
        });
        searchContents.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {


                    Bundle d = new Bundle();
                    d.putSerializable("searchKey", searchContents.getText().toString());

                    Search_HistoryListData data = new Search_HistoryListData();
                    data.setContent(searchContents.getText().toString());
                    dataList.add(0, data);
                    if (check == 0) {
                        try {
                            writeFile(dataList);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    check = 1;
                    search_addressFragment = new Search_AddressFragment();
                    searchTransaction = searchFragment.beginTransaction();
                    search_addressFragment.setArguments(d);
                    searchTransaction.replace(R.id.search_frame, search_addressFragment).commitAllowingStateLoss(); //만약 작성이 된다면 search_addressFragment로 이동
                }
                return false;
            }
        });

        return view;
    }


    public void writeFile(ArrayList<Search_HistoryListData> dataList) throws IOException {
        JSONObject obj = new JSONObject();
        try {
            JSONArray jArray = new JSONArray();//배열이 필요할때
            for (int i = 0; i < dataList.size(); i++)//배열
            {
                JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
                sObject.put("history", dataList.get(i).getContent());
                jArray.put(sObject);
            }
            obj.put("Search", jArray);//배열을 넣음
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String JsonData = obj.toString();

        OutputStreamWriter calendarWriter = new OutputStreamWriter(getActivity().openFileOutput("SearchHistory.json", Context.MODE_PRIVATE));
        calendarWriter.write(JsonData);
        calendarWriter.close();
    }

    private String getJsonString(String fileName) {
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

    private void jsonParsing(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray dataArray = jsonObject.getJSONArray("Search");

            dataList.clear();

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataObject = dataArray.getJSONObject(i);

                Search_HistoryListData data = new Search_HistoryListData();

                data.setContent(dataObject.getString("history"));

                dataList.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onHistoryClick(String value)
    {
        searchContents.setText(value);
    }


}



