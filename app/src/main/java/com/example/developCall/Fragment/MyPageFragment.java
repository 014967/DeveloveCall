package com.example.developCall.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.User;
import com.example.developCall.Alarm.AlarmSet_Fragment;
import com.example.developCall.LoginActivity;
import com.example.developCall.R;

public class MyPageFragment extends Fragment {


    TextView tv_faq;
    TextView tv_inquiry;
    LinearLayout txt_notice;
    TextView alarm_Setting;
    TextView tv_logout;
    TextView txt_user_name;

    String username;
    String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mypage, container, false);

        tv_faq = view.findViewById(R.id.FAQ);
        tv_inquiry = view.findViewById(R.id.onebyone);
        txt_notice = view.findViewById(R.id.txt_notice);
        alarm_Setting = view.findViewById(R.id.alarmSetting);
        tv_logout = view.findViewById(R.id.tv_logout);
        txt_user_name = view.findViewById(R.id.txt_user_name);


        userId = Amplify.Auth.getCurrentUser().getUserId();

        getUser(userId);


        tv_faq.setOnClickListener(new View.OnClickListener() { //faq
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FAQFragment faqFragment = new FAQFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, faqFragment).addToBackStack(null).commit();
            }
        });
        tv_inquiry.setOnClickListener(new View.OnClickListener() { //1대1
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Personal_Inquiry personal_inquiry = new Personal_Inquiry();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, personal_inquiry).addToBackStack(null).commit();
            }
        });
        txt_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                NoticeFragment noticeFragment = new NoticeFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, noticeFragment).addToBackStack(null).commit();

            }
        });
        alarm_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                AlarmSet_Fragment alarmSet_fragment = new AlarmSet_Fragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, alarmSet_fragment).addToBackStack(null).commit();

            }
        });

        tv_logout.setOnClickListener(new View.OnClickListener() {
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

    public void getUser(String userId) {
        Amplify.API.query(
                ModelQuery.list(User.class, User.ID.contains(userId)),
                response ->
                {
                    for (User user : response.getData()) {
                        username = user.getName();

                    }

                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt_user_name.setText(username);
                        }
                    });
                },
                error ->
                {

                }

        );
    }

}
