package com.fuj.enjoytv.tools.video;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.utils.DensityUtils;
import com.fuj.enjoytv.utils.LogUtils;

import java.lang.ref.SoftReference;
import java.util.Random;

/**
 * Created by gang
 */
public class Effect {
    private int mSize;
    private final int mWidth = 440, mHeight = 300;

    private Random random;
    private LayoutParams layoutParams;
    private Drawable[] drawables;
    private Interpolator[] interpolators;
    private Handler mHandler;

    private static int count;
    private static Effect instance;
    private static SoftReference<Context> mContext;

    private Effect(Context context) {
        mContext = new SoftReference<>(context);
        random = new Random();
        initLayout();
        initDrawable();
        initInterpolators();
        mHandler = new Handler();
    }

    public static Effect getInstance(Context context) {
        if(instance == null) {
            synchronized (Effect.class) {
                if(instance == null) {
                    instance = new Effect(context);
                }
            }
        }
        return instance;
    }

    public void startHeartAnimator(final ViewGroup parent) {
        count = 0;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    final ImageView imageView = new ImageView(mContext.get());
                    imageView.setImageDrawable(drawables[random.nextInt(5)]); //随机选一个
                    imageView.setLayoutParams(layoutParams);
                    parent.addView(imageView);

                    Animator animator = getAnimator(imageView);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            parent.removeView(imageView); //view动画结束后remove掉
                        }
                    });
                    animator.start();

                    if(count < 20) {
                        mHandler.postDelayed(this, 100);
                        count++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 100);
    }

    private Animator getAnimator(View target) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(getBezierValueAnimator(target));
        animatorSet.setInterpolator(interpolators[random.nextInt(4)]);
        animatorSet.setTarget(target);
        return animatorSet;
    }

    private ValueAnimator getBezierValueAnimator(final View target) {
        BezierEvaluator evaluator = new BezierEvaluator(
                new PointF(random.nextInt(mWidth), random.nextInt(mHeight)),
                new PointF(random.nextInt(mWidth), random.nextInt(mHeight))); //初始化一个自定义的贝塞尔曲线插值器，并且传入控制点
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, new PointF(mWidth, mHeight) //传入了曲线起点（左下角）和终点（顶部随机）
                , new PointF(random.nextInt(mWidth), -mSize));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue(); //获取到贝塞尔曲线轨迹上的x和y值 赋值给view
                target.setX(pointF.x);
                target.setY(pointF.y);
            }
        });
        animator.setTarget(target);
        animator.setDuration(4000);
        return animator;
    }

    private class BezierEvaluator implements TypeEvaluator<PointF> {
        private PointF pointF1;
        private PointF pointF2;
        public BezierEvaluator(PointF pointF1, PointF pointF2) {
            this.pointF1 = pointF1;
            this.pointF2 = pointF2;
        }

        @Override
        public PointF evaluate(float time, PointF startValue, PointF endValue) {
            float timeOn = 1.0f - time;
            PointF point = new PointF();
            point.x = timeOn * timeOn * timeOn * (startValue.x)
                    + 3 * timeOn * timeOn * time * (pointF1.x)
                    + 3 * timeOn * time * time * (pointF2.x)
                    + time * time * time * (endValue.x);
            point.y = timeOn * timeOn * timeOn * (startValue.y)
                    + 3 * timeOn * timeOn * time * (pointF1.y)
                    + 3 * timeOn * time * time * (pointF2.y)
                    + time * time * time * (endValue.y);
            return point;
        }
    }

    private void initLayout() {
        mSize = DensityUtils.dp2px(mContext.get(), 20);
        layoutParams = new LayoutParams(mSize, mSize);
    }

    private void initDrawable() {
        drawables = new Drawable[5];
        drawables[0] = ContextCompat.getDrawable(mContext.get(), R.mipmap.ic_blue);
        drawables[1] = ContextCompat.getDrawable(mContext.get(), R.mipmap.ic_origin);
        drawables[2] = ContextCompat.getDrawable(mContext.get(), R.mipmap.ic_pink);
        drawables[3] = ContextCompat.getDrawable(mContext.get(), R.mipmap.ic_red);
        drawables[4] = ContextCompat.getDrawable(mContext.get(), R.mipmap.ic_heart);
    }

    private void initInterpolators() {
        interpolators = new Interpolator[4]; // 初始化插值器
        interpolators[0] = new LinearInterpolator();
        interpolators[1] = new AccelerateInterpolator();
        interpolators[2] = new DecelerateInterpolator();
        interpolators[3] = new AccelerateDecelerateInterpolator();
    }
}
