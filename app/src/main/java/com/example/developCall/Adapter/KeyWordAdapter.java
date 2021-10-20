package com.example.developCall.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.developCall.Object.Ob_Chat;
import com.example.developCall.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class KeyWordAdapter extends RecyclerView.Adapter<KeyWordAdapter.ViewHolder> {

    List<Ob_Chat> keyWordList = new ArrayList<>();
    @NonNull
    @Override
    public KeyWordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_keyword ,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KeyWordAdapter.ViewHolder holder, int position) {

        Ob_Chat chat = keyWordList.get(position);
        holder.keyWord.setText(chat.getKeyWord());
    }

    @Override
    public int getItemCount() {
        return keyWordList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView keyWord;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            keyWord = itemView.findViewById(R.id.keyWord);
        }
    }

    public void initList(List<Ob_Chat> keyWordList)
    {
        this.keyWordList = keyWordList;

    }
}
