package com.example.developCall.Search;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.developCall.R;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;

public class Search_AddressFragment extends Fragment {

    Context context;

    String searchKey;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Context activity;
    Search_ContactFragment contactFragment;
    Search_ContentFragment contentFragment;
    Search_MemoFragment memoFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.search_result_address, container, false);
        context = container.getContext();
        searchKey =(String) getArguments().getSerializable("searchKey");
        activity = (Context) getArguments().getSerializable("rootActivity");
        TabLayout tabs = (TabLayout) view.findViewById(R.id.tab);
        contactFragment = new Search_ContactFragment(searchKey);
        contentFragment = new Search_ContentFragment(searchKey);
        memoFragment = new Search_MemoFragment(searchKey);



        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.search_tabframe,contactFragment).commit();



        Bundle d = new Bundle();
        d.putSerializable("rootActivity",(Serializable) activity);


        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int position = tab.getPosition();
                fragmentTransaction = fragmentManager.beginTransaction();
                if (position == 0) {
                    contactFragment = new Search_ContactFragment(searchKey);
                    contactFragment.setArguments(d);

                    fragmentTransaction.replace(R.id.search_tabframe, contactFragment).commit();

                } else if (position == 1) {

                    contentFragment = new Search_ContentFragment(searchKey);
                    fragmentTransaction.replace(R.id.search_tabframe, contentFragment).commit();

                } else if (position == 2) {
                    memoFragment = new Search_MemoFragment(searchKey);
                    fragmentTransaction.replace(R.id.search_tabframe, memoFragment).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        return view;
    }
}
