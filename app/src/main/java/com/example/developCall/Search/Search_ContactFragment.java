package com.example.developCall.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Friend;
import com.amplifyframework.datastore.generated.model.Group;
import com.amplifyframework.datastore.generated.model.User;
import com.example.developCall.Object.Ob_Friend;
import com.example.developCall.R;
import com.example.developCall.Service.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Search_ContactFragment extends Fragment {


    String searchKey;

    RecyclerView recyclerView;
    Search_ContactAdapter contactAdapter;
    List<Ob_Friend> ob_friendList;
    com.example.developCall.Service.service service = new serviceImpl();

    String userId;


    public Search_ContactFragment(String searchKey) {
        // Required empty public constructor

        this.searchKey = searchKey;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search__contact, container, false);


        ob_friendList  = new ArrayList<>();


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        contactAdapter = new Search_ContactAdapter(ob_friendList);
        recyclerView.setAdapter(contactAdapter);


        userId = Amplify.Auth.getCurrentUser().getUserId();
        service.getFriendList(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data ->
                        {
                            for (User user : data.getData()) {
                                for (Group group : user.getGroup()) {
                                    for (Friend friend : group.getFriend()) {
                                        if (friend.getName().contains(searchKey)) {
                                            Ob_Friend ob_friend = new Ob_Friend();
                                            ob_friend.setId(friend.getId());
                                            ob_friend.setName(friend.getName());
                                            ob_friend.setNumber(friend.getNumber());
                                            ob_friend.setGroupId(friend.getGroupId());
                                            ob_friend.setGroupName(group.getName());
                                            ob_friend.setRemindDate(friend.getLastContact());
                                            ob_friend.setFriendImg(friend.getFriendImg());
                                            if (friend.getFavorite() == null) {
                                                ob_friend.setFavorite(false);
                                            } else {
                                                ob_friend.setFavorite(friend.getFavorite());
                                            }

                                            ob_friend.setFriendImg(friend.getFriendImg());

                                            ob_friendList.add(ob_friend);
                                        }


                                    }
                                }
                            }
                            contactAdapter.initFriendList(ob_friendList);
                            contactAdapter.notifyDataSetChanged();

                        }
                        , error ->
                        {


                        });


        return view;
    }
}