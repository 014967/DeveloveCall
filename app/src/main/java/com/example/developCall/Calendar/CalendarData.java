package com.example.developCall.Calendar;

public class CalendarData {
    private String title;
    private String name;
    private String time;

    /*public SampleData(int profile, String name, String number){
        this.profile = profile;
        this.name = name;
        this.number = number;
    }*/

    public void setTitle(String title) {
        this.title = title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) { this.time = time; }

    public String getTitle()
    {
        return this.title;
    }

    public String getName()
    {
        return this.name;
    }

    public String getTime()
    {
        return this.time;
    }
}