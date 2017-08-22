package com.liaoyun56.template.utils;

import android.util.Log;
import com.liaoyun56.template.BuildConfig;

/**
 * Created by liaoyun on 2017/8/22.
 */
public class LogUtil {

    private final static String LOG_TAG = "template";

    public static void debug(String msg){
        if(BuildConfig.SHOW_LOG) {
            Log.d(LOG_TAG, "====> " + msg);
        }
    }

    public static void info(String msg){
        if(BuildConfig.SHOW_LOG) {
            Log.i(LOG_TAG, "====> " + msg);
        }
    }

    public static void warn(String msg){
        if(BuildConfig.SHOW_LOG){
            Log.w(LOG_TAG, "====> " + msg);
        }
    }

    public static void error(String msg){
        if(BuildConfig.SHOW_LOG) {
            Log.e(LOG_TAG, "====> " + msg);
        }
    }
}
