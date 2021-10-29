package com.example.developCall.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.developCall.Adapter.FriendListAdapter;
import com.example.developCall.ContactActivity;
import com.example.developCall.Object.Ob_Friend;
import com.example.developCall.Object.Ob_Group;
import com.example.developCall.R;

import java.util.ArrayList;
import java.util.List;

public class FriendListFragment extends Fragment {


    Button btn_addFriend;
    RecyclerView friendRv;
    ProgressBar progressBar;
    FriendListAdapter friendListAdapter;
    String userId = Amplify.Auth.getCurrentUser().getUserId();
    List<Ob_Friend> friendListArray = new ArrayList<>();
    List<Ob_Friend> passFriendList = new ArrayList<>();
    Ob_Group ob_group;

    ImageView btn_back;

    SearchView friendSearchView;

    //AlertDialog

    AlertDialog dialog;
    AlertDialog.Builder dialogBuilder;


    public FriendListFragment() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

        ob_group = (Ob_Group) getArguments().getSerializable("ob_group");


        progressBar = view.findViewById(R.id.progressBar);
        btn_addFriend = view.findViewById(R.id.btn_addFriend);
        friendRv = view.findViewById(R.id.friendRv);
        progressBar.setVisibility(View.VISIBLE);

        friendSearchView = view.findViewById(R.id.friendSeacrhView);

        btn_back = view.findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });



        friendRv.setLayoutManager((new LinearLayoutManager(getContext())));
        friendListAdapter = new FriendListAdapter(friendListArray);
        if (!friendListAdapter.hasObservers()) {
            friendListAdapter.setHasStableIds(true);
        }
        friendRv.setAdapter(friendListAdapter);




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
                    friendListAdapter.setFriendListArray(friendListArray);
                    friendListAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });



        btn_addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getContact();

            }
        });
        passFriendList();


        return view;
    }


    public void passFriendList() {
        Amplify.API.query(
                ModelQuery.list(User.class, User.ID.contains(userId)),
                response ->
                {
                    friendListArray = new ArrayList<>();
                    friendListArray = addFriendList(response, ob_group.getName());

                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            friendListAdapter.setFriendListArray(friendListArray);
                            friendListAdapter.notifyDataSetChanged();

                        }
                    });


                },
                error ->
                {

                });

    }



    public List<Ob_Friend> addFriendList(GraphQLResponse<PaginatedResult<User>> response, String group_name) {
        for (User user : response.getData()) {
            for (Group group : user.getGroup()) {

                if (group.getName().equals(group_name)) {

                    for (Friend friend : group.getFriend()) {
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
                        friendListArray.add(ob_friend);

                    }
                    break;

                }


            }

        }

        return friendListArray;
    }


    public void getContact() {
        Intent intent = new Intent(getActivity(), ContactActivity.class);
        intent.putExtra("group", ob_group);
        getContactLauncher.launch(intent);

    }

    ActivityResultLauncher<Intent> getContactLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        //Intent data = result.getData();
                        ArrayList<Ob_Friend> al = (ArrayList<Ob_Friend>) result.getData().getSerializableExtra("Ob_friendList");
                        for(Ob_Friend ob: al)
                        {
                            friendListArray.add(ob);
                        }
                        friendListAdapter.notifyDataSetChanged();
                    }
                }
            }
    );


}
