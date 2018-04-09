package com.fuj.enjoytv.activity.tv_play;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fuj.enjoytv.R;

/**
 * Created by gang
 */
public class TVPlayTabFragment extends Fragment {

    public static Fragment newInstance() {
        return new TVPlayTabFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }
}