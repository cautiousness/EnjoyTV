package com.fuj.enjoytv.activity.splash;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.fuj.enjoytv.R;
import com.fuj.enjoytv.activity.main.MainActivity;
import com.fuj.enjoytv.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    @Bind(R.id.splash_lottie)
    LottieAnimationView mLottieView;

    @Bind(R.id.splash_name)
    TextView mNameTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
        initLottieView();
        hideToolBar();
    }

    private void initLottieView() {
        mLottieView.setAnimation("anim_splash.json");
        mLottieView.playAnimation();
        mLottieView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                showMain();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if(mLottieView != null) {
            mLottieView.cancelAnimation();
            mLottieView = null;
        }
    }

    public void clickSkip(View view) {
        showMain();
    }

    private void showMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_alpha_enter, R.anim.anim_alpha_exit);
        finish();
    }
}
