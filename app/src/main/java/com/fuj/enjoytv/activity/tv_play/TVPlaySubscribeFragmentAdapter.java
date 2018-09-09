package com.fuj.enjoytv.activity.tv_play;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fuj.enjoytv.model.tv.Playlist;

import java.util.List;

/**
 * Created by gang
 */
public class TVPlaySubscribeFragmentAdapter extends FragmentPagerAdapter {
    private List<Playlist> mLists;
    private List<TVPlaySubscribeFragment> mFragments;

    public TVPlaySubscribeFragmentAdapter(FragmentManager fm, List<TVPlaySubscribeFragment> fragments, List<Playlist> list) {
        super(fm);
        this.mFragments = fragments;
        this.mLists = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mLists.get(position).day;
    }

    public void updateData(List<Playlist> lists) {
        mLists = lists;
        notifyDataSetChanged();
    }
}
