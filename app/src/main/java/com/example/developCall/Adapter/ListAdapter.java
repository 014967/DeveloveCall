package com.example.developCall.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.developCall.Object.ListData;
import com.example.developCall.R;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<ListData> sample;

    public ListAdapter(Context context, ArrayList<ListData> data) {
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
    public ListData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.list_design, null);

        ImageView imageView = (ImageView)view.findViewById(R.id.img_profile);
        TextView movieName = (TextView)view.findViewById(R.id.name_profile);
        TextView grade = (TextView)view.findViewById(R.id.number_profile);

        imageView.setImageResource(sample.get(position).getProfile());
        movieName.setText(sample.get(position).getName());
        grade.setText(sample.get(position).getNumber());

        return view;
    }
}
