package com.example.developCall.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.developCall.Fragment.Fragment2;
import com.example.developCall.Object.Ob_Friend;
import com.example.developCall.R;

import java.util.ArrayList;
import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {


    List<Ob_Friend> friendListArray = new ArrayList<>();

    public FriendListAdapter(List<Ob_Friend> friendListArray) {
        this.friendListArray = friendListArray;
    }

    @NonNull
    @Override
    public FriendListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListAdapter.ViewHolder holder, int position) {


        Ob_Friend friend = friendListArray.get(position);
        holder.friendName.setText(friend.getName());
        Bundle bundle = new Bundle();
        bundle.putSerializable("ob_friend", friend);
        holder.friendName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment2 profile = new Fragment2();
                profile.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, profile).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return friendListArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView friendImg;
        TextView friendName;
        TextView lastConnect;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progressBar);
            friendImg = itemView.findViewById(R.id.friendImg);
            friendName = itemView.findViewById(R.id.friendName);
            lastConnect = itemView.findViewById(R.id.lastConnect);
        }


    }


    public void setFriendListArray(List<Ob_Friend> friendList)
    {
        this.friendListArray = friendList;
    }

}
