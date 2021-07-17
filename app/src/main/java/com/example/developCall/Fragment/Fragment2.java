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

import com.amplifyframework.core.Amplify;
import com.example.developCall.LoginActivity;
import com.example.developCall.R;
import com.google.android.material.tabs.TabLayout;

import static java.util.Objects.requireNonNull;

public class Fragment2 extends Fragment {

    Fragment fragment1, fragment2, fragment3;
    TextView logout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile, container, false);


        fragment1 = new TabFragment1();
        fragment2 = new TabFragment2();
        fragment3 = new TabFragment3();

        requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.tabFrame, fragment1).commit();

        TabLayout tabs = (TabLayout)view.findViewById(R.id.tab);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int position = tab.getPosition();

                if(position == 0){

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabFrame, fragment1).commit();

                }else if (position == 1){

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabFrame, fragment2).commit();

                }else if (position == 2) {

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

        logout = (TextView)view.findViewById(R.id.logout);


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
