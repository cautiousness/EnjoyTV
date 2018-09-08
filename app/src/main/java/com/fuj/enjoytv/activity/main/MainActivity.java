package com.fuj.enjoytv.activity.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

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
import com.fuj.enjoytv.widget.main.DragBubbleView;
import com.fuj.enjoytv.widget.main.TabView;
import com.yanzhenjie.permission.AndPermission;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @Bind(R.id.tab_tv)
    TabView tv;

    @Bind(R.id.tab_chat)
    TabView chat;

    @Bind(R.id.tab_now)
    TabView now;

    @Bind(R.id.tab_user)
    TabView user;

    @Bind(R.id.text_tv)
    TextView text_tv;

    @Bind(R.id.text_chat)
    TextView text_chat;

    @Bind(R.id.text_now)
    TextView text_now;

    @Bind(R.id.text_user)
    TextView text_user;

    private Fragment[] fragments;

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
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
        resetIcon();
        tv.setIcon(R.mipmap.ic_tab_tv_selected, R.mipmap.ic_anchor_tv_selected);
        text_tv.setTextColor(getResources().getColor(R.color.colorPrimary));
        selectTab(fragments[0], true);
    }

    public void initView() {
        addBubble();
    }

    public void onClick(View view) {
        anim_jelly(view);
        resetIcon();
        switch (view.getId()) {
            case R.id.tab_tv:
            case R.id.text_tv:
                selectTab(fragments[0], false);
                setIcon(tv, R.mipmap.ic_tab_tv_selected, R.mipmap.ic_anchor_tv_selected);
                text_tv.setTextColor(getResources().getColor(R.color.colorPrimary));
                chat.moveLeft();
                user.moveLeft();
                break;
            case R.id.tab_chat:
            case R.id.text_chat:
                selectTab(fragments[1], true);
                setIcon(chat, R.mipmap.ic_tab_chat_selected, R.mipmap.ic_anchor_chat_selected);
                text_chat.setTextColor(getResources().getColor(R.color.colorPrimary));
                user.moveLeft();
                break;
            case R.id.tab_now:
            case R.id.text_now:
                selectTab(fragments[2], true);
                setIcon(now, R.mipmap.ic_tab_now_selected, R.mipmap.ic_anchor_null);
                text_now.setTextColor(getResources().getColor(R.color.colorPrimary));
                chat.moveRight();
                user.moveLeft();
                break;
            case R.id.tab_user:
            case R.id.text_user:
                selectTab(fragments[3], false);
                setIcon(user, R.mipmap.ic_tab_user_selected, R.mipmap.ic_anchor_user_selected);
                text_user.setTextColor(getResources().getColor(R.color.colorPrimary));
                chat.moveRight();
                break;
        }
    }

    private void resetIcon() {
        tv.setIcon(R.mipmap.ic_tab_tv_normal, R.mipmap.ic_anchor_null);
        chat.setIcon(R.mipmap.ic_tab_chat_normal, R.mipmap.ic_anchor_chat_normal);
        now.setIcon(R.mipmap.ic_tab_now_normal, R.mipmap.ic_anchor_null);
        user.setIcon(R.mipmap.ic_tab_user_normal, R.mipmap.ic_anchor_user_normal);
        text_tv.setTextColor(getResources().getColor(R.color.gray));
        text_chat.setTextColor(getResources().getColor(R.color.gray));
        text_now.setTextColor(getResources().getColor(R.color.gray));
        text_user.setTextColor(getResources().getColor(R.color.gray));
        chat.reset();
        user.reset();
    }

    private void setIcon(final TabView view, final int bigIcon, final int smallIcon) {
        view.setIcon(bigIcon, smallIcon);
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
        AndPermission.with(this)
        .requestCode(100)
        .permission(Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WAKE_LOCK, Manifest.permission.RECORD_AUDIO)
        .send();
    }

    private void addBubble() {
        DragBubbleView bubbleView = findViewById(R.id.bubble_chat);
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
            case -1:
                fragments[3].onActivityResult(requestCode, resultCode, data);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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
