package com.example.developCall.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.amplifyframework.core.Amplify;
import com.example.developCall.Alarm.AlarmSet_Fragment;
import com.example.developCall.LoginActivity;
import com.example.developCall.Object.Ob_Friend;
import com.example.developCall.R;
import com.google.android.material.tabs.TabLayout;

public class Fragment2 extends Fragment {

    Fragment fragment1, fragment2, fragment3;
    TextView logout;
    Ob_Friend ob_friend;


    TextView txt_pf_name;
    TextView txt_pf_number;
    TextView txt_category;
    TextView txt_category2;
    TextView tv_edit;


    //
    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        AlarmSet_Fragment alarmSet_fragment = new AlarmSet_Fragment();


        txt_pf_name = view.findViewById(R.id.txt_pf_name);
        txt_pf_number = view.findViewById(R.id.txt_pf_number);
        txt_category = view.findViewById(R.id.txt_category);
        txt_category2 = view.findViewById(R.id.txt_category2);
        tv_edit = view.findViewById(R.id.tv_edit);


        ob_friend = (Ob_Friend) getArguments().getSerializable("ob_friend");
        txt_pf_name.setText(ob_friend.getName());
        txt_pf_number.setText(ob_friend.getNumber());
        txt_category.setText(ob_friend.getGroup());


        txt_category2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction.replace(R.id.home_frame, alarmSet_fragment).commitAllowingStateLoss();
            }
        });

        fragment1 = new TabFragment1();
        fragment2 = new TabFragment2();
        fragment3 = new TabFragment3();

        requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.tabFrame, fragment1).commit();

        TabLayout tabs = (TabLayout) view.findViewById(R.id.tab);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int position = tab.getPosition();

                if (position == 0) {

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabFrame, fragment1).commit();

                } else if (position == 1) {

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabFrame, fragment2).commit();

                } else if (position == 2) {

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabFrame, fragment3).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        logout = (TextView) view.findViewById(R.id.logout);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amplify.Auth.signOut(
                        () -> Log.i("AuthQuickstart", "Signed out successfully"),
                        error -> Log.e("AuthQuickstart", error.toString()));

                Intent in = new Intent(requireActivity().getApplicationContext(), LoginActivity.class);
                requireActivity().startActivity(in);
                requireActivity().finish();
            }
        });

        return view;

    }
}
