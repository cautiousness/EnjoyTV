package com.fuj.enjoytv.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.activity.main.chat.ChatFragment;
import com.fuj.enjoytv.activity.main.chat.ChatPresenter;
import com.fuj.enjoytv.activity.main.now.NowFragment;
import com.fuj.enjoytv.activity.main.now.NowPresenter;
import com.fuj.enjoytv.activity.main.tv.TVFragment;
import com.fuj.enjoytv.activity.main.tv.TVPresenter;
import com.fuj.enjoytv.activity.main.user.UserFragment;
import com.fuj.enjoytv.activity.main.user.UserPresenter;
import com.fuj.enjoytv.base.BaseActivity;
import com.fuj.enjoytv.base.BaseFragment;
import com.fuj.enjoytv.utils.Constant;
import com.fuj.enjoytv.utils.LogUtils;
import com.fuj.enjoytv.utils.PermissionUtils;
import com.fuj.enjoytv.widget.main.DragBubbleView;
import com.fuj.enjoytv.widget.main.TabView;

public class MainActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private TabView tv;
    private TabView chat;
    private TabView now;
    private TabView user;

    private Fragment[] fragments;

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getWindow().setBackgroundDrawable(null);

        initFragments();
        initTabView();
        initView();
        initPermission();
    }

    private void initFragments() {
        fragments = new Fragment[4];
        fragments[0] = getSupportFragmentManager().findFragmentById(R.id.tvFragment);
        fragments[1] = getSupportFragmentManager().findFragmentById(R.id.chatFragment);
        fragments[2] = getSupportFragmentManager().findFragmentById(R.id.nowFragment);
        fragments[3] = getSupportFragmentManager().findFragmentById(R.id.userFragment);
        new TVPresenter((TVFragment) fragments[0]);
        new ChatPresenter((ChatFragment) fragments[1]);
        new NowPresenter((NowFragment) fragments[2]);
        new UserPresenter((UserFragment) fragments[3]);
    }

    public void initTabView() {
        tv = (TabView) findViewById(R.id.tab_tv);
        tv.setIcon(R.mipmap.ic_tab_tv_selected, R.mipmap.ic_anchor_tv_selected);

        chat = (TabView) findViewById(R.id.tab_chat);
        chat.setIcon(R.mipmap.ic_tab_chat_normal, R.mipmap.ic_anchor_chat_normal);

        now = (TabView) findViewById(R.id.tab_now);
        now.setIcon(R.mipmap.ic_tab_now_normal, R.mipmap.ic_anchor_null);

        user = (TabView) findViewById(R.id.tab_user);
        user.setIcon(R.mipmap.ic_tab_user_normal, R.mipmap.ic_anchor_user_normal);
        selectTab(fragments[0], true);
    }

    public void initView() {
        addBubble();
    }

    public void onItemClick(View view) {
        anim_jelly(view);
        resetIcon();
        switch (view.getId()) {
            case R.id.tab_tv:
                setIcon(tv, R.mipmap.ic_tab_tv_selected, R.mipmap.ic_anchor_tv_selected);
                selectTab(fragments[0], false);
                chat.moveLeft();
                user.moveLeft();
                break;
            case R.id.tab_chat:
                setIcon(chat, R.mipmap.ic_tab_chat_selected, R.mipmap.ic_anchor_chat_selected);
                selectTab(fragments[1], true);
                user.moveLeft();
                break;
            case R.id.tab_now:
                setIcon(now, R.mipmap.ic_tab_now_selected, R.mipmap.ic_anchor_null);
                selectTab(fragments[2], true);
                chat.moveRight();
                user.moveLeft();
                break;
            case R.id.tab_user:
                setIcon(user, R.mipmap.ic_tab_user_selected, R.mipmap.ic_anchor_user_selected);
                selectTab(fragments[3], false);
                chat.moveRight();
                break;
        }
    }

    private void resetIcon() {
        tv.setIcon(R.mipmap.ic_tab_tv_normal, R.mipmap.ic_anchor_null);
        chat.setIcon(R.mipmap.ic_tab_chat_normal, R.mipmap.ic_anchor_chat_normal);
        now.setIcon(R.mipmap.ic_tab_now_normal, R.mipmap.ic_anchor_null);
        user.setIcon(R.mipmap.ic_tab_user_normal, R.mipmap.ic_anchor_user_normal);
        chat.reset();
        user.reset();
    }

    private void setIcon(final TabView view, final int bigIcon, final int smallIcon) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setIcon(bigIcon, smallIcon);
            }
        }, 100);
    }

    private void selectTab(Fragment frag, boolean isShow) {
        showFragment(frag);

        if(isShow) {
            ((BaseFragment)frag).setCustomView();
            showToolBar();
        } else {
            hideToolBar();
        }
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for(Fragment temp : fragments) {
            transaction.hide(temp);
        }
        transaction.show(fragment);
        transaction.commit();
    }

    private void initPermission() {
        PermissionUtils.init(this);
        PermissionUtils.requestPermission(PermissionUtils.CODE_WAKE_LOCK, mPermissionGrant);
        PermissionUtils.requestPermission(PermissionUtils.CODE_CAMERA, mPermissionGrant);
        PermissionUtils.requestPermission(PermissionUtils.CODE_ACCESS_FINE_LOCATION, mPermissionGrant);
        PermissionUtils.requestPermission(PermissionUtils.CODE_ACCESS_COARSE_LOCATION, mPermissionGrant);
        PermissionUtils.requestPermission(PermissionUtils.CODE_READ_PHONE_STATE, mPermissionGrant);
        PermissionUtils.requestPermission(PermissionUtils.CODE_PERMISSION_RECEIVE_BOOT_COMPLETED, mPermissionGrant);
    }

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case PermissionUtils.CODE_RECORD_AUDIO:
                    break;
                case PermissionUtils.CODE_GET_ACCOUNTS:
                    break;
                case PermissionUtils.CODE_READ_PHONE_STATE:
                    break;
                case PermissionUtils.CODE_CALL_PHONE:
                    break;
                case PermissionUtils.CODE_CAMERA:
                    break;
                case PermissionUtils.CODE_ACCESS_FINE_LOCATION:
                    break;
                case PermissionUtils.CODE_ACCESS_COARSE_LOCATION:
                    break;
                case PermissionUtils.CODE_READ_EXTERNAL_STORAGE:
                    break;
                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                    break;
                case PermissionUtils.CODE_WAKE_LOCK:
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionUtils.requestPermissionsResult(requestCode, permissions, grantResults, mPermissionGrant);
    }

    private void addBubble() {
        DragBubbleView bubbleView = (DragBubbleView) findViewById(R.id.bubble_chat);
        bubbleView.setText("99+");
        //bubbleView.setOnBubbleStateListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode){
            case Constant.RESULT_CODE_PLAY_PATH:
                fragments[2].onActivityResult(requestCode, resultCode, data);
                break;
            case Constant.RESULT_CODE_LOGIN:
                fragments[3].onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    //public native String stringFromJNI();
}
