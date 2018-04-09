package com.fuj.enjoytv.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.ref.SoftReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志工具
 * @author dell
 *
 */
public class LogUtils implements UncaughtExceptionHandler {
    public static int LOGLEVEL = Log.INFO;
    public static final boolean LOGFLAG = true;
    
    private static String log;
	private static final String FILENAME = "log";
    private static final String DIRNAME = "/higos/PTT/";
    private static String state = Environment.getExternalStorageState();
    private static LogUtils INSTANCE = new LogUtils();
	
    private Map<String, String> info = new HashMap<>();
    private SoftReference<Context> mContext;
    private UncaughtExceptionHandler mDefaultHandler;

    public static LogUtils getInstance() {
        return INSTANCE;
    }

    public LogUtils() {}

    public void init(Context context) {
        mContext = new SoftReference<>(context);
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

	public static void v(String msg) {
		v(msg == null ? "" : msg, null);
	}

	public static void v(String msg, Throwable thr) {
		if(LOGFLAG) {
			if(LOGLEVEL <= Log.VERBOSE) {
				Log.v(Constant.TAG, buildMessage(msg), thr);
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
				Log.d(Constant.TAG, buildMessage(msg), thr);
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
				Log.i(Constant.TAG, buildMessage(msg), thr);
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
				Log.e(Constant.TAG, buildMessage(msg), thr);
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
				Log.w(Constant.TAG, buildMessage(msg), thr);
				addRecordToLog(log);
			}
		}
	}

	protected static String buildMessage(String msg) {
		StackTraceElement caller = new Throwable().fillInStackTrace()
		        .getStackTrace()[3];
		log = "[" + SimpleDateFormat.getTimeInstance(DateFormat.DEFAULT)
				.format(new Date(System.currentTimeMillis())) + "]"
				+ caller.getClassName() + "." + caller.getMethodName() + "(): " + msg;
		return log;
	}

    public static void addRecordToLog(String message) {
        File dir = new File(Environment.getExternalStorageDirectory() + DIRNAME);
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if(!dir.exists()) {
                Log.d("Dir created ", ", result = " + dir.mkdirs());
            }
            File logFile = new File(Environment.getExternalStorageDirectory() + DIRNAME + FILENAME + ".txt");
            
            if(logFile.length() > 1000000) {
                Log.d("File delete ", "result = " + logFile.delete());
            }
            
            if (!logFile.exists())  {
                try  {
                    Log.d("File created ", "result = " + logFile.createNewFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try { 
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
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
        if(!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            ActivityUtils.exit();
        }
    }

    private boolean handleException(Throwable ex) {
        if(ex == null) {
            return false;
        }

        new Thread() {
            public void run() {
                Looper.prepare();
                ToastUtils.showToast(mContext.get(), "程序异常,3秒后退出");
                Looper.loop();
            }
        }.start();
        saveCrashToFile(ex);
        return true;
    }
  
    private void saveCrashToFile(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, String> entry : info.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\r\n");
        }  
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
