package com.fuj.enjoytv.widget.comm;

/**
 * Created by gang
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Scroller;

import java.util.ArrayList;

public class MarqueeView extends View {
    private int mLineHeight = 30;
    private int mCurline = 0; // 当前行
    private int mNextLine = 1;
    private int mTextSize = 15;
    private int mCurLineYPos = 0;
    private int mPreLineYPos = 0;
    private int mNextLineYPos = 0;
    private int mScrollHeight = 0;

    private boolean mIsScrolling = false;
    private boolean mIsPlay = false;

    private Paint mPaint;
    private Rect mTexRect;
    private ArrayList<MarqueeLine> mContents;
    private Scroller mScroller;
    private PlayRunnable playRunnable = new PlayRunnable();

    public MarqueeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MarqueeView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mLineHeight = dp2px(mLineHeight);
        mTextSize = dp2px(mTextSize);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(1);
        mPaint.setTextSize(mTextSize);

        mTexRect = new Rect();
        mPaint.getTextBounds("0", 0, 1, mTexRect);

        mContents = new ArrayList<>();

        mContents.add(makeMarqueeLine("体育","张稀哲戴帽"));
        mContents.add(makeMarqueeLine("热点","无心法师2"));
        mContents.add(makeMarqueeLine("聚焦","村里母猪为何半夜惨叫"));

        mNextLine = (mCurline + 1) % mContents.size();
        mScroller = new Scroller(getContext());
    }

    private MarqueeLine makeMarqueeLine(String preWords,String contentWord) {
        MarqueeLine marqueeLine = new MarqueeLine();
        marqueeLine.preWords = preWords;
        marqueeLine.preWordColor =  Color.RED;
        marqueeLine.contentWords = contentWord;
        marqueeLine.contentWordsColor = Color.BLACK;
        Rect preRect = new Rect();

        mPaint.getTextBounds(marqueeLine.preWords, 0, marqueeLine.preWords.length(), preRect);
        marqueeLine.wordDrawX = preRect.width() + marqueeLine.wordSpace;
        return marqueeLine;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.AT_MOST) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mLineHeight, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCurLineYPos = (getMeasuredHeight() + mTexRect.height()) / 2;
        mPreLineYPos = mCurLineYPos - mLineHeight;
        mNextLineYPos = mCurLineYPos + mLineHeight;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mScrollHeight = mScroller.getCurrY();
            if (mScroller.isFinished()) {
                mCurline = (mCurline + 1) % mContents.size();
                mNextLine = (mCurline + 1) % mContents.size();
                mScrollHeight = 0;
                mIsScrolling = false;
            }
            invalidate();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawLine(canvas, mCurline, mCurLineYPos);
        if(mIsScrolling) {
            drawLine(canvas, mNextLine, mNextLineYPos);
        }
    }

    private void drawLine(Canvas canvas, int drawLineNo, int drawLinePos) {
        MarqueeLine marqueeLine = mContents.get(drawLineNo);
        mPaint.setColor(marqueeLine.preWordColor);

        canvas.drawText(marqueeLine.preWords, getPaddingLeft(), drawLinePos - mScrollHeight, mPaint);
        mPaint.setColor(marqueeLine.contentWordsColor);
        if(!marqueeLine.isEllip) {
            marqueeLine.contentWords = (String) TextUtils.ellipsize(marqueeLine.contentWords, new TextPaint(mPaint),
                getWidth() - marqueeLine.wordDrawX - getPaddingLeft() -  getPaddingRight(), TextUtils.TruncateAt.END);
            marqueeLine.isEllip = true;
        }

        canvas.drawText(marqueeLine.contentWords, getPaddingLeft() + marqueeLine.wordDrawX,
                drawLinePos - mScrollHeight, mPaint);
    }

    private void nextLine() {
        mIsScrolling = true;
        mScroller.startScroll(0, 0, 0, mLineHeight, 300);
        invalidate();
    }

    public void startPlay() {
        if(mIsPlay) {
            return;
        }
        mIsPlay = true;
        post(playRunnable);
    }

    public void stopPlay() {
        mIsPlay = false;
    }

    public class MarqueeLine {
        public String preWords;
        public int preWordColor;
        public int wordSpace = 10;
        public String contentWords;
        public int contentWordsColor;
        public float wordDrawX;
        public boolean isEllip = false;
    }

    public  int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    private class PlayRunnable implements Runnable {
        @Override
        public void run() {
            if(mIsPlay) {
                nextLine();
                postDelayed(this, 4000);
            }
        }
    }
}
