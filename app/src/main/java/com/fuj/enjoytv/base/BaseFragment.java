package com.fuj.enjoytv.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.fuj.enjoytv.tools.DialogHelper;
import com.fuj.enjoytv.utils.Constant;
import com.fuj.enjoytv.utils.DensityUtils;
import com.fuj.enjoytv.utils.LogUtils;
import com.fuj.enjoytv.utils.PreferenceUtils;
import com.fuj.enjoytv.utils.ToastUtils;
import com.fuj.enjoytv.utils.ValidUtils;
import com.fuj.enjoytv.widget.comm.ScrollChildSwipeRefreshLayout;

/**
 * Created by gang on 2016/5/11
 */
public abstract class BaseFragment extends Fragment {
    protected ScrollChildSwipeRefreshLayout swipeRL;
    protected ViewGroup root;
    protected View title;
    protected TextView titleTV;

    private Dialog flexboxDialog;
    private Dialog optionDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.i(getClass().getSimpleName());
        findViews(inflater, container, savedInstanceState);
        initTitle(inflater, container, savedInstanceState);
        return root;
    }

    /**
     * 初始化标题栏
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     */
    protected void initTitle(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {}

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i(getClass().getSimpleName());
        setListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.i(getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.i(getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*RefWatcher refWatcher = PttAPP.getRefWatcher(getActivity());
        refWatcher.watch(this);*/
    }

    /**
     * 初始化页面
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     */
    protected abstract void findViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * 初始化标题栏视图
     * @param viewId 资源id
     * @return 标题栏
     */
    protected View findTitle(int viewId) {
        return title.findViewById(viewId);
    }

    /**
     * 初始化界面
     * @param viewId 资源id
     * @return 视图
     */
    protected View findViewById(int viewId) {
        return root.findViewById(viewId);
    }

    /**
     * 获取drawable
     * @param drawableId drawable id
     * @return drawable
     */
    public Drawable getResDrawable(int drawableId) {
        return getResources().getDrawable(drawableId);
    }

    /**
     * 获取颜色
     * @param colorId 颜色资源id
     * @return 颜色
     */
    public int getResColor(int colorId) {
        return getResources().getColor(colorId);
    }

    /**
     * 展示系统toast
     * @param msg 信息
     */
    public void showToast(final String msg) {
        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast(getActivity().getApplication(), msg);
                }
            });
        }
    }

    /**
     * 展示成功时的toast
     * @param resId 资源id
     */
    protected void showYES(int resId) {
        showYES(getString(resId));
    }

    /**
     * 展示失败时的toast
     * @param resId 资源id
     */
    protected void showNO(int resId) {
        showNO(getString(resId));
    }

    /**
     * 展示成功时的toast
     * @param msg 提示信息
     */
    protected void showYES(String msg) {
        if(getActivity() != null) {
            getBaseActivity().showYES(msg);
        }
    }

    /**
     * 展示失败时的toast
     * @param msg 提示信息
     */
    protected void showNO(String msg) {
        if(getActivity() != null) {
            getBaseActivity().showNO(msg);
        }
    }

    /**
     * 展示系统toast
     * @param resId 信息id
     */
    public void showToast(int resId) {
        showToast(getString(resId));
    }

    /**
     * 展示网络错误代码信息
     * @param code 错误码
     */
    public void showErrorCode(int code) {
        //HttpError.showHttpResult(getActivity(), code);
    }

    /**
     * 设置视图宽高
     * @param view 视图
     * @param w 宽
     * @param h 高
     */
    protected void setWH(View view, int w, int h) {
        if(view == null) {
            return;
        }

        if(w != 0) {
            view.getLayoutParams().width = w;
        }

        if(h != 0) {
            view.getLayoutParams().height = h;
        }
    }

    protected void setMarginTop(View view) {
        setViewMargins(view, 0, DensityUtils.dp2px(getActivity(), 70), 0, 0);
    }

    /**
     * 设置视图边距
     * @param view 视图
     * @param left 左边距
     * @param top 上边距
     * @param right 右边距
     * @param bottom 底边距
     */
    protected void setViewMargins(View view, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams paramTest2 = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        paramTest2.setMargins(left, top, right, bottom);
        view.requestLayout();
    }

    public int getStatusBarHeight() {
        return getBaseActivity().getStatusBarHeight(getContext());
    }

    /**
     * 切换activity
     * @param clazz activity
     */
    public void showActivity(Class<? extends Activity> clazz) {
        getBaseActivity().showActivity(clazz);
    }

    protected void showActivityResult(Class<? extends Activity> clazz, int requestCode) {
        getBaseActivity().showActivityResult(clazz, requestCode);
    }

    protected void showActivityResult(Class<? extends Activity> clazz, int requestCode, Bundle bundle) {
        getBaseActivity().showActivityResult(clazz, requestCode, bundle);
    }

    /**
     * 切换activity
     * @param clazz activity
     * @param bundle 信息
     */
    public void showActivity(Class<? extends Activity> clazz, Bundle bundle) {
        getBaseActivity().showActivity(clazz, bundle);
    }

    public void showActivity(Class<? extends Activity> clazz, Bundle bundle, Bundle options) {
        getBaseActivity().showActivity(clazz, bundle, options);
    }

    /**
     * 获取标题栏
     * @return 标题栏
     */
    public ActionBar getToolBar() {
        return getBaseActivity().getToolBar();
    }

    public void hideToolBar() {
        getBaseActivity().hideToolBar();
    }

    public int getToolbarHeight() {
        return getToolBar().getHeight() - getStatusBarHeight();
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public void setPreString(String key, String s) {
        PreferenceUtils.write(getContext(), Constant.CONFIG, key, s);
    }

    public void setPreLong(String key, long value) {
        PreferenceUtils.write(getContext(), Constant.CONFIG, key, value);
    }

    public void setPreDouble(String key, double value) {
        PreferenceUtils.write(getContext(), Constant.CONFIG, key, value);
    }

    public void setPreInt(String key, int value) {
        PreferenceUtils.write(getContext(), Constant.CONFIG, key, value);
    }

    public void setPreBoolean(String key, boolean value) {
        PreferenceUtils.write(getContext(), Constant.CONFIG, key, value);
    }

    public String getPreString(String key) {
        return PreferenceUtils.readString(getContext(), Constant.CONFIG, key);
    }

    public double getDouble(String key) {
        return PreferenceUtils.readDouble(getContext(), Constant.CONFIG, key);
    }

    public long getPreLong(String key) {
        return PreferenceUtils.readLong(getContext(), Constant.CONFIG, key);
    }

    public boolean getPreBoolean(String key) {
        return PreferenceUtils.readBoolean(getContext(), Constant.CONFIG, key);
    }

    public String getEditString(EditText et) {
        return ValidUtils.getEditString(et);
    }

    public boolean getWifiStatus() {
        return ((BaseActivity)getActivity()).getNetStatus();
    }

    protected void setViewVisible(View view, boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置监听事件
     */
    protected void setListeners() {}

    public void setTitle(int resId) {
        setTitle(getString(resId));
    }

    public void setTitle(String title) {
        getBaseActivity().setTitle(title);
    }

    public void setCustomView() {
        getToolBar().setCustomView(title);
    }

    public void showLoading(int resId) {
        showLoadingDialog(getActivity(), getString(resId));
    }

    public void hideLoading() {
        hideLoadingDialog();
    }

    /**
     * 设置是否刷新
     * @param isRefresh 是否刷新
     */
    public void setLoadingIndicator(final boolean isRefresh) {
        if (getView() == null) {
            return;
        }

        swipeRL.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRL.setRefreshing(isRefresh);
            }
        }, 500);
    }

    protected void setSwipeRL(int primary, int accent, int primaryDark, View view) {
        setMarginTop(swipeRL);
        swipeRL.setColorSchemeColors(
            ContextCompat.getColor(getActivity(), primary),
            ContextCompat.getColor(getActivity(), accent),
            ContextCompat.getColor(getActivity(), primaryDark)
        );
        swipeRL.setScrollUpChild(view);
    }

    /**
     * 展示耗时加载框
     * @param context 窗口上下文
     * @param loadingMsg 加载框显示的信息
     */
    private void showLoadingDialog(Context context, String loadingMsg) {
        DialogHelper.getInstance().showLoadingDialog(context, loadingMsg);
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
        window.setWindowAnimations(R.style.dialogWindowAnim);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = gravity;
        layoutParams.x = DensityUtils.dp2px(context, paramsX);
        layoutParams.y = height;
        layoutParams.width = ConCache.getInt(context, ConCache.LOGIN_HEAD_WIDTH);
        layoutParams.dimAmount = 0.2f;
        optionDialog.onWindowAttributesChanged(layoutParams);
        optionDialog.setCanceledOnTouchOutside(true);
        optionDialog.show();
    }*/

    /**
     * 展示操作列表对话框
     * @param context 窗口上下文
     * @param view 面板
     * @param yOffset 面板Yoffset
     * @param gravity 面板位置
     * @param paramsX 面板布局参数设置
     */
    /*public void showFlexboxDialog(Context context, final View view, final int yOffset, final int gravity, final int paramsX) {
        flexboxDialog = new Dialog(context, R.style.flexboxDialog);
        flexboxDialog.setContentView(view);
        Window window = flexboxDialog.getWindow();
        window.setWindowAnimations(R.style.flexboxWindowAnim);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = gravity;
        layoutParams.x = DensityUtils.dp2px(context, paramsX);
        layoutParams.y = yOffset;
        layoutParams.width = DensityUtils.dp2px(context, ConCache.getFloat(context, ConCache.DISPLAY_WIDTH));
        layoutParams.height = DensityUtils.dp2px(context, ConCache.getFloat(context, ConCache.DISPLAY_HEIGHT) / 2);
        layoutParams.dimAmount = 0.2f;
        flexboxDialog.onWindowAttributesChanged(layoutParams);
        flexboxDialog.setCanceledOnTouchOutside(true);
        flexboxDialog.show();
    }*/

    /**
     * 隐藏flexbox面板
     */
    protected void hideFlexboxDialog() {
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

    public String getUserName() {
        return getBaseActivity().getUserName();
    }

    public void setUser(String name) {
        getBaseActivity().setUser(name);
    }

    public boolean isLogin() {
        return null != getUserName() && !"".equals(getUserName());
    }

    /**
     * 隐藏耗时加载框
     */
    private void hideLoadingDialog() {
        DialogHelper.getInstance().hideLoadingDialog();
    }

    public String getServer() {
        return "";
    }
}
