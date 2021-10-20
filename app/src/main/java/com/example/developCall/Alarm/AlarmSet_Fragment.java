package com.example.developCall.Alarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.developCall.R;

public class AlarmSet_Fragment extends Fragment {

    Button alarmTimeSet, alarmCycle, alarmCalendar, alarmSetFinish, alarmSetBack;
    Switch pushOnOff;
    int alarmtimenum, alarmcyclenum, alarmcalendarnum, checked;
    FragmentManager alarmSetFragment;
    FragmentTransaction alarmSetTransaction;
    Alarm_Fragment alarm_fragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_setting, container, false);

        alarmTimeSet = (Button) view.findViewById(R.id.alarmtimeset);
        alarmCycle = (Button) view.findViewById(R.id.alarmcycle);
        alarmCalendar = (Button) view.findViewById(R.id.calendartimeset);
        alarmSetFinish = (Button) view.findViewById(R.id.setfinish);
        alarmSetBack = (Button) view.findViewById(R.id.alarmsetback);
        pushOnOff = (Switch) view.findViewById(R.id.pushonoff);

        checked = 1;
        alarmtimenum = 15;
        alarmcyclenum = 2;
        alarmcalendarnum = 2;

        pushOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    alarmTimeSet.setEnabled(true);
                    alarmCycle.setEnabled(true);
                    alarmCalendar.setEnabled(true);
                }
                else {
                    alarmTimeSet.setEnabled(false);
                    alarmCycle.setEnabled(false);
                    alarmCalendar.setEnabled(false);
                }
            }
        });

        alarmTimeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), Alarm_TimePopUp.class);
                startActivityResult.launch(intent);
            }
        });

        alarmCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), Alarm_CyclePopUp.class);
                startActivityResult.launch(intent);
            }
        });

        alarmCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), Alarm_CalendarPopUp.class);
                startActivityResult.launch(intent);
            }
        });

        alarmSetFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmSetTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                alarm_fragment = new Alarm_Fragment();
                Bundle bundle = new Bundle();
                if(pushOnOff.isChecked()){
                    checked = 1;
                }
                else{
                    checked = 2;
                }
                bundle.putInt("onoff",checked);
                bundle.putInt("timeresult", alarmtimenum);
                bundle.putInt("cycleresult", alarmcyclenum);
                bundle.putInt("calendarresult", alarmcalendarnum);
                alarm_fragment.setArguments(bundle);
                alarmSetTransaction.replace(R.id.home_frame,alarm_fragment);
                alarmSetTransaction.commit();
            }
        });

        alarmSetBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmSetTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                alarm_fragment = new Alarm_Fragment();
                alarmSetTransaction.replace(R.id.home_frame,alarm_fragment);
                alarmSetTransaction.commit();
            }
        });

        return view;
    }

    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == 1){
                        Intent intent = result.getData();
                        String alarmtimeget = intent.getStringExtra("timeresult");
                        alarmtimenum = Integer.parseInt(alarmtimeget);

                        alarmTimeSet.setText(alarmtimeget + ":00");
                    }
                    else if(result.getResultCode() == 2){
                        Intent intent = result.getData();
                        String alarmcycleget = intent.getStringExtra("cycleresult");
                        String alarmcyclestr = intent.getStringExtra("cyclestr");
                        alarmcyclenum = Integer.parseInt(alarmcycleget);

                        alarmCycle.setText(alarmcyclestr + " 마다");
                    }
                    else if(result.getResultCode() == 3){
                        Intent intent = result.getData();
                        String alarmcalendarget = intent.getStringExtra("calendarresult");
                        String alarmcaldendarstr = intent.getStringExtra("calendarstr");
                        alarmcalendarnum = Integer.parseInt(alarmcalendarget);

                        alarmCalendar.setText(alarmcaldendarstr);
                    }
                }
            });
}
