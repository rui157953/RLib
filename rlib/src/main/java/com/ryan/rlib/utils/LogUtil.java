package com.ryan.rlib.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Ryan on 2017/12/1.
 */

public class LogUtil {

    private static Context mContext;

    public static final int LEVEL_VERBOSE = 1;
    public static final int LEVEL_DEBUG = 2;
    public static final int LEVEL_INFO = 3;
    public static final int LEVEL_WARN = 4;
    public static final int LEVEL_ERROR = 5;
    public static final int LEVEL_NOTHING = 6;
    public static int LEVEL = LEVEL_VERBOSE;

    public static String TAG = "Ryan";

    public static void v(String tag,String msg){
        if(LEVEL <=LEVEL_VERBOSE){
            Log.v(tag,msg);
        }
    }
    public static void v(String msg){
        v(TAG,msg);
    }

    public static void d(String tag,String msg){
        if(LEVEL <=LEVEL_DEBUG){
            Log.d(tag,msg);
        }
    }
    public static void d(String msg){
        d(TAG,msg);
    }

    public static void i(String tag,String msg){
        if(LEVEL <=LEVEL_INFO){
            Log.i(tag,msg);
        }
    }
    public static void i(String msg){
        i(TAG,msg);
    }

    public static void w(String tag,String msg){
        if(LEVEL <=LEVEL_WARN){
            Log.w(tag,msg);
        }
    }
    public static void w(String msg){
        w(TAG,msg);
    }

    public static void e(String tag,String msg){
        if(LEVEL <=LEVEL_ERROR){
            Log.e(tag,msg);
        }
    }

    public static void e(String msg){
        e(TAG,msg);
    }

    public static void toast(Context context, String msg){
        if(LEVEL <=LEVEL_VERBOSE){
            Toast.makeText(getApplicationContext(context), msg, Toast.LENGTH_SHORT).show();
        }
    }

    private static Context getApplicationContext(Context context){
        if (mContext == null){
            mContext = context.getApplicationContext();
        }
        return mContext;
    }

}
