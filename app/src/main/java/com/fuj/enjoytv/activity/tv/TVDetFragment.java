package com.fuj.enjoytv.activity.tv;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.activity.tv_play.TVPlayActivity;
import com.fuj.enjoytv.adapter.TVDetAdapter;
import com.fuj.enjoytv.adapter.base.RVAdapter;
import com.fuj.enjoytv.base.BaseFragment;
import com.fuj.enjoytv.model.CommResult;
import com.fuj.enjoytv.model.tv.TVDet;
import com.fuj.enjoytv.utils.AppManager;
import com.fuj.enjoytv.utils.JsonUtils;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gang
 */

public class TVDetFragment extends BaseFragment implements ITVDetContact.View {
    @Bind(R.id.tv_det_rv)
    RecyclerView mTVRV;

    private TVDetAdapter mAdapter;
    private ITVDetContact.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new TVDetAdapter(getContext(), new ArrayList<TVDet>(), R.layout.item_det_tv);
        mAdapter.setOnItemClickListener(new RVAdapter.OnItemClickListener<TVDet>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, TVDet tvDet, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppManager.BUNDLE_TVDET, tvDet);
                showActivity(TVPlayActivity.class, bundle);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, TVDet tvDet, int position) {
                return false;
            }
        });
    }

    @Override
    protected void findViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.view_tv_det, container, false);
        ButterKnife.bind(this, root);
        initView();
        setTitle("卫视");
    }

    @Override
    public void setPresenter(ITVDetContact.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.start();
    }

    private void initView() {
        mTVRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mTVRV.setAdapter(mAdapter);
        setMarginTop(mTVRV);

        getData();
    }

    private void getData() {
        String content = JsonUtils.readJsonFile(getContext(), "tvdet");
        CommResult result = new Gson().fromJson(content, CommResult.class);
        mAdapter.updateRecyclerView(result.datas);
    }
}
