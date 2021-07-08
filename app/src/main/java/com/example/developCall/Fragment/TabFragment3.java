package com.example.developCall.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.developCall.Adapter.ListAdapter;
import com.example.developCall.Object.ListData;
import com.example.developCall.R;

import java.util.ArrayList;

public class TabFragment3 extends Fragment {
    ArrayList<ListData> profileDataList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab3, container, false);

        this.InitializeProfileData();

        ListView listView = (ListView)view.findViewById(R.id.listView3);
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

        profileDataList.add(new ListData(R.drawable.common_google_signin_btn_icon_dark, "기타인데","뭘적어나야할까"));
        profileDataList.add(new ListData(R.drawable.common_google_signin_btn_icon_light, "정말로","고민이야"));
    }
}
