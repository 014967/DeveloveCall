package com.example.developCall.Search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.developCall.MemoActivity;
import com.example.developCall.Object.Ob_Memo;
import com.example.developCall.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Search_MemoAdapter extends RecyclerView.Adapter<Search_MemoAdapter.ViewHolder> {

    List<Ob_Memo> memoList = new ArrayList<>();

    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat newDateFormat;

    Date date;
    String newDate;

    Context context;

    public Search_MemoAdapter(Context context, List<Ob_Memo> memoList) {
        this.context= context;
        this.memoList = memoList;
    }


    @NonNull
    @Override
    public Search_MemoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_memo, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Search_MemoAdapter.ViewHolder holder, int position) {


        String chatId = memoList.get(position).getId();
        if (memoList.get(position).getMemo() != "") {
            String[] fullMemo = memoList.get(position).getMemo().split(chatId);


            holder.txt_title.setText(fullMemo[0]);
            holder.txt_memo.setText(fullMemo[1]);


            holder.memo_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(context, MemoActivity.class);
                    in.putExtra("title", fullMemo[0]);
                    in.putExtra("content", fullMemo[1]);
                    context.startActivity(in);
                }
            });

        }


        String END_POINT = "https://developcallfriendimg.s3.ap-northeast-2.amazonaws.com/";

        if(memoList.get(position).getFriendImg() !=null)
        {
            String url = END_POINT+memoList.get(position).getFriendImg();
            Glide.with(holder.itemView.getContext()).load(url).into(holder.friendImg);
        }

        holder.friendName.setText(memoList.get(position).getFriendName());



        simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
        newDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");


        try {
            date = simpleDateFormat.parse(memoList.get(position).getDate());
            newDate = newDateFormat.format(date);
            holder.txt_date.setText(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }



    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        LinearLayout memo_layout;
        CircleImageView friendImg;
        TextView txt_memo;
        TextView txt_date;
        TextView txt_title;
        TextView friendName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            memo_layout = itemView.findViewById(R.id.memo_layout);
            friendImg = itemView.findViewById(R.id.friendImg);
            txt_memo = itemView.findViewById(R.id.txt_memo);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_title = itemView.findViewById(R.id.txt_title);
            friendName = itemView.findViewById(R.id.friendName);


        }
    }

    public void initMemoList(List<Ob_Memo> memoList) {

        this.memoList = memoList;
    }

}
