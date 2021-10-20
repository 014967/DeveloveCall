package com.example.developCall.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.developCall.CalendarMenuActivity;
import com.example.developCall.Object.Ob_DetailChat;
import com.example.developCall.Object.Ob_Text;
import com.example.developCall.R;
import com.example.developCall.Service.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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


    com.example.developCall.Service.service service = new serviceImpl();



    public interface OnTextClickListener {
        void onTextClick(List<Ob_Text> data);
    }



    String date;
    String userId;
    String userName;
    String chat_Id;
    String friendId;

    public ChatListAdapter(Context context, List<Ob_DetailChat> data, OnTextClickListener listener, String[] intentArray) {

        this.chatData = data;
        this.context = context;
        this.listener = listener;

        this.date = intentArray[0];
        this.userId = intentArray[1];
        this.userName = intentArray[2];
        this.chat_Id = intentArray[3];
        this.friendId = intentArray[4];




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

            CardView c = view.findViewById(R.id.rightCard);
            c.setElevation(0);


        } else {
            view = layoutInflater.inflate(R.layout.chatcardleft, parent, false);

            CardView c = view.findViewById(R.id.leftCard);
            c.setElevation(0);

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

            try{
                String ss = service.findScheduleFormat(chatData.get(position).getContent());
                String dummy[] = ss.split("_");
                if(dummy[0].equals("NULL"))
                {
                    holder.textView.setText(chatData.get(position).getContent());
                } else
                {
                    SpannableString spannable = new SpannableString(chatData.get(position).getContent());
                    spannable.setSpan(new UnderlineSpan(), 0, chatData.get(position).getContent().length(), 0);
                    holder.textView.setText(spannable);

                    holder.textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Intent intent = new Intent(context, MemoMenuActivity.class);
//                            intent.putExtra("date", date);
//                            intent.putExtra("userId", userId);
//                            intent.putExtra("username",userName);
//                            intent.putExtra("chatId", chat_Id);
//                            intent.putExtra("friendId", friendId);
//                            context.startActivity(intent);
                            Intent intent = new Intent(context, CalendarMenuActivity.class);


//                            Date d =
//                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
//                            String time = simpleDateFormat.format(dummy[0]);
//
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                            SimpleDateFormat newSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");

                            try {
                                Date d = simpleDateFormat.parse(dummy[0]);
                                String filterDate = newSimpleDateFormat.format(d);
                                intent.putExtra("filterDate", filterDate);
                                intent.putExtra("username", userName);
                                context.startActivity(intent);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }

            }
            catch(StringIndexOutOfBoundsException e )
            {
                e.printStackTrace();
            }

         //   holder.textView.setText(chatData.get(position).getContent());
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

                    indexArray = new ArrayList<>();
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