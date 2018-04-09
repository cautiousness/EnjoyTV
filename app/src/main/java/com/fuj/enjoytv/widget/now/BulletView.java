package com.fuj.enjoytv.widget.now;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.tools.cache.ConCache;
import com.fuj.enjoytv.utils.DensityUtils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gang
 */
public class BulletView extends FrameLayout {
    private static int mWidth;
    private static int mHeight;
    private static int mMaxHeight;
    private BulletHandler handler;
    private List<String> mNames;
    private Context mContext;

    public BulletView(Context context) {
        this(context, null);
    }

    public BulletView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mWidth = DensityUtils.dp2px(context, ConCache.getFloat(context, ConCache.DISPLAY_WIDTH));
        init();
    }

    private void init() {
        handler = new BulletHandler(this);
        mNames = new ArrayList<>();
    }

    public void updateView(String name) {
        if (name == null) {
            return;
        }

        if (mNames.size() == 0) {
            mNames.add(name);
            startBulletView(name);
            return;
        }

        if (name.equals(mNames.get(mNames.size() - 1))) {
            return;
        }
        mNames.add(name);
        startBulletView(name);
    }

    public void startBulletView(final String name) {
        new Thread() {//开启线程发送弹幕
            @Override
            public void run() {
                try {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("name", name);
                    message.setData(bundle);
                    handler.sendMessage(message);
                    //Thread.sleep((long) (Math.random() * 2000) + 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private static class BulletHandler extends Handler {
        private SoftReference<BulletView> mBulletView;

        public BulletHandler(BulletView bulletView) {
            mBulletView = new SoftReference<>(bulletView);
        }

        @Override
        public void handleMessage(Message msg) {
            String name = msg.getData().getString("name");
            final RelativeLayout root = (RelativeLayout) LayoutInflater.from(mBulletView.get().mContext).inflate(R.layout.item_bullet, null);
            RelativeLayout itemRL = (RelativeLayout) root.findViewById(R.id.item_RL);
            itemRL.setY(mHeight = (mHeight + 20) % 260);
            itemRL.setX(mWidth + 100);

            TextView textView = (TextView) root.findViewById(R.id.item_contentTV); //设置文字
            textView.setText(name);
            mBulletView.get().addView(root);

            final ObjectAnimator anim = ObjectAnimator.ofFloat(itemRL, "translationX", -(mWidth + itemRL.getWidth() + 100));
            anim.setDuration(4500);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}

                @Override
                public void onAnimationEnd(Animator animation) {
                    anim.cancel();
                    root.clearAnimation();
                    mBulletView.get().removeView(root);
                    mBulletView.get().mNames.remove(0);
                }

                @Override
                public void onAnimationCancel(Animator animation) {}

                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
            anim.start();
        }
    }
}
