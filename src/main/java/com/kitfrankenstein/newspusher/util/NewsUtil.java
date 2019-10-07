package com.kitfrankenstein.newspusher.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Kit
 * @date: 2019/8/13 14:03
 */
public class NewsUtil {

    public static final String TABLE_MCHINA = "mchina";
    public static final String TABLE_163 = "netease";
    public static final String TABLE_SINA = "sina";

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";


    public static String stampToDateString(String s){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (s == null) {
            return simpleDateFormat.format(new Date());
        }
        long lt = new Long(s);
        if (s.length() < 13) {
            lt *= 1000L;
        }
        Date date = new Date(lt);
        return simpleDateFormat.format(date);
    }

    public static Timestamp getTimeStampAfterMinutes(int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, min);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static String subLength(String str, int length) {
        if (str.length() <= length) {
            return str;
        }
        return str.substring(0, length) + "...";
    }

}
