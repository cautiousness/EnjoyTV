package com.fuj.enjoytv.widget.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.utils.DensityUtils;

/**
 * 仿qq消息气泡
 */
public class DragBubbleView extends View {
    private Paint mBubblePaint;
    private Path mBezierPath;
    private Paint mTextPaint;
    private Rect mTextRect;
    private Paint mExplosionPaint;
    private Rect mExplosionRect;

    private PointF mBubblePoint = new PointF(0, 0); //拖拽圆圆心
    private PointF mCirclePoint = new PointF(0, 0); //拖拽圆圆心
    private float mCircleRadius; // 黏连小圆的半径
    private float mBubbleRadius; // 手指拖拽气泡的半径
    private String mText; // 气泡消息的文本
    private float d; // 两圆圆心的间距
    private float maxD; // 两圆圆心间距的最大距离，超出此值黏连小圆消失

    private int[] mExplosionDrawables = {R.mipmap.ic_bubble_a, R.mipmap.ic_bubble_b
            , R.mipmap.ic_bubble_c, R.mipmap.ic_bubble_d, R.mipmap.ic_bubble_e};
    private Bitmap[] mExplosionBitmaps; // 气泡爆炸的bitmap数组
    private int mCurExplosionIndex; // 气泡爆炸当前进行到第几张
    private boolean mIsExplosionAnimStart = false; // 气泡爆炸动画是否开始
    private int mState; // 气泡的状态
    private static final int STATE_DEFAULT = 0x00; // 默认，无法拖拽
    private static final int STATE_DRAG = 0x01; // 拖拽
    private static final int STATE_MOVE = 0x02; // 移动
    private static final int STATE_DISMISS = 0x03; // 消失
    private OnBubbleStateListener mOnBubbleStateListener; // 气泡状态的监听

    public DragBubbleView(Context context) {
        this(context, null);
    }

    public DragBubbleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragBubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BubbleView, defStyleAttr, 0);
        mBubbleRadius = ta.getDimension(R.styleable.BubbleView_bubbleRadius, DensityUtils.dp2px(context, 12));
        int mBubbleColor = ta.getColor(R.styleable.BubbleView_bubbleColor, Color.RED);
        mText = ta.getString(R.styleable.BubbleView_text);
        float mTextSize = ta.getDimension(R.styleable.BubbleView_textSize, DensityUtils.dp2px(context, 12));
        int mTextColor = ta.getColor(R.styleable.BubbleView_textColor, Color.WHITE);
        mState = STATE_DEFAULT;
        mCircleRadius = mBubbleRadius;
        maxD = 8 * mBubbleRadius;
        ta.recycle();

        mBubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBubblePaint.setColor(mBubbleColor);
        mBubblePaint.setStyle(Paint.Style.FILL);

        mBezierPath = new Path();

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextRect = new Rect();

        mExplosionPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mExplosionPaint.setFilterBitmap(true);
        mExplosionRect = new Rect();
        mExplosionBitmaps = new Bitmap[mExplosionDrawables.length];
        for (int i = 0; i < mExplosionDrawables.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mExplosionDrawables[i]);
            mExplosionBitmaps[i] = bitmap;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measuredDimension(widthMeasureSpec), measuredDimension(heightMeasureSpec));
    }

    private int measuredDimension(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = (int) (2 * mBubbleRadius);
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initData(w, h);
    }

    private void initData(int w, int h) { //设置圆心坐标
        mBubblePoint.set(w/2, h/2);
        mCirclePoint.set(w/2, h/2);
        mState = STATE_DEFAULT;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mState != STATE_DISMISS) { //画拖拽气泡
            canvas.drawCircle(mBubblePoint.x, mBubblePoint.y, mBubbleRadius, mBubblePaint);
        }

        if (mState == STATE_DRAG && d < maxD - maxD / 4) {
            canvas.drawCircle(mCirclePoint.x, mCirclePoint.y, mCircleRadius, mBubblePaint); //画黏连小圆
            float mControlX = (mBubblePoint.x + mCirclePoint.x) / 2;
            float mControlY = (mBubblePoint.y + mCirclePoint.y) / 2;
            float sin = (mBubblePoint.y - mCirclePoint.y) / d; //计算两条二阶贝塞尔曲线的起点和终点
            float cos = (mBubblePoint.x - mCirclePoint.x) / d;
            float mCircleStartX = mCirclePoint.y - mCircleRadius * sin;
            float mCircleStartY = mCirclePoint.y + mCircleRadius * cos;
            float mBubbleEndX = mBubblePoint.x - mBubbleRadius * sin;
            float mBubbleEndY = mBubblePoint.y + mBubbleRadius * cos;
            float mBubbleStartX = mBubblePoint.x + mBubbleRadius * sin;
            float mBubbleStartY = mBubblePoint.y - mBubbleRadius * cos;
            float mCircleEndX = mCirclePoint.x + mCircleRadius * sin;
            float mCircleEndY = mCirclePoint.y - mCircleRadius * cos;

            mBezierPath.reset(); //画二阶贝赛尔曲线
            mBezierPath.moveTo(mCircleStartX, mCircleStartY);
            mBezierPath.quadTo(mControlX, mControlY, mBubbleEndX, mBubbleEndY);
            mBezierPath.lineTo(mBubbleStartX, mBubbleStartY);
            mBezierPath.quadTo(mControlX, mControlY, mCircleEndX, mCircleEndY);
            mBezierPath.close();
            canvas.drawPath(mBezierPath, mBubblePaint);
        }

        if (mState != STATE_DISMISS && !TextUtils.isEmpty(mText)) { //画消息个数的文本
            mTextPaint.getTextBounds(mText, 0, mText.length(), mTextRect);
            canvas.drawText(mText, mBubblePoint.x - mTextRect.width() / 2, mBubblePoint.y + mTextRect.height() / 2, mTextPaint);
        }

        if (mIsExplosionAnimStart && mCurExplosionIndex < mExplosionDrawables.length) {
            //设置气泡爆炸图片的位置
            mExplosionRect.set((int) (mBubblePoint.x - mBubbleRadius), (int) (mBubblePoint.y - mBubbleRadius)
                    , (int) (mBubblePoint.x + mBubbleRadius), (int) (mBubblePoint.y + mBubbleRadius));
            //根据当前进行到爆炸气泡的位置index来绘制爆炸气泡bitmap
            canvas.drawBitmap(mExplosionBitmaps[mCurExplosionIndex], null, mExplosionRect, mExplosionPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mState != STATE_DISMISS) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    d = (float) Math.hypot(event.getX() - mBubblePoint.x, event.getY() - mBubblePoint.y);
                    if (d < mBubbleRadius + maxD / 4) { //当指尖坐标在圆内的时候，才认为是可拖拽的
                        mState = STATE_DRAG;
                    } else {
                        mState = STATE_DEFAULT;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mState != STATE_DEFAULT) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    mBubblePoint.set(event.getX(), event.getY());
                    d = (float) Math.hypot(mBubblePoint.x - mCirclePoint.x, mBubblePoint.y - mCirclePoint.y); //计算气泡圆心与黏连小球圆心的间距
                    if (mState == STATE_DRAG) { // 如果可拖拽
                        if (d < maxD - maxD / 4) { // 间距小于可黏连的最大距离减去(maxD/4)的像素大小，是为了让黏连小球半径到一个较小值快消失时直接消失
                            mCircleRadius = mBubbleRadius - d / 8;//使黏连小球半径渐渐变小
                            if (mOnBubbleStateListener != null) {
                                mOnBubbleStateListener.onDrag();
                            }
                        } else { // 间距大于于可黏连的最大距离
                            mState = STATE_MOVE; // 改为移动状态
                            if (mOnBubbleStateListener != null) {
                                mOnBubbleStateListener.onMove();
                            }
                        }
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                if (mState == STATE_DRAG) { // 正在拖拽时松开手指，气泡恢复原来位置并颤动一下
                    setBubbleRestoreAnim();
                } else if (mState == STATE_MOVE) {//正在移动时松开手指
                    if (d < 2 * mBubbleRadius) {// 如果在移动状态下间距回到两倍半径之内，我们认为用户不想取消该气泡，那么气泡恢复原来位置并颤动一下
                        setBubbleRestoreAnim();
                    } else { //气泡消失
                        setBubbleDismissAnim();
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 设置气泡复原的动画
     */
    private void setBubbleRestoreAnim() {
        ValueAnimator anim = ValueAnimator.ofObject(new PointFEvaluator(),
                mBubblePoint,
                new PointF(mCirclePoint.x, mCirclePoint.y));
        anim.setDuration(500);
        anim.setInterpolator(new TimeInterpolator() { // 自定义Interpolator差值器达到颤动效果
            @Override
            public float getInterpolation(float input) {
                float f = 0.571429f;
                return (float) (Math.pow(2, -4 * input) * Math.sin((input - f / 4) * (2 * Math.PI) / f) + 1);
            }
        });
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF curPoint = (PointF) animation.getAnimatedValue();
                mBubblePoint.set(curPoint);
                invalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mState = STATE_DEFAULT; //动画结束后状态改为默认
                if (mOnBubbleStateListener != null) {
                    mOnBubbleStateListener.onRestore();
                }
            }
        });
        anim.start();
    }

    /**
     * 设置气泡消失的动画
     */
    private void setBubbleDismissAnim() {
        mState = STATE_DISMISS; // 气泡改为消失状态
        mIsExplosionAnimStart = true;
        if (mOnBubbleStateListener != null) {
            mOnBubbleStateListener.onDismiss();
        }

        ValueAnimator anim = ValueAnimator.ofInt(0, mExplosionDrawables.length); //做一个int型属性动画，从0开始，到气泡爆炸图片数组个数结束
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(500);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurExplosionIndex = (int) animation.getAnimatedValue(); //拿到当前的值并重绘
                invalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mIsExplosionAnimStart = false; //动画结束后改变状态
            }
        });
        anim.start();
    }

    public interface OnBubbleStateListener {
        void onDrag(); // 拖拽气泡

        void onMove(); // 移动气泡

        void onRestore(); // 气泡恢复原来位置

        void onDismiss(); // 气泡消失
    }

    /**
     * 设置气泡状态的监听器
     */
    public void setOnBubbleStateListener(OnBubbleStateListener onBubbleStateListener) {
        mOnBubbleStateListener = onBubbleStateListener;
    }

    public void setText(String text) {
        mText = text;
        invalidate();
    }

    public void reCreate() {
        initData(getWidth(), getHeight());
        invalidate();
    }

    private static class PointFEvaluator implements TypeEvaluator<PointF> {
        @Override
        public PointF evaluate(float fraction, PointF startPointF, PointF endPointF) {
            float x = startPointF.x + fraction * (endPointF.x - startPointF.x);
            float y = startPointF.y + fraction * (endPointF.y - startPointF.y);
            return new PointF(x, y);
        }
    }
}
