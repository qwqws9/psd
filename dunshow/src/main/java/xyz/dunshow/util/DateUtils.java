package xyz.dunshow.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String getDateString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(date);
    }

    public static String getDateStringDetail(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(date);
    }

    public static String getTimeMillisStringDetail(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return formatter.format(date);
    }
    
    public static String getStringToDate(String date1) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = null;
        try {
            date = formatter.parse(date1);
        }
        catch (ParseException e) {
        }
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy MM.dd HH:mm:ss");

        return formatter1.format(date);
    }
}
