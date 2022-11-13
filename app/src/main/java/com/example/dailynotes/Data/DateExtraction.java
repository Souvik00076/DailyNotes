package com.example.dailynotes.Data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateExtraction {
    public static String generateDateString(long unixStamp) {
        Date date = new java.util.Date(unixStamp * 1000L);
        Date current = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+5:30"));
        String formattedDate = sdf.format(date);
        String currentFormattedDate = sdf.format(current);
        String month = getMonth(formattedDate.substring(5, 7));
        String day = formattedDate.substring(8, 10);
        String time = formattedDate.substring(11, 16);
        int curVal, pastVal;
        curVal = Integer.parseInt(currentFormattedDate.substring(8, 10));
        pastVal = Integer.parseInt(day);
        if (curVal == pastVal) {
            day = "TODAY";
            month = "";
        } else if (curVal - 1 == pastVal) {
            month = "";
            day = "YESTERDAY";
        }
        String timeStamp = "PM";
        String newtime = time;
        int timePos = Integer.parseInt(time.substring(0, 2));
        if (timePos > 0 && timePos < 12) timeStamp = "AM";
        else {
            if (timePos == 12) newtime = "12" + time.substring(2);
            else newtime = String.valueOf(timePos - 12) + time.substring(2);
        }
        if (month.isEmpty())
            return day + "  " + newtime + " " + timeStamp;
        return month + " " + day + ",  " + newtime + " " + timeStamp;
    }

    public static String getMonth(String month) {
        int value = Integer.parseInt(month);
        switch (value) {
            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "MARCH";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUNE";
            case 7:
                return "JULY";
            case 8:
                return "AUG";
            case 9:
                return "SEPT";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";
        }
        return "";
    }
}
