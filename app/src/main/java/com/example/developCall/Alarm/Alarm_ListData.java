package com.example.developCall.Alarm;

public class Alarm_ListData {
    private int profile;
    private String name;
    private String content;

    /*public SampleData(int profile, String name, String number){
        this.profile = profile;
        this.name = name;
        this.number = number;
    }*/

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getProfile()
    {
        return this.profile;
    }

    public String getName()
    {
        return this.name;
    }

    public String getContent()
    {
        return this.content;
    }
}
