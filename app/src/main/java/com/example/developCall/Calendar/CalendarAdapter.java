package com.example.developCall.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.developCall.R;

import java.util.ArrayList;

public class CalendarAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<CalendarData> sample;

    public CalendarAdapter(Context context, ArrayList<CalendarData> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public CalendarData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.listview_custom, null);

        TextView Title = (TextView)view.findViewById(R.id.Calendar_Title);
        TextView Name = (TextView)view.findViewById(R.id.Calendar_Name);
        TextView Time = (TextView)view.findViewById(R.id.Calendar_Time);

        Title.setText(sample.get(position).getTitle());
        Name.setText(sample.get(position).getName());
        Time.setText(sample.get(position).getTime());

        return view;
    }
}