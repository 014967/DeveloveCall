package com.example.developCall.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.developCall.Object.Ob_DetailChat;
import com.example.developCall.Object.Ob_Text;
import com.example.developCall.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChatListAdapter extends BaseAdapter implements Filterable {

    List<List<String>> data = new ArrayList<>();
    List<Ob_DetailChat> chatData = new ArrayList<>();
    Context context;
    RecyclerView recyclerView;
    int count = 0;
    ViewHolder holder;

    List<Ob_Text> indexArray = new ArrayList<>();
    String charString;

    OnTextClickListener listener;

    public interface OnTextClickListener {
        void onTextClick(List<Ob_Text> data);
    }


    public ChatListAdapter(Context context, List<Ob_DetailChat> data, OnTextClickListener listener) {

        this.chatData = data;
        this.context = context;
        this.listener = listener;


    }


    @Override
    public int getCount() {
        return chatData.size();
    }

    @Override
    public Object getItem(int position) {
        return chatData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        int viewType = getItemViewType(position);
        if (viewType == 1) {

            view = layoutInflater.inflate(R.layout.chatcardright, parent, false);


        } else {
            view = layoutInflater.inflate(R.layout.chatcardleft, parent, false);


        }
        holder = new ViewHolder();
        holder.textView = (TextView) view.findViewById(R.id.chatText);


        if (charString != null) {
            int startIndex = chatData.get(position).getContent().indexOf(charString);
            int stopIndex = startIndex + charString.length();
            if (startIndex != -1) {
                Spannable spannable = new SpannableString(chatData.get(position).getContent());
                ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.BLUE});
                TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                spannable.setSpan(highlightSpan, startIndex, stopIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.textView.setText(spannable);
            } else {
                holder.textView.setText(chatData.get(position).getContent());
            }
        } else {
            holder.textView.setText(chatData.get(position).getContent());
        }
        return view;
    }

    @Override
    public int getItemViewType(int position) {

        if (chatData.get(position).getSpk().equals("spk_0")) {
            return 1;
        } else {
            return 2;
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {



                charString = constraint.toString();
                if (charString.isEmpty()) {

                } else {

                    for (Ob_DetailChat ob : chatData) {

                        if (ob.getContent().contains(charString)) {
                            int pos = chatData.indexOf(ob);


                            int startIndex = ob.getContent().indexOf(charString);
                            int stopIndex = startIndex + charString.length();
                            Ob_Text ob_text = new Ob_Text();
                            ob_text.setIndex(pos);
                            ob_text.setStart(startIndex);
                            ob_text.setEnd(stopIndex);


                            if (indexArray.indexOf(ob) == -1) {
                                indexArray.add(ob_text);
                            }


                        }
                    }

                    listener.onTextClick(indexArray);
                }
                return null;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

            }
        };
    }

    public class ViewHolder {

        TextView textView;

    }


    public void passData(List<Ob_DetailChat> data) {
        this.chatData = data;
        Collections.sort(chatData, new Comparator<Ob_DetailChat>() {
            @Override
            public int compare(Ob_DetailChat o1, Ob_DetailChat o2) {
                if (o1.getCreated_at().compareTo(o2.getCreated_at()) > 0) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });


    }


}