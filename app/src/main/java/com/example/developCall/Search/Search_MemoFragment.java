package com.example.developCall.Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Chat;
import com.amplifyframework.datastore.generated.model.Friend;
import com.example.developCall.Object.Ob_Memo;
import com.example.developCall.R;
import com.example.developCall.Service.service;
import com.example.developCall.Service.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class Search_MemoFragment extends Fragment {


    String searchKey;
    RecyclerView memo_recyclerView;
    Search_MemoAdapter search_memoAdapter;
    String userId;


    List<Ob_Memo> memoList = new ArrayList<>();

    service service = new serviceImpl();

    public Search_MemoFragment(String searchKey) {

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
        View view = inflater.inflate(R.layout.fragment_search__memo, container, false);

        memo_recyclerView = view.findViewById(R.id.memo_recyclerView);
        memo_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        search_memoAdapter = new Search_MemoAdapter(getActivity(), memoList);
        memo_recyclerView.setAdapter(search_memoAdapter);

        userId = Amplify.Auth.getCurrentUser().getUserId();
        service.getMemoList(userId, searchKey).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response ->
                        {
                            System.out.println(response);


                            for (Chat chat : response.getData().getChat()) {
                                Ob_Memo ob_memo = new Ob_Memo();
                                ob_memo.setId(chat.getId());
                                ob_memo.setFriendId(chat.getFriendId());
                                ob_memo.setDate(chat.getDate());
                                ob_memo.setMemo(chat.getMemo());


                                memoList.add(ob_memo);

                            }

                            for (Ob_Memo ob_memo : memoList) {
                                service.getFriendName(ob_memo.getFriendId())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(
                                                data ->
                                                {


                                                    for (Friend friend : data.getData()) {
                                                        ob_memo.setFriendImg(friend.getFriendImg());
                                                        ob_memo.setFriendName(friend.getName());
                                                        ob_memo.setGroupId(friend.getGroupId());

                                                    }

                                                    search_memoAdapter.initMemoList(memoList);
                                                    search_memoAdapter.notifyDataSetChanged();

                                                }, err ->
                                                {
                                                }

                                        );
                            }
                            search_memoAdapter.initMemoList(memoList);
                            search_memoAdapter.notifyDataSetChanged();


                        }
                        , err ->
                        {
                        });


        return view;
    }
}