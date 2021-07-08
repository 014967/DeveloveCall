package com.example.developCall;

public class ListData {
    private int profile;
    private String pro_Name;
    private String pro_Number;

    public ListData(int profile, String profileName, String profileNumber){
        this.profile = profile;
        this.pro_Name = profileName;
        this.pro_Number = profileNumber;
    }

    public int getProfile()
    {
        return this.profile;
    }

    public String getName()
    {
        return this.pro_Name;
    }

    public String getNumber()
    {
        return this.pro_Number;
    }
}
