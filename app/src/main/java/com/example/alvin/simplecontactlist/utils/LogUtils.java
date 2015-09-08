package com.example.alvin.simplecontactlist.utils;

/**
 * Created by maruilin on 15/9/8.
 */
public class LogUtils {
    private final static boolean ON = true;
    public static void d(String tag, String msg) {
        if (ON) {
            android.util.Log.d(tag, msg);
        }
    }
}
