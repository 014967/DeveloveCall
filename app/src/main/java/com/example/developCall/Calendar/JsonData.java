package com.example.developCall.Calendar;

public class JsonData {
    private String title;
    private String name;
    private String time;

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String time) { this.time = time; }

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
