package com.fuj.enjoytv.anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import java.lang.ref.SoftReference;

/**
 * Created by gang
 */
public class AnimUtils {

    public static void anim_jelly(View view) {
        SoftReference<View> mView = new SoftReference<>(view);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mView.get(), "scaleX", 1f, 0.60f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(mView.get(), "scaleY", 1f, 0.60f, 1f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(200);
        set.playTogether(animator2, animator3);
        set.start();
    }
}
