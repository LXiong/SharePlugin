package com.tangzi.base.utils;

import android.util.Log;

/**
 * 日志类
 * Created by liubin on 2017/10/20.
 */

public class LogUtils {
    private static int logLevel = Level.VERBOSE;
    private static int printLevel = Level.ERROR;
    private static final String TAG = "laulester";//默认tag

    public static void setLogLevel(int logLevel) {
        LogUtils.logLevel = logLevel;
    }

    public static void setPrintLevel(int printLevel) {
        LogUtils.printLevel = printLevel;
    }

    private LogUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (logLevel <= Level.VERBOSE) {
            if (tr == null) {
                Log.v(tag, msg);
            } else {
                Log.v(tag, msg, tr);
            }
        }
        if (printLevel <= Level.VERBOSE) {
            saveFile(tag, msg, tr);
        }
    }

    public static void v(String tag, String msg) {
        v(tag, msg, null);
    }

    public static void v(String msg, Throwable tr) {
        v(TAG, msg, tr);
    }

    public static void v(String msg) {
        v(TAG, msg, null);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (logLevel <= Level.DEBUG) {
            if (tr == null) {
                Log.d(tag, msg);
            } else {
                Log.d(tag, msg, tr);
            }
        }
        if (printLevel <= Level.DEBUG) {
            saveFile(tag, msg, tr);
        }
    }

    public static void d(String tag, String msg) {
        d(tag, msg, null);
    }

    public static void d(String msg, Throwable tr) {
        d(TAG, msg, tr);
    }

    public static void d(String msg) {
        d(TAG, msg, null);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (logLevel <= Level.INFO) {
            if (tr == null) {
                Log.i(tag, msg);
            } else {
                Log.i(tag, msg, tr);
            }
        }
        if (printLevel <= Level.INFO) {
            saveFile(tag, msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        i(tag, msg, null);
    }

    public static void i(String msg, Throwable tr) {
        i(TAG, msg, tr);
    }

    public static void i(String msg) {
        i(TAG, msg, null);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (logLevel <= Level.WARN) {
            if (tr == null) {
                Log.w(tag, msg);
            } else {
                Log.w(tag, msg, tr);
            }
        }
        if (printLevel <= Level.WARN) {
            saveFile(tag, msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        d(tag, msg, null);
    }

    public static void w(String msg, Throwable tr) {
        d(TAG, msg, tr);
    }

    public static void w(String msg) {
        d(TAG, msg, null);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (logLevel <= Level.ERROR) {
            if (tr == null) {
                Log.e(tag, msg);
            } else {
                Log.e(tag, msg, tr);
            }
        }
        if (printLevel <= Level.WARN) {
            saveFile(tag, msg, tr);
        }
    }

    public static void e(String tag, String msg) {
        e(tag, msg);
    }

    public static void e(String msg, Throwable tr) {
        e(TAG, msg, tr);
    }

    public static void e(String msg) {
        e(TAG, msg, null);
    }


    private static void saveFile(String tag, String msg, Throwable tr) {

    }

    public static class Level {
        public static final int VERBOSE = 2;
        public static final int DEBUG = 3;
        public static final int INFO = 4;
        public static final int WARN = 5;
        public static final int ERROR = 6;
    }
}




