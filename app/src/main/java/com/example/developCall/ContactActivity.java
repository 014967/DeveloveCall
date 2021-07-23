package com.example.developCall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.amplifyframework.api.graphql.MutationType;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Friend;
import com.amplifyframework.datastore.generated.model.User;
import com.example.developCall.Object.ContactModel;
import com.example.developCall.Adapter.ContactAdpater;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;

public class ContactActivity extends AppCompatActivity implements ContactAdpater.AdapterCallback {


    RecyclerView recyclerView;
    ArrayList<ContactModel> arrayList = new ArrayList<ContactModel>();
    ContactAdpater contactAdpater;
    HashMap<String,String> friendList;

    Button pushData;


    String userId = Amplify.Auth.getCurrentUser().getUserId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);


        pushData = findViewById(R.id.pushData);
        recyclerView = findViewById(R.id.conTactRecyclerView);
        checkPermission();



        pushData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){


// TODO: 7/10/21


            String userId = Amplify.Auth.getCurrentUser().getUserId();



                for(Map.Entry<String,String> entry : friendList.entrySet())
                {


                     Friend friend  = Friend.builder().userId(userId).number(entry.getKey()).name(entry.getValue()).build();
                     Amplify.DataStore.save( friend
                     ,success -> Log.d( "success","success")
                     ,error -> Log.e("fail" , "fail", error));





                };
                finish();

                }

            }
        );



    }

    private void checkPermission()
    {
        if(ContextCompat.checkSelfPermission(this , Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]
                    {
                            Manifest.permission.READ_CONTACTS
                    } , 100);
        }
        else
        {
            getContactList();
        }

    }

    private void getContactList()
    {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;

        Cursor cursor = getContentResolver().query(
                uri, null, null, null, sort
        );

        if(cursor.getCount() >0)
        {
             while(cursor.moveToNext())
             {
                 String id = cursor.getString(cursor.getColumnIndex(
                         ContactsContract.Contacts._ID
                 ));


                 String name = cursor.getString(cursor.getColumnIndex(
                         ContactsContract.Contacts.DISPLAY_NAME
                 ));

                 Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

                 String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" =?";

                 Cursor phoneCursor = getContentResolver().query(
                         uriPhone, null, selection , new String[]{id}, null
                 );

                 if(phoneCursor.moveToNext())
                 {
                     String number = phoneCursor.getString(phoneCursor.getColumnIndex(
                             ContactsContract.CommonDataKinds.Phone.NUMBER
                     ));

                     ContactModel model= new ContactModel();

                     model.setName(name);
                     model.setNumber(number);
                     arrayList.add(model);
                     phoneCursor.close();

                 }
             }
             cursor.close();;
        }
        //set layout manager
        recyclerView.setLayoutManager((new LinearLayoutManager(getApplicationContext())));

        contactAdpater = new ContactAdpater(getApplicationContext(), arrayList , ContactActivity.this);

        recyclerView.setAdapter(contactAdpater);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100 && grantResults.length > 0 && grantResults[0]
        == PackageManager.PERMISSION_GRANTED)
        {
          getContactList();
        }
        else
        {
            Toast.makeText(this,"permission denied", Toast.LENGTH_SHORT).show();
            checkPermission();
        }
    }

    @Override
    public void onItemClicked(HashMap<String, String> list) {
        friendList= list;
    }
}