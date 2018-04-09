package com.fuj.enjoytv.activity.main.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gang
 */
public class ChatFragment extends BaseFragment implements IChatContract.View {

    @Bind(R.id.tv_det_rv)
    RecyclerView mTVRV;

    @Override
    protected void findViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.view_tv_det, container, false);
        ButterKnife.bind(this, root);
        initView();
    }

    @Override
    public void setPresenter(IChatContract.Presenter presenter) {

    }

    @Override
    protected void initTitle(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.initTitle(inflater, container, savedInstanceState);
        title = inflater.inflate(R.layout.title_chat, container, false);
    }

    private void initView() {
        mTVRV.setLayoutManager(new LinearLayoutManager(getContext()));
        setMarginTop(mTVRV);
    }
}
