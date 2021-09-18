package com.example.developCall.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.developCall.Fragment.FriendListFragment;
import com.example.developCall.Object.Ob_Group;
import com.example.developCall.R;

import java.util.ArrayList;
import java.util.List;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {


    List<Ob_Group> list_group= new ArrayList<>();


    public GroupListAdapter(List<Ob_Group> list_group) {
        this.list_group.add(0,null);
        this.list_group = list_group;



    }

    @NonNull
    @Override
    public GroupListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_group, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Ob_Group ob_group = list_group.get(position);
        holder.groupName.setText(ob_group.getName());
        Bundle bundle = new Bundle();
        bundle.putSerializable("ob_group", ob_group);
        holder.item_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                FriendListFragment friendListFragment = new FriendListFragment();
                friendListFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, friendListFragment).addToBackStack(null).commit();


            }
        });

    }


    @Override
    public int getItemCount() {
        return list_group.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView groupName;
        LinearLayout item_group;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.group_name);
            item_group = itemView.findViewById(R.id.item_group);

        }

    }

    public void setList_group(List<Ob_Group> list_group)
    {
        this.list_group = list_group;

    }

}
