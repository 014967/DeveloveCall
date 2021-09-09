package com.example.developCall.Calendar;

public class CalendarData {
    private int profile;
    private String name;
    private String number;

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

    public void setNumber(String number) {
        this.number = number;
    }

    public int getProfile()
    {
        return this.profile;
    }

    public String getName()
    {
        return this.name;
    }

    public String getNumber()
    {
        return this.number;
    }
}