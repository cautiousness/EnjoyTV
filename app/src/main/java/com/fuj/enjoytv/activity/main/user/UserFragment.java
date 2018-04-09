package com.fuj.enjoytv.activity.main.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.activity.qrcode.QRCodeActivity;
import com.fuj.enjoytv.activity.simulation_loc.SimulationLocActivity;
import com.fuj.enjoytv.activity.video.VideoActivity;
import com.fuj.enjoytv.base.BaseFragment;
import com.fuj.enjoytv.utils.LogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gang
 */
public class UserFragment extends BaseFragment implements IUserContract.View, View.OnClickListener {
    @Bind(R.id.user_nameTV)
    TextView userTV;

    @Bind(R.id.user_loc)
    TextView locTV;

    @Bind(R.id.user_video)
    TextView videoTV;

    @Bind(R.id.user_qrcode)
    TextView qrcodeTV;

    private IUserContract.Presenter mPresenter;

    protected void findViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.view_user, container, false);
        ButterKnife.bind(this, root);
        initView();
    }

    @Override
    public void setPresenter(IUserContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.start();
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        locTV.setOnClickListener(this);
        videoTV.setOnClickListener(this);
        qrcodeTV.setOnClickListener(this);
    }

    private void initView() {
        initSimulationLoc();
    }

    private void initSimulationLoc() {
        userTV.setText(isLogin() ? getUserName() : "请登录");
        userTV.setBackgroundResource(isLogin() ? R.drawable.bg_subscribe_selected : R.drawable.bg_subscribe_normal);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_loc:
                clickSimulationLoc();
                break;
            case R.id.user_video:
                clickVideo();
                break;
            case R.id.user_qrcode:
                clickQRCode();
                break;
            default:
                break;
        }
    }

    private void clickSimulationLoc() {
        showActivity(SimulationLocActivity.class);
    }

    private void clickVideo() {
        showActivity(VideoActivity.class);
    }

    private void clickQRCode() {
        showActivity(QRCodeActivity.class);
    }
}
