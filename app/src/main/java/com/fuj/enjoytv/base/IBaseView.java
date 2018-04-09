package com.fuj.enjoytv.base;

import android.content.Context;
import android.support.v7.app.ActionBar;

/**
 * Created by gang on 2016/5/10
 */
public interface IBaseView<T> {
    void setPresenter(T presenter);

    void showToast(int resId);

    void showToast(String msg);

    void showLoading(int resId);

    void hideLoading();

    boolean getWifiStatus();

    String getString(int resId);

    String getString(int resId, Object... formatArgs);

    String getServer();

    void setTitle(String title);

    void setTitle(int resId);

    ActionBar getToolBar();

    Context getContext();
}
