package com.example.developCall.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.developCall.FriendPopUp;
import com.example.developCall.R;

public class AddPopUpActivity extends Activity {

    EditText name, title;
    Button finish, addclose;
    String addname, addtitle;

    EditText content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_layout);

        title = (EditText)findViewById(R.id.title);
        name = (EditText)findViewById(R.id.content);
        finish = (Button)findViewById(R.id.finish);
        addclose = (Button)findViewById(R.id.addclose);
        content = (EditText)findViewById(R.id.content);


        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FriendPopUp.class);
                startActivity(intent);
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtitle = title.getText().toString();
                addname = name.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("title", addtitle);
                intent.putExtra("name", addname);
                setResult(RESULT_OK, intent);

                finish();
            }
        });

        addclose.setOnClickListener(new View.OnClickListener() {
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