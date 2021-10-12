package com.example.developCall.Object;

import java.util.List;

public class Ob_SearchChat {
    String friendImg;
    String date;
    String s3_url;

    public String getS3_url() {
        return s3_url;
    }

    public void setS3_url(String s3_url) {
        this.s3_url = s3_url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFriendImg() {
        return friendImg;
    }

    public void setFriendImg(String friendImg) {
        this.friendImg = friendImg;
    }

    String friendId;
    String groupId;
    String friendName;

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    List<Ob_DetailChat> chatList;

    public List<Ob_DetailChat> getChatList() {
        return chatList;
    }

    public void setChatList(List<Ob_DetailChat> chatList) {
        this.chatList = chatList;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }


}


