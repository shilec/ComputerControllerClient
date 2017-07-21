package com.scott.computercontrollerclient.utils;

import android.util.Log;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/6/19.</p>
 * <p>Email:     shijl5@lenovo.com</p>
 * <p>Describe:</p>
 */

public class Logger {

    public static boolean DEBUG_ABLE = true;
    interface  LOGLEVEL {
        int I = 0;
        int V = 1;
        int W = 2;
        int D = 3;
        int E = 4;
    }

    public static void showLog(String tag,String msg,int level) {

        if(!DEBUG_ABLE) return;
        switch (level) {
            case LOGLEVEL.I:
                Log.i(tag,msg);
                break;
            case LOGLEVEL.D:
                Log.d(tag,msg);
                break;
            case LOGLEVEL.E:
                Log.e(tag,msg);
                break;
        }
    }

    public static void i(String tag,String msg) {
        showLog(tag,msg, LOGLEVEL.I);
    }

    public static void e(String tag,String msg) {
        showLog(tag,msg, LOGLEVEL.E);
    }

    public static void d(String tag,String msg) {
        showLog(tag,msg, LOGLEVEL.D);
    }
}
