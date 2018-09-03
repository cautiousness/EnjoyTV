package com.fuj.enjoytv;

import android.os.Looper;
import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.fuj.enjoytv.activity.main.MainActivity;
import com.fuj.enjoytv.utils.LogUtils;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

/**
 * Created by gang
 */
public class EnjoyApp extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        initSDK();
    }

    private void initSDK() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Looper.prepare();
                    LeakCanary.install(EnjoyApp.this);
                    LogUtils.getInstance().init(EnjoyApp.this);
                    SDKInitializer.initialize(EnjoyApp.this);
                    Bugly.init(getApplicationContext(), "738a7261f6", false);
                    Beta.checkUpgrade(false, false);
                    Beta.canShowUpgradeActs.add(MainActivity.class);
                    Looper.loop();
                } catch (Exception e) {
                    LogUtils.e(" [error] " + e);
                }
            }
        }).start();
    }
}
