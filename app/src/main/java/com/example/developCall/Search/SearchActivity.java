package com.example.developCall.Search;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.developCall.R;

public class SearchActivity extends AppCompatActivity {

    FragmentManager searchFragment;
    FragmentTransaction searchTransaction;
    Search_HistoryFragment search_historyFragment;
    Search_AddressFragment search_addressFragment;
    ImageView iv_back;


    EditText searchContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        searchFragment = getSupportFragmentManager();
        searchTransaction = searchFragment.beginTransaction();
        search_historyFragment = new Search_HistoryFragment();
        search_addressFragment = new Search_AddressFragment();

        searchTransaction.replace(R.id.search_frame, search_historyFragment).commitAllowingStateLoss();

        searchContents = (EditText) findViewById(R.id.search_contents);
        iv_back = findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });
        searchContents.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    searchTransaction = searchFragment.beginTransaction();
                    searchTransaction.replace(R.id.search_frame, search_addressFragment).commitAllowingStateLoss();
                }
                return false;
            }
        });
    }
}
