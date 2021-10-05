package com.example.developCall.Alarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.developCall.R;

import java.util.ArrayList;

public class Alarm_ListAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<Alarm_ListData> sample;

    public Alarm_ListAdapter(Context context, ArrayList<Alarm_ListData> data) {
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
    public Alarm_ListData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.alarm_listview, null);

        ImageView imageView = (ImageView)view.findViewById(R.id.Profile);
        TextView Name = (TextView)view.findViewById(R.id.Name);
        TextView Content = (TextView)view.findViewById(R.id.Content);

        imageView.setImageResource(sample.get(position).getProfile());
        Name.setText(sample.get(position).getName());
        Content.setText(sample.get(position).getContent());

        return view;
    }
}
