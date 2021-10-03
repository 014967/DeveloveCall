package com.example.developCall.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.developCall.Object.Ob_Chat;
import com.example.developCall.R;

import java.util.ArrayList;

public class TabFragment3 extends Fragment {
    ArrayList<Ob_Chat> profileDataList;


    public TabFragment3()
    {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab3, container, false);

        this.InitializeProfileData();

        ListView listView = (ListView)view.findViewById(R.id.listView3);
//        final ListAdapter myAdapter = new ListAdapter(getActivity(),profileDataList);
//
//        listView.setAdapter(myAdapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView parent, View v, int position, long id){
////                Toast.makeText(requireActivity().getApplicationContext(),
////                        myAdapter.getItem(position).getName(),
////                        Toast.LENGTH_LONG).show();
//            }
//        });

        return view;
    }

    public void InitializeProfileData()
    {
        profileDataList = new ArrayList<Ob_Chat>();


    }
}
