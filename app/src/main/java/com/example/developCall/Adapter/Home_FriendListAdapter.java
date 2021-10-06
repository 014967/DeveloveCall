package com.example.developCall.Adapter;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.developCall.Fragment.Fragment2;

import com.example.developCall.Object.Ob_Friend;
import com.example.developCall.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home_FriendListAdapter extends RecyclerView.Adapter<Home_FriendListAdapter.ViewHolder>{

    List<Ob_Friend> friendListArray = new ArrayList<>();

    public Home_FriendListAdapter(List<Ob_Friend> friendListArray)
    {
        this.friendListArray = friendListArray;
    }


    @NonNull
    @Override
    public Home_FriendListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view =layoutInflater.inflate(R.layout.home_friend_list_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Home_FriendListAdapter.ViewHolder holder, int position) {

        Ob_Friend friend = friendListArray.get(position);
        holder.txt_profile_name_01.setText(friend.getName());

        String END_POINT = "https://developcallfriendimg.s3.ap-northeast-2.amazonaws.com/";

        if(friend.getFriendImg() !=null)
        {
            String url = END_POINT+friend.getFriendImg();
            Glide.with(holder.itemView.getContext()).load(url).into(holder.img_profile);
        }


        Bundle bundle = new Bundle();
        bundle.putSerializable("ob_friend", friend);

        holder.img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment2 profile = new Fragment2();
                profile.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, profile).addToBackStack(null).commit();
                //바텀 네비게이션 움직이지않
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendListArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img_profile;
        TextView txt_profile_name_01;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_profile = itemView.findViewById(R.id.img_profile_01);
            txt_profile_name_01 = itemView.findViewById(R.id.txt_profile_name_01);

        }
    }

    public void setFriendListArray(List<Ob_Friend> friendList)
    {
        this.friendListArray = friendList;
    }

}
