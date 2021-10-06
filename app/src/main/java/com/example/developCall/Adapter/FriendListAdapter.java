package com.example.developCall.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Filter;
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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {


    List<Ob_Friend> friendListArray = new ArrayList<>();

    List<Ob_Friend> filterFriend;
    List<Ob_Friend> tempFriend;


    String charString;
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
        String END_POINT = "https://developcallfriendimg.s3.ap-northeast-2.amazonaws.com/";


        if(friend.getFriendImg() != null)
        {
            String url = END_POINT+friend.getFriendImg();
            Glide.with(holder.itemView.getContext()).load(url).into(holder.friendImg);

        }


        holder.rv_friendItem.setOnClickListener(new View.OnClickListener() {
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


    public Filter getFilter()
    {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                charString = constraint.toString();


                if(charString.isEmpty())
                {
                    filterFriend = friendListArray;

                }
                else
                {
                    List<Ob_Friend> filteringList= new ArrayList<>();
                    for(Ob_Friend friend : friendListArray)
                    {
                        if(friend.getName().contains(charString))
                        {
                            filteringList.add(friend);
                        }
                    }
                    filterFriend = filteringList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterFriend;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {


                friendListArray = (List<Ob_Friend>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public void setFriendListArray(List<Ob_Friend> friendList)
    {
        this.friendListArray = friendList;
    }

}
