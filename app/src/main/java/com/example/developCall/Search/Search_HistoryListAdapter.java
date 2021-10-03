package com.example.developCall.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.developCall.R;

import java.util.ArrayList;

public class Search_HistoryListAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<Search_HistoryListData> search_History_listData;

    public Search_HistoryListAdapter(Context context, ArrayList<Search_HistoryListData> data) {
        mContext = context;
        search_History_listData = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return search_History_listData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Search_HistoryListData getItem(int position) {
        return search_History_listData.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.search_listview_custom, null);

        TextView Content = (TextView)view.findViewById(R.id.historyContents);

        Content.setText(search_History_listData.get(position).getContent());

        return view;
    }
}