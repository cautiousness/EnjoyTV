package com.fuj.enjoytv.activity.main.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fuj.enjoytv.R;
import com.fuj.enjoytv.activity.login.LoginActivity;
import com.fuj.enjoytv.activity.qrcode.QRCodeActivity;
import com.fuj.enjoytv.activity.simulation_loc.SimulationLocActivity;
import com.fuj.enjoytv.activity.video.VideoActivity;
import com.fuj.enjoytv.base.BaseFragment;
import com.fuj.enjoytv.utils.Constant;
import com.fuj.enjoytv.utils.LogUtils;
import com.fuj.enjoytv.widget.comm.GlideCircleTransform;

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

    @Bind(R.id.user_avatarIV)
    ImageView avatarIV;

    @Bind(R.id.user_locIV)
    LottieAnimationView locIV;

    @Bind(R.id.user_videoIV)
    LottieAnimationView videoIV;

    @Bind(R.id.user_qrcodeIV)
    LottieAnimationView qrcodeIV;

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
        userTV.setOnClickListener(this);
    }

    private void initView() {
        initUserInfo();
        locIV.setAnimation("location_map_pin.json");
        locIV.loop(true);
        videoIV.setAnimation("camera.json");
        videoIV.loop(true);
        qrcodeIV.setAnimation("qr_code.json");
        qrcodeIV.loop(true);
    }

    private void initUserInfo() {
        userTV.setText(isLogin() ? getUserName() : "未登录");
        userTV.setBackgroundResource(isLogin() ? R.drawable.bg_user_login_selected : R.drawable.bg_user_login);
        Glide.with(getContext())
        .load(getResources().getIdentifier(isLogin() ? "ic_avatar8" : "ic_user_avatar", "mipmap", getContext().getPackageName()))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .transform(new GlideCircleTransform(getContext()))
        .into(avatarIV);
    }

    @Override
    public void onResume() {
        super.onResume();
        locIV.playAnimation();
        videoIV.playAnimation();
        qrcodeIV.playAnimation();
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
            case R.id.user_nameTV:
                clickLogin();
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

    private void clickLogin() {
        if(!isLogin()) {
            showActivityResult(LoginActivity.class, Constant.RESULT_CODE_LOGIN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Constant.RESULT_CODE_LOGIN:
                setUser(data.getStringExtra(Constant.BUNDLE_USER_NAME));
                initUserInfo();
                break;
            default:
                break;
        }
    }
}
