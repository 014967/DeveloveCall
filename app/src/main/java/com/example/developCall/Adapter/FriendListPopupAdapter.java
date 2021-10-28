package com.example.developCall.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.developCall.Object.Ob_Friend;
import com.example.developCall.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendListPopupAdapter extends RecyclerView.Adapter<FriendListPopupAdapter.ViewHolder> {

    List<Ob_Friend> friendListArray = new ArrayList<>();
    List<Ob_Friend> filterFriend;
    OnTextClickListener listener;

    private int selectedPos = RecyclerView.NO_POSITION;

    String charString;
    public FriendListPopupAdapter(List<Ob_Friend> friendList, OnTextClickListener listener)
    {
        this.friendListArray = friendList;
        this.listener = listener;
    }


    public interface OnTextClickListener {
        void onTextClick(Ob_Friend friend);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setSelected(selectedPos == position);

        Ob_Friend friend = friendListArray.get(position);
        holder.txt_friendName.setText(friend.getName());
        String END_POINT = "https://developcallfriendimg.s3.ap-northeast-2.amazonaws.com/";
        if(friend.getFriendImg() != null)
        {
            String url = END_POINT+friend.getFriendImg();
            Glide.with(holder.itemView.getContext()).load(url).into(holder.iv_friend);

        }

        holder.rv_friendItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(selectedPos);
                selectedPos = holder.getLayoutPosition();
                notifyItemChanged(selectedPos);
                listener.onTextClick(friend);
            }
        });

    }

    @Override
    public int getItemCount() {
        return friendListArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView txt_friendName;
        CircleImageView iv_friend;
        RelativeLayout rv_friendItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            rv_friendItem = itemView.findViewById(R.id.rv_friendItem);
            iv_friend = itemView.findViewById(R.id.img_profile_03);
            txt_friendName = itemView.findViewById(R.id.search_result_adrress_name_01);



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
