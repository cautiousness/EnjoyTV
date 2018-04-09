package com.fuj.enjoytv.anim;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;

/**
 * 动画效果控制类
 * @author dell
 *
 */
public class LayoutAnim {
	public static LayoutAnimationController getSlideInAnim() {
        AnimationSet animationSet = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        animationSet.addAnimation(animation);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(600);
        animationSet.addAnimation(animation);
        return new LayoutAnimationController(animationSet);
    }
	
	public static LayoutAnimationController getSlidetTopInAnim() {
		AnimationSet animationSet = new AnimationSet(true);
		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(150);
		animationSet.addAnimation(animation);
		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(400);
		animationSet.addAnimation(animation);
		return new LayoutAnimationController(animationSet);
	}

	public static LayoutAnimationController getSlideOutAnim() {
        AnimationSet animationSet = new AnimationSet(true);
        Animation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(500);
        animationSet.addAnimation(animation);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(600);
        animationSet.addAnimation(animation);
        animationSet.setFillAfter(true);
        return new LayoutAnimationController(animationSet);
    }
}
