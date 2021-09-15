package com.example.developCall.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.developCall.R;
import com.example.developCall.Search.SearchActivity;

public class HomeFragment extends Fragment {

    ImageView searchbtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchbtn = (ImageView) view.findViewById(R.id.img_btn_search_white);

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(getActivity().getApplicationContext(), SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        return view;
    }
}