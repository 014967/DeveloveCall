package com.example.developCall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class TabFragment2 extends Fragment {
    ArrayList<ListData> profileDataList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab2, container, false);

        this.InitializeProfileData();

        ListView listView = (ListView)view.findViewById(R.id.listView2);
        final ListAdapter myAdapter = new ListAdapter(getActivity(),profileDataList);

        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Toast.makeText(requireActivity().getApplicationContext(),
                        myAdapter.getItem(position).getName(),
                        Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    public void InitializeProfileData()
    {
        profileDataList = new ArrayList<ListData>();

        profileDataList.add(new ListData(R.drawable.common_google_signin_btn_icon_dark, "메모1","배고파"));
        profileDataList.add(new ListData(R.drawable.common_google_signin_btn_icon_light, "메모2","치킨ㄱ?"));
    }
}