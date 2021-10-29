package com.example.developCall.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageButton;
import android.widget.TextView;

import com.example.developCall.ChatActivity;
import com.example.developCall.Object.Ob_Chat;
import com.example.developCall.R;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<Ob_Chat> sample;
    String username;
    String friendImg;



    public ListAdapter(Context context, ArrayList<Ob_Chat> data, String username, String friendImg) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.username = username;
        this.friendImg = friendImg;
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
    public Ob_Chat getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.list_design, null);


        TextView chatDate = (TextView)view.findViewById(R.id.name_profile);
        //TextView grade = (TextView)view.findViewById(R.id.number_profile);
        TextView keyWord = (TextView)view.findViewById(R.id.duration);
        keyWord.setText(sample.get(position).getKeyWord());
        ImageButton btn_chat = (ImageButton) view.findViewById(R.id.btn_chat);

        chatDate.setText(sample.get(position).getDate());


        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] url = sample.get(position).getS3_url().split("/");
                Log.d("url", url.toString());

                //https://developcall-transcribe-output.s3.ap-northeast-2.amazonaws.com/dd5341b7-db9f-40de-b5d3-a82b878d698b_faf53472-bcd0-43e9-bc2c-bf75dfc335c6_08092021191628.m4a.json

                String httpUrl = "developcall-transcribe-output.s3.ap-northeast-2.amazonaws.com/" + url[3];
                Intent in = new Intent(mContext, ChatActivity.class);
                in.putExtra("chatId",sample.get(position).getId());
                in.putExtra("name",username);
                in.putExtra("friendId", sample.get(position).getFriendID());
                in.putExtra("url",httpUrl);
                in.putExtra("imgUrl", friendImg);
                mContext.startActivity(in);

            }
        });



        return view;
    }

    public void setSample(ArrayList<Ob_Chat> list)
    {
        this.sample = list;
    }






}

