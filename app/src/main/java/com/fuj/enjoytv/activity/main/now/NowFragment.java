package com.fuj.enjoytv.activity.main.now;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.activity.now.NowPlayActivity;
import com.fuj.enjoytv.adapter.NowAdapter;
import com.fuj.enjoytv.adapter.base.RVAdapter;
import com.fuj.enjoytv.base.BaseFragment;
import com.fuj.enjoytv.model.NowResult;
import com.fuj.enjoytv.model.now.Now;
import com.fuj.enjoytv.utils.AppManager;
import com.fuj.enjoytv.utils.JsonUtils;
import com.fuj.enjoytv.video.IjkVideoView;
import com.fuj.enjoytv.video.PlayStateParams;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by gang
 */
public class NowFragment extends BaseFragment implements INowContract.View {
    @Bind(R.id.now_falls)
    RecyclerView mFallsRV;

    @Bind(R.id.now_floatView)
    RelativeLayout mFloatView;

    @Bind(R.id.now_videoView)
    IjkVideoView mIjkVideo;

    @Bind(R.id.now_closeBTN)
    Button mCloseBTN;

    private float x, y;

    private NowAdapter mNowAdapter;
    private INowContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNowAdapter = new NowAdapter(getContext(), new ArrayList<Now>(), R.layout.item_now);
        mNowAdapter.setOnItemClickListener(new RVAdapter.OnItemClickListener<Now>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Now now, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppManager.BUNDLE_NOW, now);
                showActivityResult(NowPlayActivity.class, 1, bundle);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Now now, int position) {
                return false;
            }
        });

        getData();
    }

    @Override
    protected void initTitle(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.initTitle(inflater, container, savedInstanceState);
        title = inflater.inflate(R.layout.title_now, container, false);
    }

    @Override
    protected void findViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.view_now, container, false);
        ButterKnife.bind(this, root);
        initFalls();
        initFloatView();
    }

    @Override
    public void setPresenter(INowContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.start();
    }

    private void initFalls() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mFallsRV.setLayoutManager(gridLayoutManager);
        mFallsRV.setAdapter(mNowAdapter);
        setMarginTop(mFallsRV);
    }

    private void getData() {
        String content = JsonUtils.readJsonFile(getContext(), "now");
        NowResult result = new Gson().fromJson(content, NowResult.class);
        mNowAdapter.updateRecyclerView(result.datas);
    }

    private void initFloatView() {
        mFloatView.setOnTouchListener(new View.OnTouchListener() {
            float mTouchStartX, mTouchStartY;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                x = event.getRawX();
                y = event.getRawY() - 25;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mTouchStartX = event.getX();
                        mTouchStartY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mFloatView.setX(x - mTouchStartX);
                        mFloatView.setY(y - mTouchStartY);
                        break;
                }
                return true;
            }
        });

        mIjkVideo.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                return true;
            }
        });

        mCloseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFloatWindow();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case AppManager.RESULT_CODE_PLAY_PATH:
                resultPlay(data);
                break;
            default:
                break;
        }

    }

    private void resultPlay(Intent data) {
        mIjkVideo.setVideoPath(data.getStringExtra(AppManager.BUNDLE_PLAY_PATH));
        mIjkVideo.seekTo(0);
        mIjkVideo.setAspectRatio(PlayStateParams.fitparent);
        mIjkVideo.start();
        setFloatVisible(true);
    }

    private void closeFloatWindow() {
        mIjkVideo.pause();
        setFloatVisible(false);
    }

    private void setFloatVisible(boolean isVisible) {
        mFloatView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        closeFloatWindow();
    }
}
