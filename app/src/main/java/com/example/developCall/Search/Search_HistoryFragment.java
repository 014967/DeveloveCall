package com.example.developCall.Search;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.developCall.R;

import java.util.ArrayList;

public class Search_HistoryFragment extends Fragment {

    Context context;
    ArrayList<Search_HistoryListData> search_historyListData;
    Search_HistoryListAdapter search_historyListAdapter;
    ListView searchListview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.search_history_list, container, false);
        context = container.getContext();

        this.InitializeData();

        searchListview = (ListView)view.findViewById(R.id.search_listview);
        search_historyListAdapter = new Search_HistoryListAdapter(view.getContext(), search_historyListData);

        searchListview.setAdapter(search_historyListAdapter);

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
}
