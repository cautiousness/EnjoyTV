package com.fuj.enjoytv.activity.main.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.bumptech.glide.request.RequestOptions;
import com.fuj.enjoytv.R;
import com.fuj.enjoytv.activity.login.LoginActivity;
import com.fuj.enjoytv.activity.qrcode.QRCodeActivity;
import com.fuj.enjoytv.activity.simulation_loc.SimulationLocActivity;
import com.fuj.enjoytv.activity.video.VideoActivity;
import com.fuj.enjoytv.base.BaseFragment;
import com.fuj.enjoytv.utils.AppManager;
import com.fuj.enjoytv.widget.comm.GlideCircleTransform;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

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

    private int themeId;
    private RequestOptions mOptions;
    private Bitmap mBitmap;
    private IUserContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeId = R.style.select_picture;
    }

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
        avatarIV.setOnClickListener(this);
    }

    private void initView() {
        initUserInfo();
        locIV.setAnimation("anim_loc.json");
        locIV.loop(true);
        videoIV.setAnimation("camera.json");
        videoIV.loop(true);
        qrcodeIV.setAnimation("qr_code.json");
        qrcodeIV.loop(true);
    }

    private void initUserInfo() {
        userTV.setText(isLogin() ? getUserName() : "未登录");
        userTV.setBackgroundResource(isLogin() ? R.drawable.bg_user_login_selected : R.drawable.bg_user_login);

        mOptions = new RequestOptions()
        .centerCrop()
        .transform(new GlideCircleTransform(getContext()))
        .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(getContext())
        .load(getResources().getIdentifier(isLogin() ? "ic_avatar8" : "ic_user_avatar", "mipmap", getContext().getPackageName()))
        .apply(mOptions)
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
            case R.id.user_avatarIV:
                chooseAvatar();
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
            showActivityResult(LoginActivity.class, AppManager.RESULT_CODE_LOGIN);
            getBaseActivity().overridePendingTransition(R.anim.anim_trans_y_enter, R.anim.anim_trans_y_exit);
        }
    }

    private void chooseAvatar() {
        if(!isLogin()) {
            return;
        }
        selectFromAlbum();
    }

    private void selectFromAlbum() {
        PictureSelector.create(this)
        .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
        .theme(themeId)
        .maxSelectNum(1)// 最大图片选择数量
        .minSelectNum(1)// 最小选择数量
        .imageSpanCount(4)// 每行显示个数
        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
        .previewImage(true)// 是否可预览图片
        .isCamera(true)// 是否显示拍照按钮
        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
        .enableCrop(false)// 是否裁剪
        .compress(false)// 是否压缩
        .synOrAsy(true)//同步true或异步false 压缩 默认同步
        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
        .minimumCompressSize(100)// 小于100kb的图片不压缩
        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case AppManager.RESULT_CODE_LOGIN:
                setUser(data.getStringExtra(AppManager.BUNDLE_USER_NAME));
                initUserInfo();
                break;
            case -1:
                LocalMedia media = PictureSelector.obtainMultipleResult(data).get(0);
                mBitmap = BitmapFactory.decodeFile(AppManager.getImagePath(media.getPath(),
                    avatarIV.getWidth(), avatarIV.getHeight()));
                Glide.with(getContext())
                .load(mBitmap)
                .apply(mOptions)
                .into(avatarIV);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }
}
