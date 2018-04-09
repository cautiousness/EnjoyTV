package com.fuj.enjoytv;

import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.fuj.enjoytv.activity.main.MainActivity;
import com.fuj.enjoytv.utils.LogUtils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

/**
 * Created by gang
 */
public class EnjoyApp extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //refWatcher = LeakCanary.install(this);
        try {
            LogUtils.getInstance().init(this);
            SDKInitializer.initialize(this);
            Bugly.init(getApplicationContext(), "738a7261f6", false);
            Beta.checkUpgrade(false, false);
            Beta.canShowUpgradeActs.add(MainActivity.class);
        } catch (Exception e) {
            LogUtils.e(" [error] " + e);
        }
    }
}
