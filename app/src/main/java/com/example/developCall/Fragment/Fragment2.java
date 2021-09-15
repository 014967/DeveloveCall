package com.example.developCall.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.datastore.DataStoreException;
import com.amplifyframework.datastore.DataStoreItemChange;
import com.amplifyframework.datastore.generated.model.Friend;
import com.amplifyframework.datastore.generated.model.Group;
import com.amplifyframework.datastore.generated.model.User;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.bumptech.glide.Glide;
import com.example.developCall.Alarm.AlarmSet_Fragment;
import com.example.developCall.LoginActivity;
import com.example.developCall.Object.Ob_Friend;
import com.example.developCall.R;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Fragment2 extends Fragment {

    Fragment fragment1, fragment2, fragment3;
    TextView logout;
    Ob_Friend ob_friend;


    TextView txt_pf_name;
    TextView txt_pf_number;
    TextView txt_category;
    TextView txt_category2;
    TextView tv_edit;

    //

    CircleImageView img_profile;
    Bitmap bm = null;


    // POPUP
    AlertDialog dialog;
    AlertDialog.Builder dialogBuilder;
    EditText et_name;
    EditText et_groupname;
    Button btn_edit;
    Button btn_return;
    //
    //
    FragmentManager fragmentManager;
    FragmentTransaction transaction;


    String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile, container, false);


        userId = Amplify.Auth.getCurrentUser().getUserId();

        fragmentManager = getActivity().getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        AlarmSet_Fragment alarmSet_fragment = new AlarmSet_Fragment();


        txt_pf_name = view.findViewById(R.id.txt_pf_name);
        txt_pf_number = view.findViewById(R.id.txt_pf_number);
        txt_category = view.findViewById(R.id.txt_category);
        txt_category2 = view.findViewById(R.id.txt_category2);
        tv_edit = view.findViewById(R.id.tv_edit);
        img_profile = view.findViewById(R.id.img_profile);


        ob_friend = (Ob_Friend) getArguments().getSerializable("ob_friend");
        txt_pf_name.setText(ob_friend.getName());
        txt_pf_number.setText(ob_friend.getNumber());
        txt_category.setText(ob_friend.getGroupName());


        txt_category2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction.replace(R.id.home_frame, alarmSet_fragment).commitAllowingStateLoss();
            }
        });

        if (ob_friend.getFriendImg() != null) {
            String END_POINT = "https://developcallfriendimg.s3.ap-northeast-2.amazonaws.com/";
            Glide.with(view).load(END_POINT + ob_friend.getFriendImg()).into(img_profile);
        }

        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder = new AlertDialog.Builder(getContext());


                View popView = getLayoutInflater().inflate(R.layout.pop_up_edit, null);
                et_name = popView.findViewById(R.id.Et_name);
                btn_edit = popView.findViewById(R.id.btn_edit);
                btn_return = popView.findViewById(R.id.btn_return);
                if (popView.getParent() != null)
                    ((ViewGroup) popView.getParent()).removeView(popView);
                dialogBuilder.setView(popView);
                dialog = dialogBuilder.create();
                dialog.show();

                btn_return.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = et_name.getText().toString();
                        Amplify.API.query(
                                ModelQuery.list(User.class, User.ID.contains(userId)),
                                response ->
                                {
                                    for(User user : response.getData())
                                    {
                                        for(Group group : user.getGroup())
                                        {
                                            for(Friend f : group.getFriend())
                                            {
                                                if(f.getNumber().equals(ob_friend.getNumber()))
                                                {
                                                    Friend newF = f.copyOfBuilder().name(name).build();
                                                    Amplify.DataStore.save(newF
                                                    ,this::onSavedSuccess
                                                    ,this::onError);
                                                }
                                            }
                                        }
                                    }

                                },error ->
                                {

                                }

                        );
                    }

                    private <T extends Model> void onSavedSuccess(@NonNull DataStoreItemChange<T> tDataStoreItemChange) {
                        et_name.setText("");
                        dialog.dismiss();
                        getActivity().runOnUiThread(() ->
                                {
                                    Friend friend = (Friend) tDataStoreItemChange.item();
                                    txt_pf_name.setText(friend.getName());
                                    Toast.makeText(getActivity(), "수정 완료", Toast.LENGTH_LONG).show();

                                }

                        );

                        /// 업데이트는 되었으나 response에서 업데이트가 된것을 반영하지 않음.
                    }
                    private void onError(DataStoreException e) {
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(getActivity(), "이름수정 에러", Toast.LENGTH_LONG).show());

                    }
                });


            }
        });
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                getImgLauncher.launch(intent);
            }
        });



        fragment1 = new TabFragment1(userId, ob_friend.getId());
        fragment2 = new TabFragment2();
        fragment3 = new TabFragment3();




        Bundle bundle = new Bundle();
        bundle.putSerializable("name", ob_friend.getName());
        fragment1.setArguments(bundle);
        transaction.replace(R.id.tabFrame,fragment1).commit();
       // requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.tabFrame, fragment1).commit();

        TabLayout tabs = (TabLayout) view.findViewById(R.id.tab);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int position = tab.getPosition();

                if (position == 0) {

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabFrame, fragment1).commit();

                } else if (position == 1) {

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabFrame, fragment2).commit();

                } else if (position == 2) {

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabFrame, fragment3).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        logout = (TextView) view.findViewById(R.id.logout);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amplify.Auth.signOut(
                        () -> Log.i("AuthQuickstart", "Signed out successfully"),
                        error -> Log.e("AuthQuickstart", error.toString()));

                Intent in = new Intent(requireActivity().getApplicationContext(), LoginActivity.class);
                requireActivity().startActivity(in);
                requireActivity().finish();
            }
        });

        return view;

    }

    ActivityResultLauncher<Intent> getImgLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();


                        if (data != null) {


                            try {
                                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                                img_profile.setImageBitmap(bm);

                                Uri selectedImageUri = data.getData();
                                String imagepath = getPath(selectedImageUri, bm);
                                File imageFile = new File(imagepath);

                                Observable.just(imageFile)
                                        .observeOn(Schedulers.newThread())
                                        .subscribe(getObserver());


                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }

                    }
                }
            }
    );

    private Observer<? super File> getObserver() {
        return new Observer<File>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull File file) {
                AWSS3StoragePlugin plugin = (AWSS3StoragePlugin) Amplify.Storage.getPlugin("awsS3StoragePlugin");
                AmazonS3Client client = plugin.getEscapeHatch();
                upload(file, client);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }


    public void upload(File file, AmazonS3Client client) {
        uploadToS3(new PutObjectRequest("developcallfriendimg", userId + "_" + ob_friend.getGroupId() + "_" + ob_friend.getId() + ".jpg", file), client);
    }

    public void uploadToS3(PutObjectRequest putObjectRequest, AmazonS3Client client) {

        try {
            client.putObject(putObjectRequest);
            System.out.println(String.format("[%s] upload complete", putObjectRequest.getKey()));

        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPath(Uri uri, Bitmap bm) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String filePath = cursor.getString(columnIndex);
        bm = BitmapFactory.decodeFile(filePath);
        cursor.close();
        return filePath;
    }

}
