package com.example.developCall.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageButton;
import android.widget.TextView;

import com.example.developCall.MemoActivity;
import com.example.developCall.Object.Ob_Chat;
import com.example.developCall.R;

import java.util.ArrayList;

public class MemoAdapter extends BaseAdapter {

    Context context;
    ArrayList<Ob_Chat> sample;
    LayoutInflater layoutInflater;
    String username;

    String st_title;
    String st_content;
    public MemoAdapter(Context context, ArrayList<Ob_Chat> data , String username)
    {
        this.context = context;
        this.sample = data;
        this.layoutInflater = LayoutInflater.from(context);
        this.username= username;
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public Object getItem(int position) {
        return sample.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.list_design, null);

        TextView title = (TextView)view.findViewById(R.id.name_profile);
        TextView memoDate = (TextView) view.findViewById(R.id.number_profile);

        ImageButton btn_memo = (ImageButton) view.findViewById(R.id.btn_chat);



        if(sample.get(position).getMemo().equals(""))
        {

        } else
        {
            String chatId = sample.get(position).getId();
            String[] dummy = sample.get(position).getMemo().split(chatId);
            st_title = dummy[0];
            st_content = dummy[1];
            String st_date = sample.get(position).getDate();

            title.setText(st_title);
            memoDate.setText(st_date);

        }


        btn_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent in = new Intent(context, MemoActivity.class);
                    in.putExtra("title", st_title);
                    in.putExtra("content", st_content);
                    context.startActivity(in);


            }
        });





        return view;
    }

    public void setSample(ArrayList<Ob_Chat> list)
    {
        this.sample = list;
    }



}
