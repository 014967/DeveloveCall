package com.example.developCall.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils;
import com.amplifyframework.api.graphql.GraphQLResponse;
import com.amplifyframework.api.graphql.PaginatedResult;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Friend;
import com.amplifyframework.datastore.generated.model.Group;
import com.amplifyframework.datastore.generated.model.User;
import com.example.developCall.Adapter.Home_FriendListAdapter;
import com.example.developCall.Object.Ob_Friend;
import com.example.developCall.R;
import com.example.developCall.Search.SearchActivity;
import com.example.developCall.Service.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeFragment extends Fragment {


    ImageView imageView;
    TextView txt_user_name;
    String username;
    String userId;

    RecyclerView home_rv_friend;
    Home_FriendListAdapter friendListAdapter;
    List<Ob_Friend> friendListArray;


    ImageView searchbtn;

    serviceImpl service = new serviceImpl();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchbtn = (ImageView) view.findViewById(R.id.img_btn_search_white);

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(getActivity().getApplicationContext(), SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        imageView = view.findViewById(R.id.img_btn_search_white);
        txt_user_name = view.findViewById(R.id.txt_user_name);
        home_rv_friend = view.findViewById(R.id.home_rv_friend);
        userId = Amplify.Auth.getCurrentUser().getUserId();
        friendListArray = new ArrayList<>();
        friendListAdapter = new Home_FriendListAdapter(friendListArray);
        service.getUserName(userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data ->
                {

                    for(User user : data.getData())
                    {
                        username = user.getName();
                        txt_user_name.setText(username);
                        for(Group group : user.getGroup())
                        {
                            for(Friend friend: group.getFriend())
                            {
                                Ob_Friend ob_friend = new Ob_Friend();
                                ob_friend.setName(friend.getName());
                                ob_friend.setFriendImg(friend.getFriendImg());
                                friendListArray.add(ob_friend);
                            }

                        }

                    }
                    friendListAdapter.setFriendListArray(friendListArray);
                    friendListAdapter.notifyDataSetChanged();

                }, error ->
                {
                    System.out.println("유저 이름이 없습니다");
                });


/*

        ArrayList<Ob_lastCall> ob = new ArrayList<>();
        service.getData(userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data ->
                {
                    for (User user : data.getData()) {
                        for (Group group : user.getGroup()) {
                            for (Friend friend : group.getFriend()) {
                                Ob_lastCall ob_lastCall = new Ob_lastCall();
                                ob_lastCall.setFriendName(friend.getName());
                                ob_lastCall.setLastCall(friend.getLastContact());
                                ob.add(ob_lastCall);

                            }
                        }
                    }
                    // 0 { friendName : ddd, lastcall : 13092021210130 } 1 { ~~~~ 각 배열마다 이름이랑 마지막 통화시간 넣어놧어
                    //용학아 여기서 데이터 이용해야해
                    System.out.println("96" + Arrays.asList(ob));

                }, error ->
                {

                });
*/


        home_rv_friend.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        home_rv_friend.setAdapter(friendListAdapter);
       // setUserAndFriend(userId);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), SearchActivity.class);
                startActivity(in);

            }
        });


        return view;
    }


    public void setUserAndFriend(String userId) {


        Amplify.API.query(
                ModelQuery.list(User.class, User.ID.contains(userId)),
                response ->
                {
                    friendListArray = new ArrayList<>();
                    friendListArray = addFriendList(response);
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            txt_user_name.setText(username);
                            friendListAdapter.setFriendListArray(friendListArray);
                            friendListAdapter.notifyDataSetChanged();


                        }
                    });

                }
                , error ->
                {

                }

        );

    }

    private List<Ob_Friend> addFriendList(GraphQLResponse<PaginatedResult<User>> response) {
        for (User user : response.getData()) {
            username = user.getName();
            for (Group group : user.getGroup()) {
                for (Friend friend : group.getFriend()) {
                    Ob_Friend ob_friend = new Ob_Friend();
                    ob_friend.setName(friend.getName());
                    ob_friend.setFriendImg(friend.getFriendImg());
                    friendListArray.add(ob_friend);
                }
            }

        }

        return friendListArray;
    }

}



