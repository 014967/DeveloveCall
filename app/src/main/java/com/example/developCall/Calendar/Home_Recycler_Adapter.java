package com.example.developCall.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.developCall.R;

import java.util.ArrayList;

public class Home_Recycler_Adapter extends RecyclerView.Adapter<Home_Recycler_Adapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView data_title;
        TextView data_target;
        TextView data_time;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            data_title = (TextView) itemView.findViewById(R.id.data_plan_title);
            data_target = (TextView) itemView.findViewById(R.id.data_plan_target);
            data_time = (TextView) itemView.findViewById(R.id.data_plan_time);
        }
    }

    private ArrayList<Home_Recycler_Data> mList = null;

    public Home_Recycler_Adapter(ArrayList<Home_Recycler_Data> mList){
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.home_recycler_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Home_Recycler_Data item = mList.get(position);

        holder.data_title.setText(item.getRecycleName());   // 사진 없어서 기본 파일로 이미지 띄움
        holder.data_target.setText(item.getRecycleTarget());
        holder.data_time.setText(item.getRecycleTime());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
