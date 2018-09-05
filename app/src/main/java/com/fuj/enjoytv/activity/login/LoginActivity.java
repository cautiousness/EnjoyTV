package com.fuj.enjoytv.activity.login;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.base.BaseActivity;
import com.fuj.enjoytv.utils.Constant;
import com.fuj.enjoytv.widget.comm.JellyInterpolator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.main_btn_login)
    TextView mBtnLogin;

    @Bind(R.id.layout_progress)
    View progress;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    public void clickLogin(View view) {
        if (accountET.getEditableText().length() == 0 || pswET.getEditableText().length() == 0) {
            showToast(R.string.toast_input_account);
            return;
        }
        mName.setVisibility(View.INVISIBLE);
        mPsw.setVisibility(View.INVISIBLE);
        inputAnimator(mInputLayout, mBtnLogin.getMeasuredWidth(), mBtnLogin.getMeasuredHeight());
    }

    private void inputAnimator(final View view, float w, float h) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout, "scaleX", 1f, 0.5f);
        set.setDuration(500);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
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

    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view, animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
    }

    private void recovery() {
        progress.setVisibility(View.GONE);
        mInputLayout.setVisibility(View.VISIBLE);
        mName.setVisibility(View.VISIBLE);
        mPsw.setVisibility(View.VISIBLE);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mInputLayout.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        mInputLayout.setLayoutParams(params);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout, "scaleX", 0.5f,1f );
        animator2.setDuration(500);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();
    }
}
