package com.ryan.rlib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.RequiresPermission;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkUtil {
    public static final int NETWORK_STATE_AVAILABLE = 1;
    public static final int NETWORK_STATE_UNAVAILABLE = 0;
    public static int NETWORK_STATE = NETWORK_STATE_AVAILABLE;
    
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static int getNetworkState(Context context) {
        ConnectivityManager connectionManager = (ConnectivityManager) context.getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        if (connectionManager == null) {
            return NETWORK_STATE = NETWORK_STATE_UNAVAILABLE;
        }
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            NETWORK_STATE = NETWORK_STATE_AVAILABLE;
        } else {
            NETWORK_STATE = NETWORK_STATE_UNAVAILABLE;
        }
        
        return NETWORK_STATE;
    }
    
    /**
     * 检测当前网络的类型 是否是wifi
     *
     * @return 1:wifi or 2:非wifi
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static int checkedNetWorkType(Context context) {
        if (getNetworkState(context) == NETWORK_STATE_UNAVAILABLE) {
            return 0;//无网络
        }
        
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting()) {
            return 1;//wifi
            
        } else {
            return 2;//非wifi
        }
    }
    
}
