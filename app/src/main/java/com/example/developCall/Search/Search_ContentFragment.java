package com.example.developCall.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Chat;
import com.amplifyframework.datastore.generated.model.DetailChat;
import com.amplifyframework.datastore.generated.model.Friend;
import com.example.developCall.Object.Ob_DetailChat;
import com.example.developCall.Object.Ob_SearchChat;
import com.example.developCall.R;
import com.example.developCall.Service.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Search_ContentFragment extends Fragment {


    String searchKey;
    String userId;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Search_ContentAdapter search_contentAdapter;

    com.example.developCall.Service.service service = new serviceImpl();


    List<Ob_SearchChat> searchList;
    List<Ob_DetailChat> chatList;


    public Search_ContentFragment(String searchKey) {
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
        View view = inflater.inflate(R.layout.fragment_search__content, container, false);


        searchList = new ArrayList<>();

        progressBar = view.findViewById(R.id.progressBar2);


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        search_contentAdapter = new Search_ContentAdapter(getActivity(), searchList);
        recyclerView.setAdapter(search_contentAdapter);
        userId = Amplify.Auth.getCurrentUser().getUserId();


        service.getdummy(userId, searchKey).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->
                        {
                            for (Chat chat : response.getData().getChat()) {
                                String friendId = chat.getFriendId();


                                String date = chat.getDate();
                                Ob_SearchChat ob_searchChat = new Ob_SearchChat();

                                ob_searchChat.setFriendId(friendId);
                                ob_searchChat.setDate(date);
                                ob_searchChat.setS3_url(chat.getS3Url());


                                chatList = new ArrayList<>();


                                if (chat.getDetailChat().size() != 0) {
                                    for (DetailChat detailChat : chat.getDetailChat()) {


                                        Ob_DetailChat ob_detailChat = new Ob_DetailChat();
                                        ob_detailChat.setId(detailChat.getId());
                                        ob_detailChat.setContent(detailChat.getContent());
                                        ob_detailChat.setSpk(detailChat.getSpeakerLabel());
                                        ob_detailChat.setCreated_at(detailChat.getCreatedAt());
                                        ob_detailChat.setChat_Id(detailChat.getChatId());
                                        chatList.add(ob_detailChat);

                                    }
                                    ob_searchChat.setChatList(chatList);
                                    searchList.add(ob_searchChat);

                                } else {


                                }


                            }


                            for (Ob_SearchChat ob_searchChat : searchList) {
                                service.getFriendName(ob_searchChat.getFriendId())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(data ->
                                                {

                                                    for (Friend friend : data.getData()) {
                                                        ob_searchChat.setFriendImg(friend.getFriendImg());
                                                        ob_searchChat.setFriendName(friend.getName());
                                                        ob_searchChat.setGroupId(friend.getGroupId());
                                                    }
                                                    progressBar.setVisibility(View.GONE);
                                                    search_contentAdapter.initList(searchList);
                                                    search_contentAdapter.notifyDataSetChanged();

                                                },
                                                err ->
                                                {
                                                });


                            }
                            //  search_contentAdapter.initList(searchList);
                            // search_contentAdapter.notifyDataSetChanged();

                        }
                        , error ->
                        {
                        });


        return view;
    }


}




