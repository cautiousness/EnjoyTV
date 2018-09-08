package com.fuj.enjoytv.activity.tv_play;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fuj.enjoytv.model.tv.Playlist;
import com.fuj.enjoytv.utils.AppManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gang
 */
public class TVPlaySubscribeFragmentAdapter extends FragmentPagerAdapter {
    private List<Playlist> mLists;
    private List<TVPlaySubscribeFragment> fragments = new ArrayList<>();

    public TVPlaySubscribeFragmentAdapter(FragmentManager fm, List<Playlist> list) {
        super(fm);
        this.mLists = list;
        for (Playlist playlist : mLists) {
            TVPlaySubscribeFragment fragment = new TVPlaySubscribeFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(AppManager.BUNDLE_PLAYLIST, playlist);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mLists.get(position).day;
    }
}
