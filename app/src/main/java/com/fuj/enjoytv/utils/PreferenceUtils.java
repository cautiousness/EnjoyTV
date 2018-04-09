package com.fuj.enjoytv.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * sharedpreference工具类
 * @author dell
 *
 */
public class PreferenceUtils {
	public static void write(Context context, String SharedPreferencesName, String key, String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
		sharedPreferences.edit().putString(key, value).apply();
	}
	
	public static void write(Context context, String SharedPreferencesName, String key, long value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
		sharedPreferences.edit().putLong(key, value).apply();
	}

	public static void write(Context context, String SharedPreferencesName, String key, int value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
		sharedPreferences.edit().putInt(key, value).apply();
	}

	public static void write(Context context, String SharedPreferencesName, String key, Boolean value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
		sharedPreferences.edit().putBoolean(key, value).apply();
	}

	public static void write(Context context, String SharedPreferencesName, String key, double value) {
		write(context, SharedPreferencesName, key, Double.doubleToRawLongBits(value));
	}

	public static String readString(Context context, String SharedPreferencesName, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, "");
	}

	public static double readDouble(Context context, String SharedPreferencesName, String key) {
		return Double.longBitsToDouble(readLong(context, SharedPreferencesName, key));
	}

	public static long readLong(Context context, String SharedPreferencesName, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
		return sharedPreferences.getLong(key, 0);
	}

	public static int readInt(Context context, String SharedPreferencesName, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(key, 0);
	}

	public static Boolean readBoolean(Context context, String SharedPreferencesName, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(key, false);
	}

	public static boolean contains(Context context, String SharedPreferencesName, String key) {
        SharedPreferences sp = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        return sp.contains(key);  
    }
	
	public static Map<String, ?> getAll(Context context, String SharedPreferencesName) {
        SharedPreferences sp = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        return sp.getAll();  
    }
	
	public static void remove(Context context, String SharedPreferencesName, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
		sharedPreferences.edit().remove(key).apply();
	}
	
	public static void clear(Context context, String SharedPreferencesName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    } 
}
