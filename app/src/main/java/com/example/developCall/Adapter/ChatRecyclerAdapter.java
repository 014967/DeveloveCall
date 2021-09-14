package com.example.developCall.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.developCall.R;

import java.util.ArrayList;
import java.util.List;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder> {

    List<List<String>> data = new ArrayList<>();
    Context context;


    public ChatRecyclerAdapter(Context context, List<List<String>> data) {

        this.data = data;
        this.context = context;
    }


    @Override
    public int getItemViewType(int position) {


        if (data.get(position).get(0).equals("spk_0")) {
            return 1;
        } else {
            return 2;

        }


    }

    @NonNull
    @Override

    public ChatRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == 1) {

            view = layoutInflater.inflate(R.layout.chatcardright, parent, false);
        } else {
            view = layoutInflater.inflate(R.layout.chatcardleft, parent, false);
        }


        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ChatRecyclerAdapter.ViewHolder holder, int position) {


        holder.textView.setText(data.get(position).get(1));


    }

    @Override
    public int getItemCount() {
        if (data.size() == 0) {
            return 0;
        } else {
            return data.size();

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView textView;

        public ViewHolder(@NonNull View itemView) {


            super(itemView);
            textView = itemView.findViewById(R.id.chatText);


        }
    }

    public void initData(List<List<String>> newData) {
        this.data = newData;
    }

}
