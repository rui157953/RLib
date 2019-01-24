package com.ryan.rlib.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ryan.
 */
public class Util {
    /**
     * 转为unicode 编码
     *
     * @return unicodeString
     */
    public static String encode(String str) {
        String prefix = "\\u";
        StringBuffer sb = new StringBuffer();
        char[] chars = str.toCharArray();
        if (chars == null || chars.length == 0) {
            return null;
        }
        for (char c : chars) {
            sb.append(prefix);
            sb.append(Integer.toHexString(c));
        }
        return sb.toString();
    }

    /**
     * 把unicode编码转换为中文
     *
     * @return
     */
    public static String decode(String str) {
        String sg = "\\u";
        int a = 0;
        List<String> list = new ArrayList<>();
        while (str.contains(sg)) {
            str = str.substring(2);
            String substring;
            if (str.contains(sg)) {
                substring = str.substring(0, str.indexOf(sg));
            } else {
                substring = str;
            }
            if (str.contains(sg)) {
                str = str.substring(str.indexOf(sg));
            }
            list.add(substring);
        }
        StringBuffer sb = new StringBuffer();
        if (list.size() > 0) {
            for (String string : list) {
                sb.append((char) Integer.parseInt(string, 16));
            }
        }
        return sb.toString();
    }

    public static String getAppFilePath(Context context, String dir) {
        String directoryPath = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//判断外部存储是否可用
            directoryPath = context.getExternalFilesDir(dir).getAbsolutePath();
        } else {//没外部存储就使用内部存储
            directoryPath = context.getFilesDir() + File.separator + dir;
        }
        File file = new File(directoryPath);
        if (!file.exists()) {//判断文件目录是否存在
            file.mkdirs();
        }
        return directoryPath;
    }


    public static String getPublicFilePath(Context context, String dir) {
        String directoryPath = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//判断外部存储是否可用
            directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {//没外部存储就使用内部存储
            directoryPath = context.getFilesDir() + File.separator + dir;
        }
        File file = new File(directoryPath);
        if (!file.exists()) {//判断文件目录是否存在
            file.mkdirs();
        }
        return directoryPath;
    }

    public static String getCurrentTimeString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String timeString = formatter.format(curDate);
        return timeString;
    }

    /**
     * 日期比较
     *
     *  date1 比 date2 大，则返回 1.
     *  date1 比 date2 小，则返回 -1.
     *  date1 等于 date2 小，则返回 0.
     *
     * @param date1
     * @param date2
     * @return
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
