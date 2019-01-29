package com.ryan.rlib.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.WindowManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class DeviceUtils {
    
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
    
    public static int[] getDisplayDimension(Context context) {
        int[] dimension = new int[2];
        WindowManager wm1 = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm1.getDefaultDisplay().getWidth();
        int height = wm1.getDefaultDisplay().getHeight();
        if (width < height) {
            dimension[0] = width;
            dimension[1] = height;
        } else {
            dimension[1] = width;
            dimension[0] = height;
        }
        return dimension;
    }
}
