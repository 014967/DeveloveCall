package com.example.developCall.Function;

import android.util.Log;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.User;
import com.example.developCall.Object.FriendId;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class S3Upload {


    public static void upload(String filename, InputStream inputStream) {


        String[] format = filename.split("\\.");


        Callee callee = new Callee();


        String userId = Amplify.Auth.getCurrentUser().getUserId();
        String friendNumber = "";
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
        String uploadDate = formatter.format(date);

        final String[] uploadFilename = new String[1];
        final String friendIdString;


        FriendId friendId = new FriendId();


        Amplify.API.query(
                ModelQuery.list(User.class, User.ID.contains(userId)),
                response ->
                {
                    for (User user : response.getData()) {


//                        for (Friend friend : user.getFriend()) {
//                            //Log.d(" friend : " , friend.getNumber()+ friend.getId());
//                            if (friend.getNumber().equals(friendNumber)) {
//                                System.out.println(friend.getId());
//                                callee.setFriendId(friend.getId());
//
//                            }
//                        }
                    }
                    callee.execute();
                },
                error ->
                {

                }

        );


        callee.setCallback(new Callee.Callback() {
            @Override
            public void callbackMethod(String id) {
                friendId.setFriendId(id);
                //  System.out.println(friendId.getFriendId());
                uploadFilename[0] = userId + "_" + friendId.getFriendId() + "_" + uploadDate + "." + format[1];


                try {
                    Amplify.Storage.uploadInputStream(
                            uploadFilename[0],
                            inputStream, //result.getKey()
                            result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                            storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)

                    );
                } catch (Exception e) {
                    Log.d("error ", String.valueOf(e));
                }

            }
        });


    }


    public static class Callee {
        interface Callback {
            void callbackMethod(String id);
        }

        public Callback m_callback;
        public String friendId = "";

        public Callee() {

            m_callback = null;
            friendId = "";
        }

        public void setCallback(Callback callback) {
            this.m_callback = callback;
        }

        public void setFriendId(String id) {
            this.friendId = id;
        }

        public void execute() {

            if (this.m_callback != null && !this.friendId.equals("")) {
                m_callback.callbackMethod(friendId);
            }
        }


    }


}
