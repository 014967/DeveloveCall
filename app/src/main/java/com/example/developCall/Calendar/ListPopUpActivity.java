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

    EditText name, number;
    Button rewrite, delete, close;
    String listtitle, listnumber;
    int listposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.listview_popup);

        Intent intent = getIntent();
        listtitle = intent.getStringExtra("name");
        listnumber = intent.getStringExtra("number");
        listposition = intent.getIntExtra("position",0);

        name = (EditText)findViewById(R.id.listview_title);
        number = (EditText)findViewById(R.id.listview_content);
        rewrite = (Button)findViewById(R.id.rewrite);
        delete = (Button)findViewById(R.id.delete);
        close = (Button)findViewById(R.id.close);

        String test = String.valueOf(listposition);
        Log.d("tag",test);

        name.setText(listtitle);
        number.setText(listnumber);

        rewrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listtitle = name.getText().toString();
                listnumber = number.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("name", listtitle);
                intent.putExtra("number", listnumber);
                intent.putExtra("position", listposition);
                setResult(1, intent);

                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(2, intent);
                intent.putExtra("position", listposition);
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