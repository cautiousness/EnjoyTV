package com.fuj.enjoytv.base;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.anim.AnimUtils;
import com.fuj.enjoytv.tools.DialogHelper;
import com.fuj.enjoytv.tools.cache.ConCache;
import com.fuj.enjoytv.utils.ActivityUtils;
import com.fuj.enjoytv.utils.Constant;
import com.fuj.enjoytv.utils.DensityUtils;
import com.fuj.enjoytv.utils.LogUtils;
import com.fuj.enjoytv.utils.PreferenceUtils;
import com.fuj.enjoytv.utils.ToastUtils;

/**
 * Created by dell on 2016/5/19
 */
public abstract class BaseActivity extends AppCompatActivity {
    private boolean isNetworkOn;

    private TextView titleTV;
    private ActionBar ab;
    private DialogHelper dialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtils.i(getClass().getSimpleName());
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTheme(R.style.AppTheme_NoActionBar);
        setWindowFlag();
        dialogHelper = DialogHelper.getInstance();
        ActivityUtils.addActivity(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        ViewGroup container = (ViewGroup) View.inflate(this, R.layout.activity_toolbar, null);
        Toolbar mToolBar = (Toolbar) container.findViewById(R.id.id_tool_bar);
        titleTV = (TextView) mToolBar.findViewById(R.id.toolbar_title);
        titleTV.getLayoutParams().width = DensityUtils.dp2px(this, 2 * ConCache.getFloat(this, ConCache.DISPLAY_WIDTH) / 3);
        setSupportActionBar(mToolBar);
        onCreateCustomToolBar(mToolBar);

        ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(false);
            ab.setDisplayShowTitleEnabled(false);
            ab.setDisplayShowCustomEnabled(true);
            titleTV.setVisibility(View.GONE);
        }

        View mUserView = LayoutInflater.from(this).inflate(layoutResID, null);
        container.addView(mUserView);
        container.bringChildToFront(mToolBar);
        setContentView(container);
    }

    protected void setWindowFlag() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 获取状态栏高度
     * @param context 上下文
     * @return 状态栏高度
     */
    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if(resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.i(getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.i(getClass().getSimpleName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.i(getClass().getSimpleName());
    }

    private void onCreateCustomToolBar(Toolbar toolbar){
        toolbar.showOverflowMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }

    public ActionBar getToolBar() {
        return ab;
    }

    public void showToolBar() {
        ab.show();
    }

    public void hideToolBar() {
        ab.hide();
    }

    public void setBackToolbar() {
        ab.setDisplayHomeAsUpEnabled(true);
        titleTV.setVisibility(View.VISIBLE);
    }

    public void setTitle(String title) {
        titleTV.setText(title);
    }

    public void setTitle(int resId) {
        titleTV.setText(getString(resId));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtils.delActivity(this);
        /*RefWatcher refWatcher = PttAPP.getRefWatcher(this);
        refWatcher.watch(this);*/
    }

    public void showToast(int resId) {
        showToast(getString(resId));
    }

    public void showToast(String msg) {
        ToastUtils.showToast(this, msg);
    }

    public void showYES(int resId) {
        showYES(getString(resId));
    }

    public void showNO(int resId) {
        showNO(getString(resId));
    }

    protected void showYES(final String msg) {
        hideLoadingDialog();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showYESorNO(getApplication(), R.layout.widget_toast_yes, msg);
            }
        });
    }

    protected void showNO(final String msg) {
        hideLoadingDialog();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showYESorNO(getApplication(), R.layout.widget_toast_no, msg);
            }
        });
    }

    public void showLoadingDialog(int resId) {
        dialogHelper.showLoadingDialog(this, getString(resId));
    }

    /**
     * 展示耗时加载框
     * @param loadingMsg 加载框显示的信息
     */
    public void showLoadingDialog(String loadingMsg) {
        dialogHelper.showLoadingDialog(this, loadingMsg);
    }

    public void showProgressDialog(int progress) {
        dialogHelper.showProgressDialog(this, progress);
    }

    /**
     * 切换activity
     * @param clazz activity
     */
    public void showActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    protected void showActivityResult(Class<? extends Activity> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    protected void showActivityResult(Class<? extends Activity> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 切换activity
     * @param clazz activity
     * @param bundle 信息
     */
    public void showActivity(Class<? extends Activity> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void showActivity(Class<? extends Activity> clazz, Bundle bundle, Bundle options) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        startActivity(intent, options);
    }

    public void anim_jelly(View view) {
        AnimUtils.anim_jelly(view);
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
    /*protected void showOptionDialog(Context context, ListView optionLV, int height, CommonAdapter adapter, int gravity, int paramsX) {
        dialogHelper.showOptionDialog(context, optionLV, height, adapter, gravity, paramsX);
    }*/

    /**
     * 展示操作列表对话框
     * @param activity 窗口上下文
     * @param view 面板
     * @param yOffset 面板Yoffset
     * @param gravity 面板位置
     * @param paramsX 面板布局参数设置
     */
    /*protected void showFlexboxDialog(Activity activity, final View view, final int yOffset, final int gravity, final int paramsX) {
        dialogHelper.showFlexboxDialog(activity, view, yOffset, gravity, paramsX);
    }*/

    /**
     * 隐藏flexbox面板
     */
    protected void hideFlexboxDialog() {
        dialogHelper.hideFlexboxDialog();
    }

    /**
     * 隐藏操作列表对话框
     */
    protected void hideOptionDialog() {
        dialogHelper.hideOptionDialog();
    }

    /*public void hideProgressDialog() {
        dialogHelper.hideProgressDialog();
    }*/

    /**
     * 隐藏耗时加载框
     */
    public void hideLoadingDialog() {
        dialogHelper.hideLoadingDialog();
    }

    public String getPreString(String key) {
        return PreferenceUtils.readString(this, Constant.CONFIG, key);
    }

    public int getPreInt(String key) {
        return PreferenceUtils.readInt(this, Constant.CONFIG, key);
    }

    public void setPreString(String key, String value) {
        PreferenceUtils.write(this, Constant.CONFIG, key, value);
    }

    public void setPreInt(String key, int value) {
        PreferenceUtils.write(this, Constant.CONFIG, key, value);
    }

    /*public String getServer() {
        return getPreString(Constant.CONFIG_SERVER_ADDR);
    }*/

    public boolean getNetStatus() {
        return isNetworkOn;
    }

    public void setNetStatus(boolean status) {
        isNetworkOn = status;
    }

    public String getUserName() {
        return getPreString(Constant.BUNDLE_USER_NAME);
    }
}