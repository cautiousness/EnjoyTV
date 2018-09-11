package com.fuj.enjoytv.widget.comm;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.fuj.enjoytv.listener.OnChatClickListener;
import com.fuj.enjoytv.utils.LogUtils;

public class SwipeItemLayout extends ViewGroup {
    private int downX, moveX, moved;
    private boolean haveShowRight = false;

    private Scroller scroller = new Scroller(getContext());
    private OnChatClickListener mListener;

    public static SwipeItemLayout INSTANCE;

    public static SwipeItemLayout getINSTANCE() {
        return INSTANCE;
    }

    public SwipeItemLayout(Context context) {
        super(context);
    }

    public SwipeItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (INSTANCE != null) {
            INSTANCE.closeMenus();
            INSTANCE = null;
        }
    }

    private void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        scroller.startScroll(scrollX, 0, delta, 0, 100);
        invalidate();
    }

    public void closeMenus() {
        smoothScrollTo(0, 0);
        haveShowRight = false;
    }

    public void closeMenu() {
        INSTANCE.closeMenus();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scroller.isFinished()) {
            return false;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (INSTANCE != null && INSTANCE == this && haveShowRight) {
                    closeMenu();
                    return true;
                }
                moveX = (int) ev.getRawX();
                moved = moveX - downX;

                if (haveShowRight) {
                    moved -= getChildAt(1).getMeasuredWidth();
                }
                scrollTo(-moved, 0);
                if (getScrollX() <= 0) {
                    scrollTo(0, 0);
                } else if (getScrollX() >= getChildAt(1).getMeasuredWidth()) {
                    scrollTo(getChildAt(1).getMeasuredWidth(), 0);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (INSTANCE != null) {
                    closeMenu();
                }

                if(getScrollX() < 20) {
                    mListener.clickContent();
                } else if (getScrollX() >= getChildAt(1).getMeasuredWidth() / 2) {
                    haveShowRight = true;
                    INSTANCE = this;
                    smoothScrollTo(getChildAt(1).getMeasuredWidth(), 0);
                } else {
                    haveShowRight = false;
                    smoothScrollTo(0, 0);
                }
                break;
        }
        return true;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        View child = getChildAt(0);
        int margin = ((MarginLayoutParams) child.getLayoutParams()).topMargin +
                ((MarginLayoutParams) child.getLayoutParams()).bottomMargin;
        setMeasuredDimension(width, getChildAt(0).getMeasuredHeight() + margin);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            if (i == 0) {
                child.layout(l, t, r, b);
            } else if (i == 1) {
                child.layout(r, t, r + child.getMeasuredWidth(), b);
            }
        }
    }

    public void setOnChatClickListener(OnChatClickListener listener) {
        mListener = listener;
    }
}

