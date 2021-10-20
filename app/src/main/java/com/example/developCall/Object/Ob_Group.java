package com.example.developCall.Object;

import java.io.Serializable;

public class Ob_Group implements Serializable {
    String userID;
    String id;
    String name;
    Ob_Friend friend;
    String created_At;



    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ob_Friend getFriend() {
        return friend;
    }

    public void setFriend(Ob_Friend friend) {
        this.friend = friend;
    }
}
