package com.example.developCall.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.developCall.Object.ContactModel;
import com.example.developCall.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactAdpater extends RecyclerView.Adapter<ContactAdpater.ViewHolder> {


    public interface AdapterCallback{
        void onItemClicked(HashMap<String,String> list);
    }
    AdapterCallback callback;


    Context context;
    ArrayList<ContactModel> arrayList;
    HashMap<String , String > friendList = new HashMap<>();

    public ContactAdpater(Context context, ArrayList<ContactModel> arrayList , AdapterCallback callback)
    {
        this.context = context;
        this.arrayList = arrayList;
        this.callback = callback;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ContactAdpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.item_contact,parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdpater.ViewHolder holder, int position) {


        ContactModel contactModel = arrayList.get(position);
        holder.tv_name.setText(contactModel.getName());
        holder.tv_number.setText(contactModel.getNumber());


        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!friendList.equals(""))
                {
                    if(isChecked)
                    {
                        friendList.put(contactModel.getNumber(),contactModel.getName());
                    }
                    else
                    {
                        friendList.remove(contactModel.getNumber());
                    }
                }
                if(callback != null)
                {
                    callback.onItemClicked(friendList);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_number;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_number = itemView.findViewById(R.id.tv_number);
            checkBox = itemView.findViewById(R.id.checkBox);



        }
    }
}
