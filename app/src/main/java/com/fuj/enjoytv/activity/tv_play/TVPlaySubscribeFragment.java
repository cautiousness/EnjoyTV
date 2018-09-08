package com.fuj.enjoytv.activity.tv_play;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.model.tv.Playlist;
import com.fuj.enjoytv.utils.AppManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gang
 */
public class TVPlaySubscribeFragment extends Fragment {
    private Playlist mPlaylist;

    @Bind(R.id.subscribe_rv)
    RecyclerView mSubscribeRV;

    private TVPlayPlaylistAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlaylist = (Playlist) getArguments().getSerializable(AppManager.BUNDLE_PLAYLIST);
        mAdapter = new TVPlayPlaylistAdapter(getContext(), new ArrayList<TVProgram>(), R.layout.item_subscribe_playlist);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout, container, false);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        mSubscribeRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mSubscribeRV.setAdapter(mAdapter);
        mAdapter.updateRecyclerView(mPlaylist.playlist);
    }
}