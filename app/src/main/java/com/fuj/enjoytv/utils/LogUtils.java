package com.fuj.enjoytv.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class LogUtils {
    private static int LOGLEVEL = Log.INFO;
    private final static boolean LOGFLAG = true;
    private final static String TAG = Constant.getAppName();
    private static String log;
    private static File mLogFile;

    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private LogUtils() {}

    public static LogUtils getInstance() {
        return Singleton.Instance;
    }

    private static class Singleton {
        private final static LogUtils Instance = new LogUtils();
    }

    public void init(Context context) {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        mLogFile = new File(Constant.logDir() + Constant.shortDate() + ".log");
        //Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static void v(String msg) {
        v(msg == null ? "" : msg, null);
    }

    public static void v(String msg, Throwable thr) {
        if(LOGFLAG) {
            if(LOGLEVEL <= Log.VERBOSE) {
                android.util.Log.v(TAG, buildMessage(msg), thr);
                addRecordToLog(log);
            }
        }
    }

    public static void d(String msg) {
        d(msg == null ? "" : msg, null);
    }

    public static void d(String msg, Throwable thr) {
        if(LOGFLAG) {
            if(LOGLEVEL <= Log.DEBUG) {
                android.util.Log.d(TAG, buildMessage(msg), thr);
                addRecordToLog(log);
            }
        }
    }

    public static void i(String msg) {
        i(msg == null ? "" : msg, null);
    }

    public static void i(String msg, Throwable thr) {
        if(LOGFLAG) {
            if(LOGLEVEL <= Log.INFO) {
                android.util.Log.i(TAG, buildMessage(msg), thr);
                addRecordToLog(log);
            }
        }
    }

    public static void e(String msg) {
        e(msg == null ? "" : msg, null);
    }

    public static void e(String msg, Throwable thr) {
        if(LOGFLAG) {
            if(LOGLEVEL <= Log.ERROR) {
                android.util.Log.e(TAG, buildMessage(msg), thr);
                addRecordToLog(log);
            }
        }
    }

    public static void w(String msg) {
        w(msg == null ? "" : msg, null);
    }

    public static void w(Throwable thr) {
        w("", thr);
    }

    public static void w(String msg, Throwable thr) {
        if(LOGFLAG) {
            if(LOGLEVEL <= Log.WARN) {
                android.util.Log.w(TAG, buildMessage(msg), thr);
                addRecordToLog(log);
            }
        }
    }

    protected static String buildMessage(String msg) {
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[3];
        log = "[" + Constant.getTimeShort() + "]"
                + caller.getClassName() + "." + caller.getMethodName() + "(): " + msg;
        return log;
    }

    public static void addRecordToLog(String message) {
        File dir = new File(Constant.logDir());
        if (Environment.MEDIA_MOUNTED.equals(Constant.STATE)) {
            if(!dir.exists()) {
                Log.d("create dir", ", result = " + dir.mkdirs());
            }
            if (!mLogFile.exists())  {
                try  {
                    Log.d("create file", "result = " + mLogFile.createNewFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                BufferedWriter buf = new BufferedWriter(new FileWriter(mLogFile, true));
                buf.write(message + "\r\n");
                buf.newLine();
                buf.flush();
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public boolean handleException(Throwable ex) {
        if (ex == null)
            return false;
        saveCrashToFile(ex);
        return true;
    }

    private void saveCrashToFile(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }

        String result = writer.toString();
        sb.append(result);
        addRecordToLog(sb.toString());

        try {
            pw.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}