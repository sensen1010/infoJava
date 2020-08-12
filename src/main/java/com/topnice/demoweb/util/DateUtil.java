package com.topnice.demoweb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @return
     */

    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /**
     * @desc: string转时间戳
     * @author: sen
     * @date: 2020/8/12 0012 10:54
     **/
    public static long timeDateStamp(String time, String format) {
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date dateStart = sdf.parse(time);
            return (dateStart.getTime() / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String date2TimeStamp(Date date, String format) {
        if (date == null) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String addOneday(String today, int i) {
        try {
            long time = Long.parseLong(today) + 24 * 3600 * i;

            return timeStamp2Date(time + "", null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return today;
        }
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 取得当前时间戳（精确到秒）
     *
     * @return
     */
    public static int timeStamp() {
        long time = System.currentTimeMillis();
        String t = String.valueOf(time / 1000);
        return (int) (time / 1000);
    }

    /**
     * 取得当天0点时间戳（精确到秒）
     *
     * @return
     */
    public static long getTomorrowBegin() {
        long now = System.currentTimeMillis() / 1000l;
        long daySecond = 60 * 60 * 24;
        long dayTime = now - (now + 8 * 3600) % daySecond;
        return dayTime;
    }
}
