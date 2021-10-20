package com.example.developCall.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.datastore.DataStoreException;
import com.amplifyframework.datastore.DataStoreItemChange;
import com.amplifyframework.datastore.generated.model.Inquiry;
import com.example.developCall.R;

public class Personal_Inquiry extends Fragment {


    EditText et_title;
    EditText et_content;
    EditText et_email;
    TextView tv_submit;

    String title;
    String content;
    String email;


    ImageView btn_back;
    String userId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_inquiry, container, false);


        et_title = view.findViewById(R.id.et_title);
        et_content = view.findViewById(R.id.et_content);
        et_email = view.findViewById(R.id.et_email);
        tv_submit = view.findViewById(R.id.tv_submit);

        btn_back = view.findViewById(R.id.btn_back);


        userId = Amplify.Auth.getCurrentUser().getUserId();



        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = et_title.getText().toString();
                content = et_content.getText().toString();
                email = et_email.getText().toString();

                Inquiry inquiry = Inquiry.builder().userId(userId).title(title).content(content).build();

                Amplify.DataStore.save(inquiry,
                        this::onSavedSucessed,
                        this::onError);

            }

            private void onError(DataStoreException e) {
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "실패 ! ", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                });
            }

            private <T extends Model> void onSavedSucessed(DataStoreItemChange<T> tDataStoreItemChange) {
                ThreadUtils.runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  Toast.makeText(getContext(), "전송 성공 !", Toast.LENGTH_SHORT).show();
                                                  getActivity().onBackPressed();
                                              }
                                          }
                );
            }
        });




        return view;
    }
}
