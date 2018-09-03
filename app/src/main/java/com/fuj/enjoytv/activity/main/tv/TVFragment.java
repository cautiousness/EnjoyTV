package com.fuj.enjoytv.activity.main.tv;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.activity.tv.TVDetActivity;
import com.fuj.enjoytv.adapter.FallsAdapter;
import com.fuj.enjoytv.adapter.MainLoopAdapter;
import com.fuj.enjoytv.adapter.NaviAdapter;
import com.fuj.enjoytv.adapter.base.RVAdapter;
import com.fuj.enjoytv.anim.transformer.ScaleInTransformer;
import com.fuj.enjoytv.base.BaseFragment;
import com.fuj.enjoytv.model.MainResult;
import com.fuj.enjoytv.model.main.Pic_title;
import com.fuj.enjoytv.utils.JsonUtils;
import com.fuj.enjoytv.widget.comm.MarqueeView;
import com.fuj.enjoytv.widget.tv.NestedScrollView;
import com.fuj.enjoytv.widget.tv.ViewPagerIndicator;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gang
 */
public class TVFragment extends BaseFragment implements ITVContract.View {
    @Bind(R.id.tv_loop)
    TextView loopTV;

    @Bind(R.id.tv_marqueeview)
    MarqueeView mMarqueeView;

    @Bind(R.id.tv_navi)
    RecyclerView mNaviRV;

    @Bind(R.id.tv_falls)
    RecyclerView mFallRV;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.appbar)
    AppBarLayout appBarLayout;

    @Bind(R.id.scrollView)
    NestedScrollView scrollView;

    @Bind(R.id.id_viewpager)
    ViewPager mViewPager;

    @Bind(R.id.tv_dotLL)
    LinearLayout linearLayout;

    private MainLoopAdapter mLoopAdapter;
    private NaviAdapter mNaviAdapter;
    private FallsAdapter mFallAdapter;
    private ViewPagerIndicator mViewPagerIndicator;
    private ITVContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAdapter();
        getData();
    }

    @Override
    protected void findViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.view_tv, container, false);
        ButterKnife.bind(this, root);
        initTitle();
        initView();
    }

    @Override
    public void setPresenter(ITVContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.start();
    }

    private void initTitle() {
        setViewMargins(toolbar, 0, getStatusBarHeight(), 0, 0);
        getBaseActivity().setSupportActionBar(toolbar);
        getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getBaseActivity().getSupportActionBar().setDisplayShowTitleEnabled(false);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitleEnabled(false);
        initViewPager();
    }

    private void initView() {
        initNavi();
        initFalls();
        initScrollView();
        mMarqueeView.startPlay();
    }

    private void initNavi() {
        mNaviRV.setLayoutManager(new GridLayoutManager(getContext(), 5));
        mNaviRV.setAdapter(mNaviAdapter);
    }

    public void initFalls() {
        mFallRV.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mFallRV.setAdapter(mFallAdapter);
    }

    private void initScrollView() {
        scrollView.setFlingOverflowListener(new NestedScrollView.OnFlingOverflowListener() {
            @Override
            public void onFling(float velocity) {
                appBarLayout.setExpanded(true,true);
            }
        });
    }

    private void initViewPager() {
        //mViewPager.setPageMargin(50);
        mViewPagerIndicator = new ViewPagerIndicator(mViewPager,
            getContext(), linearLayout, loopTV, mLoopAdapter.getList());
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mLoopAdapter);
        mViewPager.setPageTransformer(true, new ScaleInTransformer());
        mViewPager.addOnPageChangeListener(mViewPagerIndicator);
    }

    private void initAdapter() {
        mLoopAdapter = new MainLoopAdapter(getContext());
        mNaviAdapter = new NaviAdapter(getContext(), new ArrayList<Pic_title>(), R.layout.item_navi);
        mNaviAdapter.setOnItemClickListener(new RVAdapter.OnItemClickListener<Pic_title>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Pic_title item, int position) {
                switch (position) {
                    case 0:
                        showActivity(TVDetActivity.class);
                        break;
                    default:
                        showToast("点击了" + item.title);
                        break;
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Pic_title item, int position) {
                return false;
            }
        });

        mFallAdapter = new FallsAdapter(getContext(), new ArrayList<Pic_title>(), R.layout.item_fall);
        mFallAdapter.setOnItemClickListener(new RVAdapter.OnItemClickListener<Pic_title>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Pic_title item, int position) {
                showToast("点击了" + item.title);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Pic_title item, int position) {
                return false;
            }
        });
    }

    private void getData() {
        String content = JsonUtils.readJsonFile(getContext(), "main");
        MainResult result = new Gson().fromJson(content, MainResult.class);
        if(result != null) {
            if(result.datas.loop != null && result.datas.loop.size() > 0) {
                mLoopAdapter.updateData(result.datas.loop);
            }

            if(result.datas.navi != null && result.datas.navi.size() > 0) {
                mNaviAdapter.updateRecyclerView(result.datas.navi);
            }

            if(result.datas.gif != null && result.datas.gif.size() > 0) {
                mFallAdapter.updateRecyclerView(result.datas.gif);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewPagerIndicator.onDestroy();
    }
}
