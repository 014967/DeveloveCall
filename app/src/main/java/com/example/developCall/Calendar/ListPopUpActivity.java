package com.example.developCall.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.developCall.R;

public class ListPopUpActivity extends Activity {

    EditText title, name;
    Button rewrite, delete, close;
    String listtitle, listname;
    int listposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.listview_popup);

        Intent intent = getIntent();
        listtitle = intent.getStringExtra("title");
        listname = intent.getStringExtra("name");
        listposition = intent.getIntExtra("position",0);

        title = (EditText)findViewById(R.id.listview_title);
        name = (EditText)findViewById(R.id.listview_content);
        rewrite = (Button)findViewById(R.id.rewrite);
        delete = (Button)findViewById(R.id.delete);
        close = (Button)findViewById(R.id.close);

        String test = String.valueOf(listposition);
        Log.d("tag",test);

        title.setText(listtitle);
        name.setText(listname);

        rewrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtitle = title.getText().toString();
                listname = name.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("title", listtitle);
                intent.putExtra("name", listname);
                intent.putExtra("position", listposition);
                setResult(1, intent);

                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(2, new Intent().putExtra("position", listposition));
                finish();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

}