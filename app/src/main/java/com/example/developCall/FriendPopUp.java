package com.example.developCall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Friend;
import com.amplifyframework.datastore.generated.model.Group;
import com.amplifyframework.datastore.generated.model.User;
import com.example.developCall.Adapter.FriendListPopupAdapter;
import com.example.developCall.Object.Ob_Friend;
import com.example.developCall.Service.serviceImpl;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FriendPopUp extends Activity implements FriendListPopupAdapter.OnTextClickListener {


    RecyclerView friendRecyclerView;
    SearchView friendSearchView;
    String userId;
    FriendListPopupAdapter friendListAdapter;
    ProgressBar loading;


    ArrayList<Ob_Friend> ob = new ArrayList<>();

    com.example.developCall.Service.service service = new serviceImpl();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendpopup);
        loading = findViewById(R.id.loading);


        friendRecyclerView = findViewById(R.id.friendRv);
        friendSearchView = findViewById(R.id.friendSv);
        userId = Amplify.Auth.getCurrentUser().getUserId();

        friendRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        friendListAdapter = new FriendListPopupAdapter(ob,this::onTextClick);
        friendRecyclerView.setAdapter(friendListAdapter);



        friendSearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        friendSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                friendListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals(""))
                {
                    friendListAdapter.setFriendListArray(ob);
                    friendListAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });

        ob = new ArrayList<>();
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
                                        Ob_Friend ob_friend = new Ob_Friend();
                                        ob_friend.setId(friend.getId());
                                        ob_friend.setName(friend.getName());
                                        ob_friend.setFriendImg(friend.getFriendImg());
                                        ob.add(ob_friend);
                                    }
                                }
                            }

                            loading.setVisibility(View.INVISIBLE);
                            friendListAdapter.setFriendListArray(ob);
                            friendListAdapter.notifyDataSetChanged();



                        }
                        , error ->
                        {
                        });


    }

    @Override
    public void onTextClick(Ob_Friend friend) {

        Intent intent = new Intent();
        intent.putExtra("friendName", friend.getName());
        //intent.putExtra("name", addname);
        setResult(RESULT_OK, intent);
        finish();

    }
}
