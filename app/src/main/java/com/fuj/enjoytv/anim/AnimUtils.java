package com.fuj.enjoytv.anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import java.lang.ref.SoftReference;

/**
 * Created by gang
 */
public class AnimUtils {
    public static final int ANIM_SHAKE = 1;
    public static final int ANIM_JELLY = 2;

    public static void startAnim(View view, int type, int repeat) {
        SoftReference<View> mView = new SoftReference<>(view);
        switch (type) {
            case ANIM_SHAKE:
                mView.get().startAnimation(shakeAnim(repeat));
                break;
            case ANIM_JELLY:
                anim_jelly(mView.get());
                break;
            default:
                break;
        }
    }

    private static Animation shakeAnim(int CycleTimes) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 10);
        translateAnimation.setInterpolator(new CycleInterpolator(CycleTimes));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

    private static void anim_jelly(View view) {
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.60f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.60f, 1f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(200);
        set.playTogether(animator2, animator3);
        set.start();
    }
}
