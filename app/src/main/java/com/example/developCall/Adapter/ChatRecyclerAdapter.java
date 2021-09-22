package com.example.developCall.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.developCall.R;

import java.util.ArrayList;
import java.util.List;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder> implements Filterable {

    List<List<String>> data = new ArrayList<>();
    Context context;
    RecyclerView recyclerView;
    int count = 0;

    List<Integer> indexArray = new ArrayList<>();


    OnTextClickListener listener;

    public interface OnTextClickListener {
        void onTextClick(List<Integer> data);
    }


    public ChatRecyclerAdapter(Context context, List<List<String>> data, OnTextClickListener listener ) {

        this.data = data;
        this.context = context;
        this.listener = listener;


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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty())
                {

                }else
                {

                    for(List<String> list : data)
                    {

                        if(list.get(1).contains(charString))
                        {
                                int pos = data.indexOf(list);
                                //System.out.println(list.get(1));
                                //System.out.println(pos);


                            indexArray.add(pos);


//                            ThreadUtils.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    recyclerView.getLayoutManager().scrollToPosition(pos);
//                                }
//                            });

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
