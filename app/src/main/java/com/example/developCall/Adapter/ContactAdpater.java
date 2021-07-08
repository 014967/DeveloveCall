package com.example.developCall.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.developCall.Object.ContactModel;
import com.example.developCall.R;

import java.util.ArrayList;

public class ContactAdpater extends RecyclerView.Adapter<ContactAdpater.ViewHodler> {


    Context context;
    ArrayList<ContactModel> arrayList;

    public ContactAdpater(Context context, ArrayList<ContactModel> arrayList)
    {
        this.context = context;
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ContactAdpater.ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.item_contact,parent, false);

        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdpater.ViewHodler holder, int position) {


        ContactModel contactModel = arrayList.get(position);
        holder.tv_name.setText(contactModel.getName());
        holder.tv_number.setText(contactModel.getNumber());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_number;
        CheckBox checkBox;

        public ViewHodler(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_number = itemView.findViewById(R.id.tv_number);
            checkBox = itemView.findViewById(R.id.checkBox);

        }
    }
}
