package com.ryan.rlib.utils;

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.LocaleList;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
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
    
    /**
     * 检测GPS是否打开
     *
     * @return
     */
    public static boolean checkGPSIsOpen(Context context) {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            isOpen = true;
        } else {
            isOpen = false;
        }
        return isOpen;
    }
    
    /**
     * 跳转GPS设置
     */
    public static void openGPSSettings(final Context context) {
        if (checkGPSIsOpen(context)) {
            //            initLocation();
            //自己写的定位方法
        } else {
            // 没有打开则弹出对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(context/*, R.style.AlertDialogCustom*/);
            builder.setTitle("温馨提示");
            builder.setMessage("当前应用需要打开定位功能。请点击\"设置\"-\"定位服务\"打开定位功能。");
            //设置对话框是可取消的
            builder.setCancelable(false);
            builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();                    //跳转GPS设置界面
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
//                    ActivityManager.getInstance().exit();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
