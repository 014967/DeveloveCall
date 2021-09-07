package com.example.developCall.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.amplifyframework.core.model.Model;
import com.amplifyframework.datastore.DataStoreException;
import com.amplifyframework.datastore.DataStoreItemChange;
import com.amplifyframework.datastore.generated.model.Group;
import com.amplifyframework.datastore.generated.model.User;
import com.example.developCall.Adapter.GroupListAdapter;
import com.example.developCall.Object.Ob_Group;
import com.example.developCall.R;

import java.util.ArrayList;
import java.util.List;

public class GroupListFragment extends Fragment {


    RecyclerView group_rv;
    Button btn_addGroup;
    AlertDialog dialog;
    AlertDialog.Builder dialogBuilder;
    ProgressBar progressBar;

    List<Ob_Group> list_group = new ArrayList<>();
    GroupListAdapter groupListAdapter;


    String userId = Amplify.Auth.getCurrentUser().getUserId();

    //popup
    EditText Et_group;
    Button btn_Return;
    Button btn_addEt_group;


    public GroupListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupListAdapter = new GroupListAdapter(list_group);
        groupListAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_list, container, false);
        group_rv = view.findViewById(R.id.group_rv);
        btn_addGroup = view.findViewById(R.id.btn_addGroup);
        String userId = Amplify.Auth.getCurrentUser().getUserId();
        progressBar = view.findViewById(R.id.group_progressBar);
        progressBar.setVisibility(View.VISIBLE);


        group_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        groupListAdapter = new GroupListAdapter(list_group);
        if (!groupListAdapter.hasObservers()) {
            groupListAdapter.setHasStableIds(true);
        }
        group_rv.setAdapter(groupListAdapter);


        btn_addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder = new AlertDialog.Builder(getContext());
                View popView = getLayoutInflater().inflate(R.layout.popup_group, null);
                Et_group = popView.findViewById(R.id.Et_group);
                btn_Return = popView.findViewById(R.id.btn_return);
                btn_addEt_group = popView.findViewById(R.id.btn_addET_group);



                if (popView.getParent() != null)
                    ((ViewGroup) popView.getParent()).removeView(popView);
                dialogBuilder.setView(popView);
                dialog = dialogBuilder.create();
                dialog.show();


                btn_Return.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btn_addEt_group.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String group_name = Et_group.getText().toString();
                        Group group = Group.builder().userId(userId).name(group_name).build();

                        Amplify.DataStore.save(group
                                , this::onSavedSuccess   // 해당 그룹을 db에 저장
                                , this::onError

                        );


                    }


                    private <T extends Model> void onSavedSuccess(@NonNull DataStoreItemChange<T> tDataStoreItemChange) {
                        Et_group.setText("");
                        dialog.dismiss();
                        getActivity().runOnUiThread(() ->
                                {
                                    Group group= (Group) tDataStoreItemChange.item(); // 이거 서로 안맞
                                    Ob_Group ob_group = new Ob_Group();
                                    ob_group.setUserID(group.getUserId());
                                    ob_group.setId(group.getId());
                                    ob_group.setName(group.getName());
                                    list_group.add(ob_group);
                                    groupListAdapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(), "그룹추가 완료", Toast.LENGTH_LONG).show();

                                }

                        );



                        /// 업데이트는 되었으나 response에서 업데이트가 된것을 반영하지 않음.
                    }

                    private void onError(DataStoreException e) {
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(getActivity(), "그룹추가 에러", Toast.LENGTH_LONG).show());

                    }
                }); //End of btn click listener
            }
        });

        getGroupList();


        return view;
    }


    public void getGroupList() {
        Amplify.API.query(
                ModelQuery.list(User.class, User.ID.contains(userId)),
                response ->
                {
                    list_group = new ArrayList<>(); // 초기화 한번
                    list_group = addGroupList(response);     // 변경된 db에서 데이터를 가져와서 adpater에 넘긴다


                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            groupListAdapter.setList_group(list_group);
                            groupListAdapter.notifyDataSetChanged();

                        }
                    });



                },
                error ->
                {

                }
        );

    }

    private List<Ob_Group> addGroupList(@NonNull GraphQLResponse<PaginatedResult<User>> response) {
        for (User user : response.getData()) {
            for (Group group : user.getGroup()) {
                Ob_Group ob_group = new Ob_Group();
                ob_group.setId(group.getId());
                ob_group.setUserID(group.getUserId());
                ob_group.setName(group.getName());
                list_group.add(ob_group);
            }
        }


        return list_group;
    }
}
