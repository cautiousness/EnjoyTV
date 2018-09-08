package com.fuj.enjoytv.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 常量类
 * Created by dell
 */
public class AppManager {
    public static final int RESULT_CODE_PLAY_PATH = 100;
    public static final int RESULT_CODE_LOGIN = 101;

    public static final String CONFIG = "config";

    public static final String BUNDLE_TVDET = "bundle_tvdet";
    public static final String BUNDLE_NOW = "bundle_now";
    public static final String BUNDLE_PLAYLIST = "bundle_playlist";
    public static final String BUNDLE_PLAY_PATH = "bundle_play_path";
    public static final String BUNDLE_USER_NAME = "bundle_user_name";
    public static final String BUNDLE_CHAT_DETAIL = "bundle_chat_detail";

    public static final String LOC_LAT = "loc_lat";
    public static final String LOC_LON = "loc_lon";

    public static final String PROVIDER = "com.fuj.enjoytv.hook.EnjoyProvider";
    public static final String STATE = Environment.getExternalStorageState();
    public static final File DIRECTORY = Environment.getExternalStorageDirectory();

    private static final String CACHE_DIR_NAME = "cache";
    private static final String IMAGE_DIR_NAME = "image";
    private static final String LOG_DIR_NAME = "log";

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
        LogUtils.getInstance().init(context);
    }

    public static synchronized String getAppName() {
        try {
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return mContext.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized String getVersionName() {
        try {
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized int getVersionCode() {
        try {
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static synchronized String getPackageName() {
        try {
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String cacheDir() {
        return DIRECTORY + File.separator + getPackageName() + File.separator + CACHE_DIR_NAME + File.separator;
    }

    public static String imageDir() {
        return DIRECTORY + File.separator + getPackageName() + File.separator + IMAGE_DIR_NAME + File.separator;
    }

    public static String logDir() {
        return DIRECTORY + File.separator + getPackageName() + File.separator + LOG_DIR_NAME + File.separator;
    }

    public static String dateAndTime() {
        return new SimpleDateFormat("yyyyMMdd hh:mm:ss", Locale.CHINA).format(new Date());
    }

    public static String shortDate() {
        return new SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(new Date());
    }

    public static String getTimeShort() {
        return new SimpleDateFormat("hh:mm:ss", Locale.CHINA).format(new Date());
    }

    public static String getImagePath(String path, int width, int high) {
        String[] name = path.split("/");
        String savePath = imageDir() + "temp_" + width + "_" + high + "_" + name[name.length - 1];
        File file = new File(savePath);
        if(file.exists()) {
            return file.getAbsolutePath();
        }
        return BitmapUtils.converPath(path, width, high, savePath);
    }
}
