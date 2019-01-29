package com.ryan.rlib.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAndTimeUtil {
    
    private static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 获取时间日期String
     *
     * @param format 时间日期格式，填null 返回格式
     * @return 时间日期String
     */
    public static String getCurrentTimeString(String format) {
        SimpleDateFormat formatter;
        try {
            formatter = new SimpleDateFormat(format);
        } catch (Exception e) {
            formatter = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
        }
        Date curDate = new Date(System.currentTimeMillis());
        
        return formatter.format(curDate);
    }
    
    /**
     * 日期比较
     * <p>
     * date1 比 date2 大，则返回 1.
     * date1 比 date2 小，则返回 -1.
     * date1 等于 date2 小，则返回 0.
     */
    public static int compareDate(String date1, String date2) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = formatter.parse(date1);
            Date dt2 = formatter.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                //dt1在dt2后
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                //dt1在dt2前
                return -1;
            } else {
                ////dt1 == dt2
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
}
