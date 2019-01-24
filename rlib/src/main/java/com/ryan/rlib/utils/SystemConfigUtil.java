package com.ryan.rlib.utils;

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.LocaleList;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.WindowManager;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;

public class SystemConfigUtil {
    
    public static String getCountry() {
        Locale locale = Locale.getDefault();
        return locale.getCountry();
    }
    
    public static boolean setCountry(String country) {
        Locale locale;
        if (TextUtils.equals("US", country)) {
            locale = Locale.US;
        } else if (TextUtils.equals("CN", country)) {
            locale = Locale.SIMPLIFIED_CHINESE;
        } else {
            locale = new Locale("th", "TH");
        }
        
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return changeSystemLanguage(locale);
        } else {
            return changeSystemLanguage(new LocaleList(locale));
        }
    }
    
    /**
     * Android 6.0
     */
    private static boolean changeSystemLanguage(Locale locale) {
        if (locale != null) {
            try {
                Class classActivityManagerNative = Class.forName("android.app.ActivityManagerNative");
                Method getDefault = classActivityManagerNative.getDeclaredMethod("getDefault");
                Object objIActivityManager = getDefault.invoke(classActivityManagerNative);
                Class classIActivityManager = Class.forName("android.app.IActivityManager");
                Method getConfiguration = classIActivityManager.getDeclaredMethod("getConfiguration");
                Configuration config = (Configuration) getConfiguration.invoke(objIActivityManager);
                config.setLocale(locale);
                //config.userSetLocale = true;
                Class clzConfig = Class
                        .forName("android.content.res.Configuration");
                java.lang.reflect.Field userSetLocale = clzConfig
                        .getField("userSetLocale");
                userSetLocale.set(config, true);
                Class[] clzParams = {Configuration.class};
                Method updateConfiguration = classIActivityManager.getDeclaredMethod("updateConfiguration", clzParams);
                updateConfiguration.invoke(objIActivityManager, config);
                BackupManager.dataChanged("com.android.providers.settings");
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
    
    /**
     * Android 7.0
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private static boolean changeSystemLanguage(LocaleList locale) {
        if (locale != null) {
            try {
                Class classActivityManagerNative = Class.forName("android.app.ActivityManagerNative");
                Method getDefault = classActivityManagerNative.getDeclaredMethod("getDefault");
                Object objIActivityManager = getDefault.invoke(classActivityManagerNative);
                Class classIActivityManager = Class.forName("android.app.IActivityManager");
                Method getConfiguration = classIActivityManager.getDeclaredMethod("getConfiguration");
                Configuration config = (Configuration) getConfiguration.invoke(objIActivityManager);
                config.setLocales(locale);
                Class[] clzParams = {Configuration.class};
                Method updateConfiguration = classIActivityManager.getDeclaredMethod("updatePersistentConfiguration", clzParams);
                updateConfiguration.invoke(objIActivityManager, config);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
    
    public static String getIPAddress(Context context){
        WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        int ipAddress = 0;
        if (wifiManager != null) {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            ipAddress = wifiInfo.getIpAddress();
            LogUtil.i("ipAddress(int):"+ipAddress);
            return intToIp(ipAddress);
        }
        return null;
    }
    
    public static String getInetIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr
                        .hasMoreElements();) {
                    InetAddress inetAddress = ipAddr.nextElement();
                    return inetAddress.getHostAddress();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);
    }
    
    public static int[] getDisplayDimension(Context context){
        int [] dimension = new int[2];
        WindowManager wm1 = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm1.getDefaultDisplay().getWidth();
        int height = wm1.getDefaultDisplay().getHeight();
        if (width<height){
            dimension[0] = width;
            dimension[1] = height;
        }else {
            dimension[1] = width;
            dimension[0] = height;
        }
        return dimension;
    }
}
