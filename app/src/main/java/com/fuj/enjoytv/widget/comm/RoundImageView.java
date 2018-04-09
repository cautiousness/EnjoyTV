package com.fuj.enjoytv.widget.comm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.support.v7.widget.AppCompatImageView;

import com.fuj.enjoytv.R;

import java.lang.ref.WeakReference;

public class RoundImageView extends AppCompatImageView {
	private Paint mPaint;
	private Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
	private Bitmap mMaskBitmap;
	private WeakReference<Bitmap> mWeakBitmap;
	private int type;
	public static final int TYPE_CIRCLE = 0;
	public static final int TYPE_ROUND = 1;
	private static final int BODER_RADIUS_DEFAULT = 20;
	private int mBorderRadius; // 圆角类型的弧度大小

	public RoundImageView(Context context) {
		this(context, null);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageViewByXfermode);
		mBorderRadius = a.getDimensionPixelSize(
		        R.styleable.RoundImageViewByXfermode_borderRadius,
		        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
		                BODER_RADIUS_DEFAULT, getResources().getDisplayMetrics()));// 默认为10dp
		type = a.getInt(R.styleable.RoundImageViewByXfermode_type, TYPE_CIRCLE);
		a.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if(type == TYPE_CIRCLE) {
			int width = (int) (Math.min(getMeasuredWidth(), getMeasuredHeight()) * 1.2);
			setMeasuredDimension(width, width);
		}
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		Bitmap bitmap = mWeakBitmap == null ? null : mWeakBitmap.get();
		if(null == bitmap || bitmap.isRecycled()) {
			Drawable drawable = getDrawable();// 拿到Drawable
			int dWidth = drawable.getIntrinsicWidth();
			int dHeight = drawable.getIntrinsicHeight();
            bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_4444);
            float scale;
            Canvas drawCanvas = new Canvas(bitmap);
            scale = type == TYPE_ROUND ? Math.max(getWidth() * 1.0f / dWidth, getHeight() * 1.0f / dHeight) :
                    getWidth() * 1.0F / Math.min(dWidth, dHeight);

            drawable.setBounds(0, 0, (int) (scale * dWidth), (int) (scale * dHeight));
            drawable.draw(drawCanvas);

            if(mMaskBitmap == null || mMaskBitmap.isRecycled()) {
                mMaskBitmap = getBitmap();
            }

            mPaint.reset();
            mPaint.setFilterBitmap(false);
            mPaint.setXfermode(mXfermode);
            drawCanvas.drawBitmap(mMaskBitmap, 0, 0, mPaint);
            mPaint.setXfermode(null);
            canvas.drawBitmap(bitmap, 0, 0, null);
            mWeakBitmap = new WeakReference<>(bitmap);
		}

        mPaint.setXfermode(null);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, mPaint);
	}

	public Bitmap getBitmap() {
		Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.BLACK);
		if(type == TYPE_ROUND) {
			canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()),
			        mBorderRadius, mBorderRadius, paint);
		} else {
			canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2, paint);
		}
		return bitmap;
	}
}