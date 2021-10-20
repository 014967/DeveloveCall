package com.example.developCall.Search;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.developCall.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Search_HistoryFragment extends Fragment {

    Context context;
    ArrayList<Search_HistoryListData> search_historyListData;
    Search_HistoryListAdapter search_historyListAdapter;
    ListView searchListview;

    TextView searchText;

    OnHistoryClickListener historyClickListener;

    public interface OnHistoryClickListener{
        void onHistoryClick(String value);
    }
    public Search_HistoryFragment(OnHistoryClickListener onHistoryClick) {
        this.historyClickListener = onHistoryClick;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.search_history_list, container, false);

        View view2 = inflater.inflate(R.layout.search_result, container, false);

        context = container.getContext();

        this.InitializeData();


        searchText = (TextView) view2.findViewById(R.id.search_contents);


        search_historyListData = new ArrayList<Search_HistoryListData>();
        String searchTempName = getJsonString("SearchHistory.json");
        jsonParsing(searchTempName);


        searchListview = (ListView)view.findViewById(R.id.search_listview);
        search_historyListAdapter = new Search_HistoryListAdapter(view.getContext(), search_historyListData);

        searchListview.setAdapter(search_historyListAdapter);


        searchListview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                //((HomeActivity)getActivity()).searchContents.setText(search_historyListAdapter.getItem(position).getContent());
                historyClickListener.onHistoryClick(search_historyListAdapter.getItem(position).getContent());
            }
        });


        return view;
    }

    public void InitializeData()
    {
        search_historyListData = new ArrayList<Search_HistoryListData>();
        Search_HistoryListData data1 = new Search_HistoryListData();
        Search_HistoryListData data2 = new Search_HistoryListData();
        Search_HistoryListData data3 = new Search_HistoryListData();

        data1.setContent("과제");

        search_historyListData.add(data1);

        data2.setContent("떡볶이");

        search_historyListData.add(data2);

        data3.setContent("프로젝트");

        search_historyListData.add(data3);
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

            JSONArray dataArray = jsonObject.getJSONArray("Search");

            search_historyListData.clear();

            for(int i=0; i<dataArray.length(); i++)
            {
                JSONObject dataObject = dataArray.getJSONObject(i);

                Search_HistoryListData data = new Search_HistoryListData();

                data.setContent(dataObject.getString("history"));

                search_historyListData.add(data);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
