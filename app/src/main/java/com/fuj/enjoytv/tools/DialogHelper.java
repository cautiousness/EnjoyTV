package com.fuj.enjoytv.tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.tools.cache.ConCache;
import com.fuj.enjoytv.utils.LogUtils;

import java.lang.ref.SoftReference;

/**
 * Created by gang
 */
public class DialogHelper {
    private Dialog loadingDialog;
    private Dialog optionDialog;
    private Dialog flexboxDialog;
    private Dialog progressDialog;

    private SoftReference<Activity> mActivity;

    private static DialogHelper instance;

    private DialogHelper() {}

    public static DialogHelper getInstance() {
        if(instance == null) {
            synchronized (DialogHelper.class) {
                if(instance == null) {
                    instance = new DialogHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 展示耗时加载框
     * @param context 窗口上下文
     * @param loadingMsg 加载框显示的信息
     */
    public void showLoadingDialog(Context context, String loadingMsg) {
        synchronized(this) {
            if(loadingDialog != null) {
                loadingDialog.dismiss();
                loadingDialog = null;
            }

            View view = View.inflate(context, R.layout.dialog_loading, null);
            ImageView loadingIV = (ImageView) view.findViewById(R.id.dialog_loadingIV);
            TextView loadingTV = (TextView) view.findViewById(R.id.dialog_loadingTV);
            Animation loadingAnim = AnimationUtils.loadAnimation(context, R.anim.dialog_loading);
            loadingIV.startAnimation(loadingAnim);
            loadingTV.setText(loadingMsg);

            initLoadingDialog(context, view);

            if(!loadingDialog.isShowing()) {
                LogUtils.d("loading dialog is show = " + loadingDialog.isShowing());
                loadingDialog.show();
            }
        }
    }

    /**
     * 展示操作列表对话框
     * @param context 窗口上下文
     * @param optionLV 操作列表
     * @param height 列表高度
     * @param adapter 列表数据适配器
     * @param gravity 列表位置
     * @param paramsX 列表布局参数设置
     */
    /*public void showOptionDialog(Context context, ListView optionLV, int height, CommonAdapter adapter, int gravity, int paramsX) {
        optionLV.setAdapter(adapter);
        optionDialog = new Dialog(context, R.style.titleDialog);
        optionDialog.setContentView(optionLV);
        Window window = optionDialog.getWindow();
        if(window != null) {
            window.setWindowAnimations(R.style.dialogWindowAnim);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = gravity;
            layoutParams.x = DensityUtils.dp2px(context, paramsX);
            layoutParams.y = height;
            layoutParams.width = ConCache.getInt(context, ConCache.LOGIN_HEAD_WIDTH);
            layoutParams.dimAmount = 0.2f;
            optionDialog.onWindowAttributesChanged(layoutParams);
        }
        optionDialog.setCanceledOnTouchOutside(true);
        optionDialog.show();
    }*/

    /**
     * 展示操作列表对话框
     * @param activity 窗口上下文
     * @param view 面板
     * @param yOffset 面板Yoffset
     * @param gravity 面板位置
     * @param paramsX 面板布局参数设置
     */
    /*public void showFlexboxDialog(Activity activity, final View view, final int yOffset, final int gravity, final int paramsX) {
        mActivity = new SoftReference<>(activity);
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    flexboxDialog = new Dialog(mActivity.get(), R.style.flexboxDialog);
                    flexboxDialog.setContentView(view);
                    Window window = flexboxDialog.getWindow();
                    if(window != null) {
                        window.setWindowAnimations(R.style.flexboxWindowAnim);
                        WindowManager.LayoutParams layoutParams = window.getAttributes();
                        layoutParams.gravity = gravity;
                        layoutParams.x = DensityUtils.dp2px(mActivity.get(), paramsX);
                        layoutParams.y = yOffset;
                        layoutParams.width = DensityUtils.dp2px(mActivity.get(), ConCache.getFloat(mActivity.get(), ConCache.DISPLAY_WIDTH));
                        layoutParams.height = DensityUtils.dp2px(mActivity.get(), ConCache.getFloat(mActivity.get(), ConCache.DISPLAY_HEIGHT) / 2);
                        layoutParams.dimAmount = 0.2f;
                        flexboxDialog.onWindowAttributesChanged(layoutParams);
                    }
                    flexboxDialog.setCanceledOnTouchOutside(true);
                    flexboxDialog.show();
                }
            });
        }
    }*/

    public void showProgressDialog(Context context, int progress) {
        //showProgressDialog(context, progress, false);
    }

    /*public void showProgressDialog(Context context, int progress, boolean canceledOnTouchOutside) {
        synchronized(this) {
            if(progressDialog == null) {
                View view = View.inflate(context, R.layout.dialog_progress, null);
                progressbar = (ArcProgressbar) view.findViewById(R.id.dialog_progress);
                progressbar.setOnCenterDraw(new OnTextCenter());
                progressDialog = new Dialog(context, R.style.loading_dialog);
                progressDialog.setContentView(view);
                progressDialog.setCancelable(false);
                WindowManager.LayoutParams layoutParams = progressDialog.getWindow().getAttributes();
                layoutParams.gravity = Gravity.CENTER;
                int temp = (int) (ConCache.getInt(context, ConCache.LOGIN_HEAD_WIDTH) * 1.3);
                layoutParams.width = temp;
                layoutParams.height = temp;
                layoutParams.dimAmount = 0.4f;
                progressDialog.onWindowAttributesChanged(layoutParams);
                progressDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            }

            progressbar.setProgress(progress);

            if(!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }*/

    /**
     * 初始化耗时加载框
     * @param context 窗口上下文
     * @param view 视图
     */
    private void initLoadingDialog(Context context, View view) {
        loadingDialog = new Dialog(context, R.style.loading_dialog);
        loadingDialog.setContentView(view);
        loadingDialog.setCancelable(false);
        WindowManager.LayoutParams layoutParams = loadingDialog.getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = ConCache.getInt(context, ConCache.LOGIN_HEAD_WIDTH);
        layoutParams.height = ConCache.getInt(context, ConCache.LOGIN_HEAD_WIDTH);
        layoutParams.dimAmount = 0.4f;
        loadingDialog.onWindowAttributesChanged(layoutParams);
        loadingDialog.setCanceledOnTouchOutside(true);
    }

    /**
     * 隐藏flexbox面板
     */
    public void hideFlexboxDialog() {
        if(flexboxDialog != null) {
            flexboxDialog.dismiss();
        }
    }

    /**
     * 隐藏操作列表对话框
     */
    public void hideOptionDialog() {
        if(optionDialog != null) {
            optionDialog.dismiss();
        }
    }

    /**
     * 隐藏耗时加载框
     */
    public void hideLoadingDialog() {
        if(loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}
