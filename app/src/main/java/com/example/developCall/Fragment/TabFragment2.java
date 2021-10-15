package com.example.developCall.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils;
import com.amplifyframework.api.graphql.GraphQLResponse;
import com.amplifyframework.api.graphql.PaginatedResult;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Chat;
import com.example.developCall.Adapter.MemoAdapter;
import com.example.developCall.Object.Ob_Chat;
import com.example.developCall.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class TabFragment2 extends Fragment {
    ArrayList<Ob_Chat> profileDataList;


    String userId;
    String friendId;
    MemoAdapter memoAdapter;

    public TabFragment2() {
    }

    public TabFragment2(String userId, String friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab2, container, false);

        this.InitializeProfileData();

        String username = getArguments().getSerializable("name").toString();
        ListView listView = (ListView) view.findViewById(R.id.listView2);
        memoAdapter = new MemoAdapter(getActivity(), profileDataList, username);
        listView.setAdapter(memoAdapter);
//

        return view;
    }

    public void InitializeProfileData() {
        profileDataList = new ArrayList<Ob_Chat>();

        Amplify.API.query( // Chat 클래스에서 찾아야함.
                ModelQuery.list(Chat.class, Chat.USER_ID.contains(userId)),
                response ->
                {
                    try {
                        profileDataList = addChatList(response, friendId);
                        Collections.sort(profileDataList, new Comparator<Ob_Chat>() {
                            @Override
                            public int compare(Ob_Chat lDate, Ob_Chat rDate) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                                Date leftDate = null;
                                Date rightDate = null;


                                try {
                                    leftDate = dateFormat.parse(lDate.getDate());
                                    rightDate = dateFormat.parse(rDate.getDate());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                                return leftDate.getTime() > rightDate.getTime() ? -1 : (leftDate.getTime() < rightDate.getTime()) ? 1 : 0;
                            }
                        });


                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                memoAdapter.setSample(profileDataList);
                                memoAdapter.notifyDataSetChanged();

                            }
                        });
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                , error ->
                {
                }

        );


    }

    public ArrayList<Ob_Chat> addChatList(GraphQLResponse<PaginatedResult<Chat>> response, String friendId) throws ParseException {
        for (Chat chat : response.getData()) {
            if (chat.getFriendId().equals(friendId)) {

                if (!chat.getMemo().equals("")) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
                    SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                    Date date = simpleDateFormat.parse(chat.getDate());
                    String chatDate = newDateFormat.format(date);


                    Ob_Chat ob_chat = new Ob_Chat();
                    ob_chat.setDate(chatDate);
                    ob_chat.setFriendID(chat.getFriendId());
                    ob_chat.setKeyWord(chat.getKeyWord());
                    ob_chat.setMemo(chat.getMemo());
                    ob_chat.setS3_url(chat.getS3Url());
                    ob_chat.setSummary(chat.getSummary());
                    ob_chat.setUserID(chat.getUserId());
                    ob_chat.setId(chat.getId());
                    profileDataList.add(ob_chat);
                }


            }
        }

        return profileDataList;
    }


}
