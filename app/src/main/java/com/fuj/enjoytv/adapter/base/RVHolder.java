package com.fuj.enjoytv.adapter.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fuj.enjoytv.widget.comm.GlideCircleTransform;
import com.fuj.enjoytv.widget.main.DragBubbleView;

import java.lang.ref.SoftReference;

import static com.baidu.mapapi.BMapManager.getContext;

public class RVHolder extends RecyclerView.ViewHolder {
    private int mPosition;
    private int mLayoutId;

    private SparseArray<View> mViews;
    private View mConvertView;
    private SoftReference<Context> mContext;

    public RVHolder(Context context, View itemView, ViewGroup parent, int position) {
        super(itemView);
        mContext = new SoftReference<>(context);
        mConvertView = itemView;
        mPosition = position;
        mViews = new SparseArray<>();
        mConvertView.setTag(this);
    }

    public static RVHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            RVHolder holder = new RVHolder(context, itemView, parent, position);
            holder.mLayoutId = layoutId;
            return holder;
        } else {
            RVHolder holder = (RVHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public boolean getChecked(int viewId) {
        Checkable cb = getView(viewId);
        return cb.isChecked();
    }

    public RVHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public RVHolder setBubble(int viewId, String text) {
        DragBubbleView bubbleView = getView(viewId);
        bubbleView.setText(text);
        return this;
    }

    public RVHolder setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public RVHolder setMaxLine(int viewId, int maxLine) {
        TextView tv = getView(viewId);
        tv.setMaxLines(maxLine);
        return this;
    }

    public RVHolder setMinLine(int viewId, int maxLine) {
        TextView tv = getView(viewId);
        tv.setMinLines(maxLine);
        return this;
    }

    public RVHolder setLine(int viewId, int maxLine) {
        TextView tv = getView(viewId);
        tv.setLines(maxLine);
        return this;
    }

    public RVHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public RVHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public RVHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public RVHolder setBackgroundColor(int viewId, int color) {
        getView(viewId).setBackgroundColor(color);
        return this;
    }

    public RVHolder setBackgroundRes(int viewId, int backgroundRes) {
        getView(viewId).setBackgroundResource(backgroundRes);
        return this;
    }

    public RVHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public RVHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(mContext.get().getResources().getColor(textColorRes));
        return this;
    }

    @SuppressLint("NewApi")
    public RVHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    public RVHolder setVisible(int viewId, boolean visible) {
        getView(viewId).setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public RVHolder linkify(int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public RVHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public RVHolder setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    public RVHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public RVHolder setMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    public RVHolder setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    public RVHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public RVHolder setTag(int viewId, Object tag) {
        getView(viewId).setTag(tag);
        return this;
    }

    public RVHolder setTag(int viewId, int key, Object tag) {
        getView(viewId).setTag(key, tag);
        return this;
    }

    public Object getTag(int viewId) {
        return getView(viewId).getTag();
    }

    public RVHolder setChecked(int viewId, boolean checked) {
        Checkable view = getView(viewId);
        view.setChecked(checked);
        return this;
    }

    public RVHolder setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener listener) {
        CheckBox cb = getView(viewId);
        cb.setOnCheckedChangeListener(listener);
        return this;
    }

    public RVHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        getView(viewId).setOnClickListener(listener);
        return this;
    }

    public RVHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        getView(viewId).setOnTouchListener(listener);
        return this;
    }

    public RVHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        getView(viewId).setOnLongClickListener(listener);
        return this;
    }

    public int updatePosition(int position) {
        mPosition = position;
        return mPosition;
    }

    public RVHolder setImageCircleByGlide(int viewId, int resId) {
        RequestOptions options = new RequestOptions()
            .centerCrop()
            .transform(new GlideCircleTransform(getContext()))
            .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext.get())
            .load(resId)
            .apply(options)
            .into((ImageView) getView(viewId));
        return this;
    }

    public RVHolder setImageByGlide(int viewId, int resId) {
        RequestOptions options = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext.get())
            .load(resId)
            .apply(options)
            .into((ImageView) getView(viewId));
        return this;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    public RVHolder setMargins(int viewId, int left, int top, int right, int bottom) {
        View view = getView(viewId);
        ViewGroup.MarginLayoutParams paramTest2 = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        paramTest2.setMargins(left, top, right, bottom);
        view.requestLayout();
        return this;
    }

    public RVHolder setWH(int viewId, int w, int h) {
        View view = getView(viewId);
        view.getLayoutParams().width = w;
        view.getLayoutParams().height = h;
        return this;
    }
}
