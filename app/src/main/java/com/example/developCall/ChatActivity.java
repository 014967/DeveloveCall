package com.example.developCall;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.DetailChat;
import com.bumptech.glide.Glide;
import com.example.developCall.Adapter.ChatListAdapter;
import com.example.developCall.Adapter.ChatListAdapter.OnTextClickListener;
import com.example.developCall.Object.AmazonTranscription;
import com.example.developCall.Object.Ob_DetailChat;
import com.example.developCall.Object.Ob_Text;
import com.example.developCall.Service.service;
import com.example.developCall.Service.serviceImpl;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChatActivity extends AppCompatActivity implements OnTextClickListener {


    ListView chatListView;
    ChatListAdapter chatListAdapter;

    serviceImpl service = new serviceImpl();

    String st_date;

    ProgressBar loading;
    ImageView btn_back;
    ImageView btn_addMemo;

    AmazonTranscription amazonTranscription = new AmazonTranscription();
    Gson gson = new Gson();
    String[][] modifi;

    List<List<String>> list = new ArrayList<>();
    List<Ob_DetailChat> chatList = new ArrayList<>();
    TextView nonechat;
    TextView username;
    TextView tv_date;


    Date date;
    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat newDateFormat;
    String chatDate;

    //pop
    AlertDialog dialog;
    AlertDialog.Builder dialogBuilder;
    TextView popup_tv_date;
    TextView popup_set_date;


    //
    DatePickerDialog datePickerDialog;

    Calendar myCalendar = Calendar.getInstance();


    String userId;

    SearchView searchView;
    LinearLayout search_support;

    ImageButton btn_up;
    ImageButton btn_down;
    int index = 0;


    CircleImageView img_profile;


    service sv = new serviceImpl();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        loading = findViewById(R.id.loading);


        img_profile = findViewById(R.id.img_profile_01);
        btn_addMemo = findViewById(R.id.addMemo);
        nonechat = findViewById(R.id.noneText);
        btn_back = findViewById(R.id.btn_back);
        username = findViewById(R.id.username);
        tv_date = findViewById(R.id.tv_date);
        searchView = findViewById(R.id.search_view);
        search_support = findViewById(R.id.search_support);
        btn_up = findViewById(R.id.up);
        btn_down = findViewById(R.id.down);

        String st_name = getIntent().getStringExtra("name");

        String chat_Id = getIntent().getStringExtra("chatId");
        String friendId = getIntent().getStringExtra("friendId");
        username.setText(st_name);


        String url = getIntent().getStringExtra("url");

        String imgUrl = getIntent().getStringExtra("imgUrl");





        if (imgUrl != null) {
            String END_POINT = "https://developcallfriendimg.s3.ap-northeast-2.amazonaws.com/";
            Glide.with(getApplicationContext()).load(END_POINT+imgUrl).into(img_profile);
        }


        String expression = "(developcall-transcribe-output.s3.ap-northeast-2.amazonaws.com/)(.*)(.)(.*?)(.json)";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            String[] temp = matcher.group(2).split("_");
            st_date = temp[2];
            simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
            newDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

            try {
                date = simpleDateFormat.parse(st_date);
                chatDate = newDateFormat.format(date);
                tv_date.setText(chatDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                chatListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return false;
            }
        });

        btn_addMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PopupMenu popup = new PopupMenu(getApplicationContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.option_menu, popup.getMenu());
                popup.show();

                popup.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        System.out.println(1);

                        //startActivity(intent);
                        Intent intent = new Intent(getApplicationContext(), CalendarMenuActivity.class);
                        intent.putExtra("username", username.getText().toString());
                        startActivity(intent);

                        return false;
                    }
                });
                popup.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        System.out.println(2);
                        Intent intent = new Intent(getApplicationContext(), MemoMenuActivity.class);
                        intent.putExtra("date", tv_date.getText().toString());
                        intent.putExtra("userId", userId);
                        intent.putExtra("username", username.getText().toString());
                        intent.putExtra("chatId", chat_Id);
                        intent.putExtra("friendId", friendId);
                        startActivity(intent);
                        return false;
                    }
                });


            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        userId = Amplify.Auth.getCurrentUser().getUserId();
        chatListView = findViewById(R.id.chatListView);

        String[] intentArray  = {tv_date.getText().toString(), userId, username.getText().toString(),chat_Id, friendId};
        chatListAdapter = new ChatListAdapter(this, chatList, this::onTextClick, intentArray);
        chatListView.setAdapter(chatListAdapter);


        sv.getChatList(chat_Id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data ->
                {
                    loading.setVisibility(View.GONE);
                    for (DetailChat chat : data.getData()) {

                        Ob_DetailChat ob_detailChat = new Ob_DetailChat();
                        ob_detailChat.setSpk(chat.getSpeakerLabel());
                        ob_detailChat.setContent(chat.getContent());
                        ob_detailChat.setCreated_at(chat.getCreatedAt());
                        chatList.add(ob_detailChat);
                    }

                    chatListAdapter.passData(chatList);
                    chatListAdapter.notifyDataSetChanged();


                });


    }


    @Override
    public void onTextClick(List<Ob_Text> data) {

        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                search_support.setVisibility(View.VISIBLE);

                int max = data.size();


                chatListView.setSelection(0);
                chatListView.setSelection(data.get(index).getIndex());


                btn_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        index++;
                        if (index < max) {
                            chatListView.setSelection(data.get(index).getIndex());

                        } else {
                            index--;
                            chatListView.setSelection(data.get(index).getIndex());
                        }


                    }
                });
                btn_down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (index == 0) {
                            chatListView.setSelection(data.get(index).getIndex());


                        } else {
                            index--;
                        }

                        chatListView.setSelection(data.get(index).getIndex());

                    }
                });

            }
        });

    }
}