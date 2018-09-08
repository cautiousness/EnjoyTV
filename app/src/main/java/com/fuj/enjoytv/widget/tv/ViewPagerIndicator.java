package com.fuj.enjoytv.widget.tv;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.model.main.Pic_title;
import com.fuj.enjoytv.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gang
 */
public class ViewPagerIndicator implements ViewPager.OnPageChangeListener {
    private int mSize;
    private int mIndex = 1;
    private boolean mIsStop = false;

    private final static int DELAY = 5000;
    private final static int RADIUS = 8;

    private List<ImageView> mDotViewLists = new ArrayList<>();
    private List<Pic_title> mList;
    private TextView mLoopTV;
    private ViewPager mViewPager;
    private ExecutorService executor = Executors.newFixedThreadPool(1);

    public ViewPagerIndicator(ViewPager viewPager, Context context, LinearLayout dotLayout, TextView textView, List<Pic_title> list) {
        this.mList = list;
        this.mViewPager = viewPager;
        this.mLoopTV = textView;
        this.mSize = mList.size() - 2;

        for (int i = 0; i < mSize; i++) {
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
            params.leftMargin = DensityUtils.px2dp(context, RADIUS);
            params.rightMargin = DensityUtils.px2dp(context, RADIUS);
            params.height = RADIUS;
            params.width = RADIUS;
            if (i == 0) {
                setSelectedDotBackground(imageView);
            } else {
                setNormalDotBackground(imageView);
            }
            dotLayout.addView(imageView, params); //为LinearLayout添加ImageView
            mDotViewLists.add(imageView);
        }
        viewPager.setCurrentItem(mIndex);
        mLoopTV.setText(mList.get(mIndex).title);
        aotuScroll();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        if (mList.size() > 1) {
            if (position < 1) {
                position = mList.size() - 1;
                mIndex = position;
                mViewPager.setCurrentItem(position,false);
            } else if(position > mSize) {
                position = 1;
                mIndex = position;
                mViewPager.setCurrentItem(position, false);
            } else {
                mIndex = position;
            }
            mLoopTV.setText(mList.get(position).title);
        }

        for (int i = 0; i < mSize; i++) {
            if ((position - 1 % mSize) == i) {
                setSelectedDotBackground(mDotViewLists.get(i));
            } else {
                setNormalDotBackground(mDotViewLists.get(i));
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    private void aotuScroll() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (!mIsStop) {
                    mViewPager.post(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setCurrentItem((++mIndex) % mList.size());
                        }
                    });

                    try {
                        synchronized (this) {
                            wait(DELAY);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void onDestroy() {
        mIsStop = true;
        if(!executor.isShutdown()) {
            executor.shutdownNow();
        }
    }

    private void setSelectedDotBackground(ImageView imageView) {
        imageView.setBackgroundResource(R.mipmap.ic_dot_red);
    }

    private void setNormalDotBackground(ImageView imageView) {
        imageView.setBackgroundResource(R.mipmap.ic_dot_white);
    }
}