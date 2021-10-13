package com.example.developCall.Search;

import android.annotation.SuppressLint;
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
import com.example.developCall.ChatActivity;
import com.example.developCall.Object.Ob_SearchChat;
import com.example.developCall.R;
import com.example.developCall.Service.service;
import com.example.developCall.Service.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Search_ContentAdapter extends RecyclerView.Adapter<Search_ContentAdapter.ViewHolder>{



    List<Ob_SearchChat> searchChat = new ArrayList<>();

    service service= new serviceImpl();

    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat newDateFormat;

    Date date;
    String newDate;

    Context context;

    String imgUrl;
    public Search_ContentAdapter(Context context, List<Ob_SearchChat> searchChat) {
        this.searchChat = searchChat;
        this.context = context;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_detail_chat,null, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {




        if(searchChat.get(position).getChatList().size() != 0)
        {

            simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
            newDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

            try {
                date = simpleDateFormat.parse(searchChat.get(position).getDate());
                newDate = newDateFormat.format(date);
                holder.txt_date.setText(newDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            String END_POINT = "https://developcallfriendimg.s3.ap-northeast-2.amazonaws.com/";


            if(searchChat.get(position).getFriendImg() !=null)
            {
                String url = END_POINT+searchChat.get(position).getFriendImg();
                Glide.with(holder.itemView.getContext()).load(url).into(holder.friendImg);
                imgUrl = url;
            }

            holder.friendName.setText(searchChat.get(position).getFriendName());

            holder.txt_content.setText(searchChat.get(position).getChatList().get(0).getContent());



            String[] url = searchChat.get(position).getS3_url().split("/");
            String httpUrl = "developcall-transcribe-output.s3.ap-northeast-2.amazonaws.com/" + url[3];

            holder.detailLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("chatId",searchChat.get(position).getChatList().get(0).getChat_Id());
                    intent.putExtra("name",searchChat.get(position).getFriendName());
                    intent.putExtra("friendId", searchChat.get(position).getFriendId());
                    intent.putExtra("url",httpUrl);
                    intent.putExtra("imgUrl", imgUrl);
                    context.startActivity(intent);

                }
            });
        }
        else
        {

        }





    }

    @Override
    public int getItemCount() {
        return searchChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView friendImg;
        TextView friendName;
        TextView txt_content;
        TextView txt_date;
        LinearLayout detailLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            friendImg = itemView.findViewById(R.id.friendImg);
            friendName = itemView.findViewById(R.id.friendName);
            txt_content = itemView.findViewById(R.id.txt_content);
            txt_date =itemView.findViewById(R.id.txt_date);
            detailLayout = itemView.findViewById(R.id.detailLayout);
        }
    }



    public void initList(List<Ob_SearchChat> searchChat)
    {
        this.searchChat = searchChat;



    }




}
