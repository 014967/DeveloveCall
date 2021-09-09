package com.example.developCall.Service;

public interface service {
    public String findScheduleFormat(String str);
    public String[][] getTokenizedArray(String item);
    public String[][] getModifiedChatArray(String[][] array1, String[][] array2);
}