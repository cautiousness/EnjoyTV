package com.fuj.enjoytv.activity.login;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fuj.enjoytv.R;
import com.fuj.enjoytv.base.BaseActivity;
import com.fuj.enjoytv.utils.Constant;
import com.fuj.enjoytv.widget.comm.GlideCircleTransform;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.main_btn_login)
    TextView mBtnLogin;

    @Bind(R.id.layout_lottie)
    LottieAnimationView lottieView;

    @Bind(R.id.input_layout)
    View mInputLayout;

    @Bind(R.id.input_layout_name)
    LinearLayout mName;

    @Bind(R.id.input_layout_psw)
    LinearLayout mPsw;

    @Bind(R.id.login_accountET)
    EditText accountET;

    @Bind(R.id.login_pswET)
    EditText pswET;

    @Bind(R.id.login_avatorIV)
    ImageView avatarIV;

    @Bind(R.id.login_nameTV)
    TextView nameTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        RequestOptions mOptions = new RequestOptions()
        .centerCrop()
        .transform(new GlideCircleTransform(this))
        .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(this)
        .load(getResources().getIdentifier("ic_user_avatar","mipmap", getPackageName()))
        .apply(mOptions)
        .into(avatarIV);
    }

    public void clickLogin(View view) {
        if (accountET.getEditableText().length() == 0 || pswET.getEditableText().length() == 0) {
            showToast(R.string.toast_input_account);
            return;
        }
        nameTV.setVisibility(View.GONE);
        mBtnLogin.setVisibility(View.GONE);
        inputAnimator();
    }

    private void inputAnimator() {
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout, "scaleY", 1f, 0.7f);
        animator2.setDuration(300);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();
        animator2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                lottieView.setVisibility(View.VISIBLE);
                lottieView.setAnimation("anim_loading.json");
                lottieView.playAnimation();
                mInputLayout.setVisibility(View.INVISIBLE);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if ("123".equals(accountET.getEditableText().toString())
                            && "123".equals(pswET.getEditableText().toString())) {
                            showLoginAnim(R.string.login_success, "success.json");
                            Intent intent = new Intent();
                            intent.putExtra(Constant.BUNDLE_USER_NAME, "畅享TV");
                            setResult(Constant.RESULT_CODE_LOGIN, intent);
                            finish();
                            return;
                        }
                        showLoginAnim(R.string.login_failed, "failed.json");
                        recovery();
                    }
                }, 2000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {}
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_trans_y_enter, R.anim.anim_trans_y_exit);
    }

    private void recovery() {
        lottieView.setVisibility(View.GONE);
        mInputLayout.setVisibility(View.VISIBLE);
        mName.setVisibility(View.VISIBLE);
        mPsw.setVisibility(View.VISIBLE);
        nameTV.setVisibility(View.VISIBLE);
        mBtnLogin.setVisibility(View.VISIBLE);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mInputLayout.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        mInputLayout.setLayoutParams(params);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout, "scaleY", 0.7f,1f);
        animator2.setDuration(300);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();
    }
}
