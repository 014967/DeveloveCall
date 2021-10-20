package com.example.developCall.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.developCall.Fragment.Fragment2;
import com.example.developCall.Object.Ob_Friend;
import com.example.developCall.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Search_ContactAdapter extends RecyclerView.Adapter<Search_ContactAdapter.ViewHolder> {


    List<Ob_Friend> ob_friendList = new ArrayList<>();

    public Search_ContactAdapter(List<Ob_Friend> ob_friendList) {

        this.ob_friendList = ob_friendList;

    }

    @NonNull
    @Override
    public Search_ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_friend, null, false);



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Search_ContactAdapter.ViewHolder holder, int position) {

        Ob_Friend friend = ob_friendList.get(position);
        holder.friendName.setText(friend.getName());
        Bundle bundle = new Bundle();
        bundle.putSerializable("ob_friend", friend);
        String END_POINT = "https://developcallfriendimg.s3.ap-northeast-2.amazonaws.com/";


        if(friend.getFriendImg() != null)
        {
            String url = END_POINT+friend.getFriendImg();
            Glide.with(holder.itemView.getContext()).load(url).into(holder.friendImg);

        }




        holder.rv_friendItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AppCompatActivity activity = (AppCompatActivity) v.getContext();



                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment2 profile = new Fragment2();
                BottomNavigationView bottomNavigationView = activity.findViewById(R.id.nav_view);
                bottomNavigationView.setVisibility(View.VISIBLE);
                profile.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, profile).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ob_friendList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rv_friendItem;
        CircleImageView friendImg;
        TextView friendName;
        TextView lastConnect;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rv_friendItem = itemView.findViewById(R.id.rv_friendItem);
            progressBar = itemView.findViewById(R.id.progressBar);
            friendImg = itemView.findViewById(R.id.img_profile_03);
            friendName = itemView.findViewById(R.id.search_result_adrress_name_01);
            lastConnect = itemView.findViewById(R.id.lastConnect);


        }
    }

    public void initFriendList(List<Ob_Friend> friendList)
    {
        this.ob_friendList = friendList;
    }

}
