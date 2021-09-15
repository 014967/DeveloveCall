package com.example.developCall.Service;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;


public class serviceImpl implements service {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String findScheduleFormat(String str) {
        Calendar cal = Calendar.getInstance();
        LocalDateTime now = LocalDateTime.now();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        String ret = "NULL";

        String scheduleStr = "";

        String[] tmrw = {"내일"};
        String[] dayTmrw = {"모레", "내일 모레", "이틀 뒤", "이틀 후"};
        String[] thrDayNow = {"글피", "모레 모레", "삼일 뒤", "삼일 후"};

        int chk1 = 0, chk2 = 0, chk3 = 0, chk4 = 0; //달,시,일,분

        String[] aftrNmths = {"달 후", "달 뒤"};
        String[] aftrNHrs = {"시간 후", "시간 뒤"};
        String[] hrsNum = {"한", "두", "세", "네", "다섯", "여섯", "일곱", "여덟", "아홉", "열", "열한", "열두"};

        String[] aftrNdays = {"일 후", "일 뒤", "일후", "일뒤"};
        String[] aftrNMnts = {"분 후", "분 뒤", "분후", "분뒤"};
        String[] mntsNum = {"일", "이", "삼", "사", "오", "육", "칠", "팔", "구", "십", "십일", "십이",
                "십삼", "십사", "십오", "십육", "십칠", "십팔", "십구", "이십", "이십일", "이십이",
                "이십삼", "이십사", "이십오", "이십육", "이십칠", "이십팔", "이십구", "삼십", "삼십일",
                "삼십이", "삼십삼", "삼십사", "삼십오", "삼십육", "삼십칠", "삼십팔", "삼십구", "사십",
                "사십일", "사십이", "사십삼", "사십사", "사십오", "사십육", "사십칠", "사십팔", "사십구",
                "오십", "오십일", "오십이", "오십삼", "오십사", "오십오", "오십육", "오십칠", "오십팔",
                "오십구", "육십"};

        String[] mthsNum = {"일", "이", "삼", "사", "오", "유", "칠", "팔", "구", "시", "십일", "십이"};
        String[] daysNum = {"일", "이", "삼", "사", "오", "육", "칠", "팔", "구", "십", "십일", "십이",
                "십삼", "십사", "십오", "십육", "십칠", "십팔", "십구", "이십", "이십일", "이십이",
                "이십삼", "이십사", "이십오", "이십육", "이십칠", "이십팔", "이십구", "삼십", "삼십일"};

        int index = 0, tmp = 0, last = 0;

        if (tmp > last) last = tmp;

        for (String form : tmrw) {
            if ((tmp = str.indexOf(form)) != -1) {
                if (tmp > last) last = tmp;
                cal.add(cal.DATE, 1);
                ret = sdf.format(cal.getTime());
            }
        }
        for (String form : dayTmrw) {
            if ((tmp = str.indexOf(form)) != -1) {
                if (tmp > last) last = tmp;
                cal.add(cal.DATE, 2);
                ret = sdf.format(cal.getTime());
            }
        }
        for (String form : thrDayNow) {
            if ((tmp = str.indexOf(form)) != -1) {
                if (tmp > last) last = tmp;
                cal.add(cal.DATE, 3);
                ret = sdf.format(cal.getTime());
            }
        }

        for (String form : aftrNmths) {
            index = 0;
            for (String form2 : hrsNum) {
                if ((tmp = str.indexOf(form2 + " " + form)) != -1
                        || (tmp = str.indexOf(form2 + form)) != -1) {
                    if (tmp > last) last = tmp;
                    cal.set(cal.MONTH, now.getMonthValue() + index);
                    ret = sdf.format(cal.getTime());
                    chk1 = 1;
                }
                index++;
            }
        }
        for (String form : aftrNdays) {
            index = 0;
            for (String form2 : mntsNum) {
                if ((tmp = str.indexOf(form2 + " " + form)) != -1
                        || (tmp = str.indexOf(form2 + form)) != -1) {
                    if (tmp > last) last = tmp;
                    cal.set(cal.DATE, now.getDayOfMonth() + index + 1);
                    ret = sdf.format(cal.getTime());
                    chk3 = 1;
                }
                index++;
            }
        }
        for (String form : aftrNHrs) {
            index = 0;
            for (String form2 : hrsNum) {
                if ((tmp = str.indexOf(form2 + " " + form)) != -1
                        || (tmp = str.indexOf(form2 + form)) != -1) {
                    if (tmp > last) last = tmp;
                    cal.set(cal.HOUR, now.getHour() + index - 11);
                    ret = sdf.format(cal.getTime());
                    chk2 = 1;
                }
                index++;
            }
        }
        for (String form : aftrNMnts) {
            index = 0;
            for (String form2 : mntsNum) {
                if ((tmp = str.indexOf(form2 + " " + form)) != -1
                        || (tmp = str.indexOf(form2 + form)) != -1) {
                    if (tmp > last) last = tmp;
                    cal.set(cal.MINUTE, now.getMinute() + index + 1);
                    ret = sdf.format(cal.getTime());
                    chk4 = 1;
                }
                index++;
            }
        }

        if ((tmp = str.indexOf("내년")) != -1) {
            if (tmp > last) last = tmp;
            cal.add(cal.YEAR, 1);
            ret = sdf.format(cal.getTime());
        }
        if ((tmp = str.indexOf("후년")) != -1
                || (tmp = str.indexOf("내후년")) != -1) {
            if (tmp > last) last = tmp;
            cal.add(cal.YEAR, 2);
            ret = sdf.format(cal.getTime());
        }

        index = 0;
        for (String form : mthsNum) {
            if ((tmp = str.indexOf(form + "월")) != -1
                    || (tmp = str.indexOf(form + " 월")) != -1) {
                if (tmp > last) last = tmp;
                cal.set(cal.MONTH, index);
                ret = sdf.format(cal.getTime());
            }
            index++;
        }
        index = 0;
        for (String form : daysNum) {
            if (((tmp = str.indexOf(form + "일")) != -1
                    || (tmp = str.indexOf(form + " 일")) != -1)
                    && chk3 != 1) {
                if (tmp > last) last = tmp;
                cal.set(cal.MINUTE, 0);
                cal.set(cal.HOUR, 0);
                cal.set(cal.DATE, index + 1);
                ret = sdf.format(cal.getTime());
            }
            index++;
        }
        index = 0;
        for (String form : hrsNum) {
            if (((tmp = str.indexOf(form + "시")) != -1
                    || (tmp = str.indexOf(form + " 시")) != -1)
                    && chk2 != 1) {
                if (tmp > last) last = tmp;
                cal.set(cal.MINUTE, 0);
                if ((tmp = str.indexOf(form + "시 반")) != -1) {
                    if (tmp > last) last = tmp;
                    cal.set(cal.MINUTE, 30);
                }
                cal.set(cal.HOUR, index + 1);
                ret = sdf.format(cal.getTime());
            }
            index++;
        }
        index = 0;
        for (String form : mntsNum) {
            if (((tmp = str.indexOf(form + "분")) != -1
                    || (tmp = str.indexOf(form + " 분")) != -1)
                    && chk4 != 1) {
                if (tmp > last) last = tmp;
                cal.set(cal.MINUTE, index + 1);
                ret = sdf.format(cal.getTime());
            }
            index++;
        }
        int scdIdx = 0;
        tmp = str.indexOf(" ", last);
        if (tmp > scdIdx) scdIdx = tmp;
        tmp = str.indexOf(" 후", last) + 2;
        if (tmp > scdIdx) scdIdx = tmp;
        tmp = str.indexOf(" 뒤", last) + 2;
        if (tmp > scdIdx) scdIdx = tmp;
        tmp = str.indexOf(" 후에", last) + 3;
        if (tmp > scdIdx) scdIdx = tmp;
        tmp = str.indexOf(" 뒤에", last) + 3;
        if (tmp > scdIdx) scdIdx = tmp;
        tmp = str.indexOf(" 쯤", last) + 2;
        if (tmp > scdIdx) scdIdx = tmp;
        tmp = str.indexOf(" 쯤에", last) + 3;
        if (tmp > scdIdx) scdIdx = tmp;
        tmp = str.indexOf("시 반", last) + 3;
        if (tmp > scdIdx) scdIdx = tmp;
        tmp = str.indexOf("시 반에", last) + 4;
        if (tmp > scdIdx) scdIdx = tmp;
        return ret + str.substring(scdIdx);
    }


    public String[][] mergeArray(String[][] array1, String[][] array2) {
        String result[][];
        String pre;
        for (int i = 0; i < array1.length; i++) {
            for (int j = 0; j < array2.length; j++) {
                if (array1[i][0].equals(array2[j][0])) {
                    array1[i][3] = array2[j][3];
                }
            }
        }
        pre = array1[0][2];
        int count = 1;
        for (int i = 1; i < array1.length; i++) {
            if (array1[i][2].equals(pre)) {

            } else {
                pre = array1[i][2];
                count++;

            }
        }
        result = new String[count][2];
        for (String k[] : result) {
            Arrays.fill(k, "");
        }
        count = 0;
        for (int i = 0; i < array1.length; i++) {
            if (array1[i][2].equals(pre)) {
                result[count][0] = pre;
                result[count][1] += array1[i][3] + " ";

            } else {
                pre = array1[i][2];
                count++;
                result[count][0] = pre;
                result[count][1] += array1[i][3];
            }
        }

        return result;
    }





    public String[][] getTokenizedArray(String item) {
        String dummy[];
        String array[][];
        int i = 0;

        dummy = item.replace("[", "").replace("]", "").split(",");
        array = new String[dummy.length][];
        for (String row : dummy) {
            array[i++] = row.split("/", 5);
        }
        return array;
    }

    public String[][] getModifiedChatArray(String[][] array1, String[][] array2) {
        String preSpk = "";
        String preContent = "";
        int preIndex = 0;
        String preEndtime = "";

        String postSpk = "";
        String postContent = "";
        String postEndtime = "";
        int changeCount = 0;

        String[][] modifyArray;

        for (int i = 0; i < array1.length; i++) {
            for (int j = 0; j < array2.length; j++) {
                if (array1[i][0].equals(array2[j][0])) {
                    array1[i][3] = array2[j][3];
                }
            }
        }
        for (int i = 0; i < array1.length; i++) {
            if (preSpk.equals("")) {
                preSpk = array1[i][2];
                preContent = array1[i][3];
                preIndex = i;
                preEndtime = array1[i][1];
            } else {
                postSpk = array1[i][2];
                postContent = array1[i][3];
                postEndtime = array1[i][1];
                if (preSpk.equals(postSpk)) {
                    preContent = preContent + " " + postContent;  // TODO: 2021/09/10  스피커가 바꾸지않았을경우 이거 수정햐여험
                    preEndtime = postEndtime;
                } else {
                    array1[preIndex][3] = preContent;
                    array1[preIndex][1] = postEndtime;
                    preSpk = postSpk;
                    preContent = postContent;
                    preEndtime = postEndtime;
                    preIndex = i;
                    changeCount++;
                }
            }
        }
        preSpk = "";
        modifyArray = new String[changeCount + 1][4];
        int k = 0;
        for (int i = 0; i < array1.length; i++) {
            if (preSpk.equals("")) {
                for (int j = 0; j < 4; j++) {
                    modifyArray[k][j] = array1[i][j];
                }
                k++;
                preSpk = array1[i][2];
            } else {
                postSpk = array1[i][2];
                if (!preSpk.equals(postSpk)) {
                    for (int m = 0; m < 4; m++) {
                        modifyArray[k][m] = array1[i][m];
                    }
                    k++;
                    preSpk = postSpk;
                }
            }
        }
        return modifyArray;
    }
}
