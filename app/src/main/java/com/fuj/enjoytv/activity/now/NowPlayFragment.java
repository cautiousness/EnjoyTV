package com.fuj.enjoytv.activity.now;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.base.BaseFragment;
import com.fuj.enjoytv.model.now.ChatEvent;
import com.fuj.enjoytv.model.now.Now;
import com.fuj.enjoytv.tools.video.Effect;
import com.fuj.enjoytv.utils.AppManager;
import com.fuj.enjoytv.video.MediaUtils;
import com.fuj.enjoytv.video.PlayStateParams;
import com.fuj.enjoytv.video.PlayerView;
import com.fuj.enjoytv.video.VideoijkBean;
import com.fuj.enjoytv.widget.now.BulletView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gang
 */
public class NowPlayFragment extends BaseFragment implements INowPlayContact.View {
    @Bind(R.id.video_bulletView)
    BulletView mBulletView;

    @Bind(R.id.app_video_box)
    RelativeLayout mRootView;

    private Now mNow;
    private String[] mTabTitles = new String[] {"聊天", "主播详情"};
    private Fragment[] mFragmentArrays = new Fragment[mTabTitles.length];
    private PlayerView player;
    private PowerManager.WakeLock wakeLock;

    private INowPlayContact.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mNow = (Now) getArguments().getSerializable(AppManager.BUNDLE_NOW);
        Effect.getInstance().init(getContext());
    }

    @Override
    protected void initTitle(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.initTitle(inflater, container, savedInstanceState);
        hideToolBar();
    }

    @Override
    protected void findViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.view_tv_play, container, false);
        ButterKnife.bind(this, root);
        initVideoView();
        initViewPager();
    }

    @Override
    public void setPresenter(INowPlayContact.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.start();
    }

    private void initVideoView() {
        PowerManager pm = (PowerManager) getContext().getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "liveTAG");
        wakeLock.acquire();
        List<VideoijkBean> list = new ArrayList<>();
        list.add(new VideoijkBean("标清", mNow.url));
        list.add(new VideoijkBean("高清", mNow.url));
        player = new PlayerView(getActivity(), root) {
            @Override
            public PlayerView toggleProcessDurationOrientation() {
                hideSteam(getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                return setProcessDurationOrientation(getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ? PlayStateParams.PROCESS_PORTRAIT : PlayStateParams.PROCESS_LANDSCAPE);
            }

            @Override
            public PlayerView setPlaySource(List<VideoijkBean> list) {
                return super.setPlaySource(list);
            }
        }
        .setTitle(mNow.title)
        .setProcessDurationOrientation(PlayStateParams.PROCESS_PORTRAIT)
        .setScaleType(PlayStateParams.fitxy)
        .forbidTouch(false)
        .hideSteam(true)
        .hideCenterPlayer(false)
        .setPlaySource(list)
        .setChargeTie(false, 60)
        .startPlay();
        setViewMargins(player.getTopBarView(), 0, getStatusBarHeight(), 0, 0);
    }

    private void initViewPager() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        mFragmentArrays[0] = NowPlayChatFragment.newInstance();
        mFragmentArrays[1] = NowPlayChatFragment.newInstance();
        PagerAdapter pagerAdapter = new MyViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        MediaUtils.muteAudioFocus(getContext().getApplicationContext(), true); //恢复系统其它媒体的状态
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
        MediaUtils.muteAudioFocus(getContext().getApplicationContext(), false); //暂停系统其它媒体的状态
        if (wakeLock != null) {
            wakeLock.acquire(10 * 60 * 1000L);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }

        if (wakeLock != null) {
            wakeLock.release();
        }

        Intent intent = new Intent();
        intent.putExtra(AppManager.BUNDLE_PLAY_PATH, mNow.url);
        getActivity().setResult(AppManager.RESULT_CODE_PLAY_PATH, intent);
        getActivity().finish();
    }

    private class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentArrays[position];
        }

        @Override
        public int getCount() {
            return mFragmentArrays.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onChatEvent(ChatEvent chatEvent) {
        if ("666".equals(chatEvent.msg)) {
            Effect.getInstance().startHeartAnimator(mRootView);
        } else {
            mBulletView.updateView(chatEvent.msg);
        }
    }
}
