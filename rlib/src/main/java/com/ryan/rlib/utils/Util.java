package com.ryan.rlib.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Base64;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Ryan.
 */
public class Util {
    
    private static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 转为unicode编码
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
     * 把unicode转换为中文
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
    
    /** 根据手机的分辨率从 dp 的单位 转成为 px(像素) */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    /** 根据手机的分辨率从 px(像素) 的单位 转成为 dp */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    
    /**
     * Md5 32位 or 16位 加密
     *
     * @return 32位加密
     */
    public static String Md5(String plainText) {
        StringBuffer buf = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) i += 256;
                if (i < 16) buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }
    
    
    
    /**
     * 正则：手机号（精确）
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198</p>
     * <p>联通：130、131、132、145、155、156、175、176、185、186、166</p>
     * <p>电信：133、153、173、177、180、181、189、199</p>
     * <p>全球星：1349</p>
     * <p>虚拟运营商：170</p>
     */
    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
//    public static final String REGEX_MOBILE_EXACT = "^(13\d|14[579]|15[^4\D]|17[^49\D]|18\d)\d{8}";
//    public static final String REGEX_MOBILE_EXACT = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])d{8}$";
    /**
     * 手机号正则判断
     */
    public static boolean isPhoneNumber(String str) throws PatternSyntaxException {
        if (str != null) {
            String pattern;
            pattern = REGEX_MOBILE_EXACT;
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(str);
            return m.matches();
        } else {
            return false;
        }
    }
    
    
    /**
     * 字符串进行Base64编码
     */
    public static String StringToBase64(String str) {
        String encodedString = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
        return encodedString;
    }
    
    /**
     * 字符串进行Base64解码
     */
    public static String Base64ToString(String encodedString) {
        String decodedString = new String(Base64.decode(encodedString, Base64.DEFAULT));
        return decodedString;
    }
    
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
