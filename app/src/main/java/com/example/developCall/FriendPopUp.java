package com.example.developCall;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Friend;
import com.amplifyframework.datastore.generated.model.Group;
import com.amplifyframework.datastore.generated.model.User;
import com.example.developCall.Object.Ob_Friend;
import com.example.developCall.Service.serviceImpl;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FriendPopUp extends Activity {


    RecyclerView friendRecyclerView;
    SearchView friendSearchView;
    String userId;

    com.example.developCall.Service.service service = new serviceImpl();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendpopup);

        friendRecyclerView = findViewById(R.id.friendRecyclerView);
        friendSearchView = findViewById(R.id.friendSv);
        userId = Amplify.Auth.getCurrentUser().getUserId();

        ArrayList<Ob_Friend> ob = new ArrayList<>();
        service.getFriendList(userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data ->
                        {
                            for(User user: data.getData())
                            {
                                for(Group group : user.getGroup())
                                {
                                    for(Friend friend : group.getFriend())
                                    {
                                        Ob_Friend ob_friend = new Ob_Friend
                                    }
                                }
                            }
                        }
                        , error ->
                        {
                        });


    }
}
