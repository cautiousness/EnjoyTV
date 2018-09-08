package com.fuj.enjoytv.tools.cache;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.SparseArray;

import com.fuj.enjoytv.utils.DensityUtils;

/**
 * @author dell
 */
public class ConCache {
    public static final int GROUP_LOGO_WIDTH = 0x01;
    public static final int GROUP_LOGO_HEIGHT = 0x02;
    public static final int GROUP_SWIPEEDIT_WIDTH = 0x03;
    public static final int GROUP_NAMEEDIT_WIDTH = 0x04;
    public static final int LOGIN_HEAD_WIDTH = 0x05;
    public static final int MAP_DIALOG_WIDTH = 0x06;
    public static final int RADIO_PANEL_HEIGHT = 0x07;
    public static final int RADIO_BUTTON_WIDTH = 0x08;
    public static final int RADIO_MEMBER_HEAD_WIDTH = 0x09;
    public static final int REGIST_RESEND_WIDTH = 0x10;
    public static final int GROUP_ICON_WIDTH = 0x11;
    public static final int LOGIN_TEXT_SIZE = 0x28;
    public static final int DISPLAY_WIDTH = 0x29;
    public static final int DISPLAY_HEIGHT = 0x30;
    public static final int SERVER_ADDR = 0x50;

    public static SparseArray<Integer> caches = new SparseArray<>();
    public static SparseArray<Float> cachesFloat = new SparseArray<>();
    public static SparseArray<String> cachesString = new SparseArray<>();
    private static ConCache instance;

    public static ConCache getInstance() {
        if(instance == null) {
            instance = new ConCache();
        }
        return instance;
    }

	@SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void init(Context context) {
        float display_width;
        float display_height;
        if (Build.VERSION.SDK_INT >= 13) {
            Point dims = new Point();
            ((Activity)context).getWindowManager().getDefaultDisplay().getSize(dims);
            display_width = DensityUtils.px2dp(context, dims.x);
            display_height = DensityUtils.px2dp(context, dims.y);
		} else {
            display_width = DensityUtils.px2dp(context, ((Activity)context).getWindowManager().getDefaultDisplay().getWidth());
            display_height = DensityUtils.px2dp(context, ((Activity)context).getWindowManager().getDefaultDisplay().getHeight());
		}

        caches.put(GROUP_LOGO_WIDTH, dp2px(context, display_width / 2));
        caches.put(GROUP_LOGO_HEIGHT, dp2px(context, display_width * 3 / 8));
        caches.put(GROUP_ICON_WIDTH, dp2px(context, display_width / 8));
        caches.put(GROUP_SWIPEEDIT_WIDTH, dp2px(context, display_width / 4));
        caches.put(GROUP_NAMEEDIT_WIDTH, dp2px(context, display_width * 2 / 3));
        caches.put(LOGIN_HEAD_WIDTH, dp2px(context, display_width / 3));
        caches.put(MAP_DIALOG_WIDTH, dp2px(context, display_width * 2 / 3));
        caches.put(RADIO_PANEL_HEIGHT, dp2px(context, display_height / 4));
        caches.put(RADIO_BUTTON_WIDTH, dp2px(context, display_width / 3));
        caches.put(RADIO_MEMBER_HEAD_WIDTH, dp2px(context, display_width / 7));
        caches.put(REGIST_RESEND_WIDTH, dp2px(context, display_width / 4));

        cachesFloat.put(LOGIN_TEXT_SIZE, display_width / 18);
        cachesFloat.put(DISPLAY_WIDTH, display_width);
        cachesFloat.put(DISPLAY_HEIGHT, display_height);
	}

    public static Integer getInt(Context context, int key) {
        if(caches == null || caches.size() == 0 || caches.get(key) == null) {
            getInstance().init(context);
        }

        return caches.get(key);
    }

    public static float getFloat(Context context, int key) {
        if(cachesFloat == null || cachesFloat.size() == 0 || cachesFloat.get(key) == null) {
            getInstance().init(context);
        }

        return cachesFloat.get(key);
    }

    public static String getStr(Context context, int key) {
        if(cachesString == null || cachesString.size() == 0 || cachesString.get(key) == null) {
            getInstance().init(context);

        }

        return cachesString.get(key);
    }

    private int dp2px(Context context, float value) {
        return DensityUtils.dp2px(context, value);
    }
}
