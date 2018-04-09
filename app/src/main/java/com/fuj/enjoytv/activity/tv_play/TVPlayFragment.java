package com.fuj.enjoytv.activity.tv_play;

import android.content.Context;
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

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.base.BaseFragment;
import com.fuj.enjoytv.model.tv.TVDet;
import com.fuj.enjoytv.utils.Constant;
import com.fuj.enjoytv.video.MediaUtils;
import com.fuj.enjoytv.video.PlayStateParams;
import com.fuj.enjoytv.video.PlayerView;
import com.fuj.enjoytv.video.VideoijkBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by gang
 */
public class TVPlayFragment extends BaseFragment implements ITVPlayContact.View {
    private static final int SIZE = 3;
    private TVDet mTVDet;

    private Fragment[] mFragmentArrays = new Fragment[SIZE];
    private String[] mTabTitles = new String[] {"节目单", "王牌节目", "边聊边看"};
    private PlayerView player;
    private PowerManager.WakeLock wakeLock;

    private ITVPlayContact.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTVDet = (TVDet) getArguments().getSerializable(Constant.BUNDLE_TVDET);
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
    public void setPresenter(ITVPlayContact.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.start();
    }

    private void initVideoView() {
        PowerManager pm = (PowerManager) getContext().getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "liveTAG");
        wakeLock.acquire();
        List<VideoijkBean> list = new ArrayList<>();
        list.add(new VideoijkBean("标清", mTVDet.url));
        list.add(new VideoijkBean("高清", mTVDet.url));
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
        .setTitle(mTVDet.title)
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
        mFragmentArrays[0] = TVPlayListFragment.newInstance();
        mFragmentArrays[1] = TVPlayTabFragment.newInstance();
        mFragmentArrays[2] = TVPlayTabFragment.newInstance();
        PagerAdapter pagerAdapter = new MyViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onStop() {
        super.onStop();
        /*mVideoView.stopPlayback();
        mVideoView.release(true);
        IjkMediaPlayer.native_profileEnd();*/
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        MediaUtils.muteAudioFocus(getContext(), true); //demo的内容，恢复系统其它媒体的状态
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
        MediaUtils.muteAudioFocus(getContext(), false); //demo的内容，暂停系统其它媒体的状态
        if (wakeLock != null) {
            wakeLock.acquire();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
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
}
