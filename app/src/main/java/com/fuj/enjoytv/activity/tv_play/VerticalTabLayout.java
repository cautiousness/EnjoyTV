package com.fuj.enjoytv.activity.tv_play;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.utils.DensityUtils;

import java.lang.ref.SoftReference;

public class VerticalTabLayout extends ScrollView {
    private int mTabHeight;
    private int mTabMargin;
    private int mTabMode;
    private int mIndicatorWidth;
    private int mIndicatorGravity;
    private int mColorIndicator;

    private float mIndicatorCorners;

    private TabStrip mTabStrip;
    private TabView mLastTab;

    public static int TAB_MODE_FIXED = 10;
    public static int TAB_MODE_SCROLLABLE = 11;

    private static SoftReference<Context> mContext;

    private OnTabSelectedListener mTabSelectedListener;

    public VerticalTabLayout(Context context) {
        this(context, null);
    }

    public VerticalTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = new SoftReference<>(context);
        setFillViewport(true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalTabLayout);
        mColorIndicator = typedArray.getColor(R.styleable.VerticalTabLayout_indicator_color, context.getResources().getColor(R.color.colorPrimary));
        mIndicatorWidth = (int) typedArray.getDimension(R.styleable.VerticalTabLayout_indicator_width, DensityUtils.dp2px(mContext.get(), 2));
        mIndicatorCorners = typedArray.getDimension(R.styleable.VerticalTabLayout_indicator_corners, 0);
        mIndicatorGravity = typedArray.getInteger(R.styleable.VerticalTabLayout_indicator_gravity, Gravity.START);
        mTabMargin = (int) typedArray.getDimension(R.styleable.VerticalTabLayout_tab_margin, 0);
        mTabMode = typedArray.getInteger(R.styleable.VerticalTabLayout_tab_mode, TAB_MODE_FIXED);
        mTabHeight = (int) typedArray.getDimension(R.styleable.VerticalTabLayout_tab_height, 16);
        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) removeAllViews();
        initTabStrip();
    }

    private void initTabStrip() {
        mTabStrip = new TabStrip(mContext.get());
        addView(mTabStrip, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public void removeAllTabs() {
        mTabStrip.removeAllViews();
        mLastTab = null;
    }

    public TabView getTabAt(int position) {
        return (TabView) mTabStrip.getChildAt(position);
    }

    private void addTabWithMode(TabView tabView) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        initTabWithMode(params);
        mTabStrip.addView(tabView, params);
        if (mTabStrip.indexOfChild(tabView) == 0) {
            tabView.setChecked(true);
            params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            tabView.setLayoutParams(params);
            mLastTab = tabView;
        }
    }

    private void initTabWithMode(LinearLayout.LayoutParams params) {
        if (mTabMode == TAB_MODE_FIXED) {
            params.height = 0;
            params.weight = 1.0f;
            params.setMargins(0, 0, 0, 0);
        } else if (mTabMode == TAB_MODE_SCROLLABLE) {
            params.height = mTabHeight;
            params.weight = 0f;
            params.setMargins(0, mTabMargin, 0, 0);
        }
    }

    private void scrollToTab() {
        mLastTab.post(new Runnable() {
            @Override
            public void run() {
                int y = getScrollY();
                int tabTop = mLastTab.getTop() + mLastTab.getHeight() / 2 - y;
                int target = getHeight() / 2;
                if (tabTop > target) {
                    smoothScrollBy(0, tabTop - target);
                } else if (tabTop < target) {
                    smoothScrollBy(0, tabTop - target);
                }
            }
        });
    }

    public void addTab(TabView tabView) {
        if (tabView != null) {
            addTabWithMode(tabView);
            tabView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = mTabStrip.indexOfChild(view);
                    setCurrentTab(position);
                }
            });
        } else {
            throw new IllegalStateException("tabview can't be null");
        }
    }

    public void setCurrentTab(int position) {
        TabView view = getTabAt(position);
        if (view != mLastTab) {
            mTabSelectedListener.onTabSelected(view, position);
            mLastTab.setChecked(false);
            view.setChecked(true);
            mTabStrip.moveIndicator(position);
            mLastTab = view;
            scrollToTab();
        }
    }

    public void setTabMode(int mode) {
        if (mode != TAB_MODE_FIXED && mode != TAB_MODE_SCROLLABLE) {
            throw new IllegalStateException("only support TAB_MODE_FIXED or TAB_MODE_SCROLLABLE");
        }
        if (mode == mTabMode) return;
        mTabMode = mode;
        for (int i = 0; i < mTabStrip.getChildCount(); i++) {
            View view = mTabStrip.getChildAt(i);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            initTabWithMode(params);
            if (i == 0) {
                params.setMargins(0, 0, 0, 0);
            }
            view.setLayoutParams(params);
        }
        mTabStrip.invalidate();
        mTabStrip.post(new Runnable() {
            @Override
            public void run() {
                mTabStrip.updataIndicatorMargin();
            }
        });
    }

    /**
     * only in TAB_MODE_SCROLLABLE mode will be supported
     *
     * @param margin margin
     */
    public void setTabMargin(int margin) {
        if (margin == mTabMargin) return;
        mTabMargin = margin;
        if (mTabMode == TAB_MODE_FIXED) return;
        for (int i = 0; i < mTabStrip.getChildCount(); i++) {
            View view = mTabStrip.getChildAt(i);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.setMargins(0, i == 0 ? 0 : mTabMargin, 0, 0);
            view.setLayoutParams(params);
        }
        mTabStrip.invalidate();
        mTabStrip.post(new Runnable() {
            @Override
            public void run() {
                mTabStrip.updataIndicatorMargin();
            }
        });
    }

    /**
     * only in TAB_MODE_SCROLLABLE mode will be supported
     *
     * @param height height
     */
    public void setTabHeight(int height) {
        if (height == mTabHeight) return;
        mTabHeight = height;
        if (mTabMode == TAB_MODE_FIXED) return;
        for (int i = 0; i < mTabStrip.getChildCount(); i++) {
            View view = mTabStrip.getChildAt(i);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.height = mTabHeight;
            view.setLayoutParams(params);
        }
        mTabStrip.invalidate();
        mTabStrip.post(new Runnable() {
            @Override
            public void run() {
                mTabStrip.updataIndicatorMargin();
            }
        });
    }

    public void setIndicatorColor(int color) {
        mColorIndicator = color;
        mTabStrip.invalidate();
    }

    public void setIndicatorWidth(int width) {
        mIndicatorWidth = width;
        mTabStrip.setIndicatorGravity();
    }

    public void setIndicatorCorners(int corners) {
        mIndicatorCorners = corners;
        mTabStrip.invalidate();
    }

    /**
     * @param gravity only support Gravity.LEFT,Gravity.RIGHT,Gravity.FILL
     */
    public void setIndicatorGravity(int gravity) {
        if (gravity == Gravity.START || gravity == Gravity.END || Gravity.FILL == gravity) {
            mIndicatorGravity = gravity;
            mTabStrip.setIndicatorGravity();
        } else {
            throw new IllegalStateException("only support Gravity.LEFT,Gravity.RIGHT,Gravity.FILL");
        }
    }

    public void addOnTabSelectedListener(OnTabSelectedListener listener) {
        if (listener != null) {
            mTabSelectedListener = listener;
        }
    }

    public void setTabAdapter(TVPlayListFragment.TabAdapter adapter) {
        removeAllTabs();
        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                addTab(new TabView(mContext.get(), mIndicatorWidth).setTitle(adapter.getTitle(i)));
            }
            setCurrentTab(0);
        } else {
            removeAllTabs();
        }
    }

    private int getTabCount() {
        return mTabStrip.getChildCount();
    }

    private int getSelectedTabPosition() {
        int index = mTabStrip.indexOfChild(mLastTab);
        return index == -1 ? 0 : index;
    }

    private class TabStrip extends LinearLayout {
        private float mIndicatorY;
        private float mIndicatorX;
        private float mIndicatorBottomY;
        private int mLastWidth;
        private int mIndicatorHeight;
        private Paint mIndicatorPaint;
        private long mInvalidateCount;

        public TabStrip(Context context) {
            super(context);
            setWillNotDraw(false);
            setOrientation(LinearLayout.VERTICAL);
            mIndicatorPaint = new Paint();
            mIndicatorGravity = mIndicatorGravity == 0 ? Gravity.START : mIndicatorGravity;
            setIndicatorGravity();
        }

        @Override
        protected void onFinishInflate() {
            super.onFinishInflate();
            mIndicatorBottomY = mIndicatorHeight;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (getChildCount() > 0) {
                View childView = getChildAt(0);
                mIndicatorHeight = childView.getMeasuredHeight();
                if (mInvalidateCount == 0) {
                    mIndicatorBottomY = mIndicatorHeight;
                }
                mInvalidateCount++;
            }
        }

        protected void updataIndicatorMargin() {
            int index = getSelectedTabPosition();
            mIndicatorY = calcIndicatorY(index);
            mIndicatorBottomY = mIndicatorY + mIndicatorHeight;
            invalidate();
        }

        protected void setIndicatorGravity() {
            if (mIndicatorGravity == Gravity.START) {
                mIndicatorX = 0;
                if (mLastWidth != 0) mIndicatorWidth = mLastWidth;
                setPadding(mIndicatorWidth, 0, 0, 0);
            } else if (mIndicatorGravity == Gravity.END) {
                if (mLastWidth != 0) mIndicatorWidth = mLastWidth;
                setPadding(0, 0, mIndicatorWidth, 0);
            } else if (mIndicatorGravity == Gravity.FILL) {
                mIndicatorX = 0;
                setPadding(0, 0, 0, 0);
            }
            post(new Runnable() {
                @Override
                public void run() {
                    if (mIndicatorGravity == Gravity.END) {
                        mIndicatorX = getWidth() - mIndicatorWidth;
                    } else if (mIndicatorGravity == Gravity.FILL) {
                        mLastWidth = mIndicatorWidth;
                        mIndicatorWidth = getWidth();
                    }
                    invalidate();
                }
            });
        }

        private float calcIndicatorY(float offset) {
            if (mTabMode == TAB_MODE_FIXED)
                return offset * mIndicatorHeight;
            return offset * (mIndicatorHeight + mTabMargin);
        }

        protected void moveIndicator(final int index) {
            final int direction = index - getSelectedTabPosition();
            final float target = calcIndicatorY(index);
            final float targetBottom = target + mIndicatorHeight;
            if (mIndicatorY == target) return;
            post(new Runnable() {
                @Override
                public void run() {
                    ValueAnimator anime = null;
                    if (direction > 0) {
                        anime = ValueAnimator.ofFloat(mIndicatorBottomY, targetBottom);
                        anime.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                mIndicatorBottomY = Float.parseFloat(animation.getAnimatedValue().toString());
                                invalidate();
                            }
                        });
                        anime.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                ValueAnimator anime2 = ValueAnimator.ofFloat(mIndicatorY, target);
                                anime2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        mIndicatorY = Float.parseFloat(animation.getAnimatedValue().toString());
                                        invalidate();
                                    }
                                });
                                anime2.setDuration(100).start();
                            }
                        });

                    } else if (direction < 0) {
                        anime = ValueAnimator.ofFloat(mIndicatorY, target);
                        anime.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                mIndicatorY = Float.parseFloat(animation.getAnimatedValue().toString());
                                invalidate();
                            }
                        });

                        anime.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                ValueAnimator anime2 = ValueAnimator.ofFloat(mIndicatorBottomY, targetBottom);
                                anime2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        mIndicatorBottomY = Float.parseFloat(animation.getAnimatedValue().toString());
                                        invalidate();
                                    }
                                });
                                anime2.setDuration(100).start();
                            }
                        });
                    }
                    if (anime != null) {
                        anime.setDuration(100).start();
                    }
                }
            });
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            mIndicatorPaint.setColor(mColorIndicator);
            RectF r = new RectF(mIndicatorX, mIndicatorY, mIndicatorX + mIndicatorWidth, mIndicatorBottomY);
            if (mIndicatorCorners != 0) {
                canvas.drawRoundRect(r, mIndicatorCorners, mIndicatorCorners, mIndicatorPaint);
            } else {
                canvas.drawRect(r, mIndicatorPaint);
            }
        }
    }

    public interface OnTabSelectedListener {
        void onTabSelected(TabView tab, int position);
    }
}
