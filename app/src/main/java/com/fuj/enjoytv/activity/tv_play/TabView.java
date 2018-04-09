package com.fuj.enjoytv.activity.tv_play;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.utils.DensityUtils;

/**
 * Created by gang
 */
public class TabView extends FrameLayout implements Checkable {
    private int mMinHeight;
    private int mPaddingLeft;
    private boolean mChecked;

    private Context mContext;
    private TextView mTitle;
    private TabTitle mTabTitle;
    private LinearLayout mContainer;

    public TabView(Context context) {
        this(context, 0);
    }

    public TabView(Context context, int padding) {
        super(context);
        this.mPaddingLeft = padding;
        this.mContext = context;
        mMinHeight = DensityUtils.dp2px(mContext, 30);
        mTabTitle = new TabTitle(context);
        initView();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, new int[]{android.R.attr.state_checked});
        }
        return drawableState;
    }

    private void initView() {
        initContainer();
        initTitleView();
        addView(mContainer);
    }

    private void initContainer() {
        mContainer = new LinearLayout(mContext);
        mContainer.setOrientation(LinearLayout.HORIZONTAL);
        mContainer.setMinimumHeight(mMinHeight);
        mContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mContainer.setPadding(mPaddingLeft, 0, 0, 0);
        mContainer.setGravity(Gravity.CENTER);
    }

    private void initTitleView() {
        if (mTitle != null) mContainer.removeView(mTitle);
        mTitle = new TextView(mContext);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mTitle.setLayoutParams(params);
        mTitle.setTextColor(mTabTitle.mColorNormal);
        mTitle.setTextSize(mTabTitle.mTitleTextSize);
        mTitle.setText(mTabTitle.mContent);
        mTitle.setGravity(Gravity.CENTER);
        mTitle.setSingleLine();
        mTitle.setEllipsize(TextUtils.TruncateAt.END);
        mContainer.addView(mTitle);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        refreshDrawableState();
        mTitle.setTextColor(checked ? mTabTitle.mColorSelected : mTabTitle.mColorNormal);
        mTitle.setBackgroundColor(getResources().getColor(checked ? R.color.white : R.color.silver_white));
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    public TabView setTitle(TabTitle title) {
        if (title != null)
            mTabTitle = title;
        initTitleView();
        setChecked(mChecked);
        return this;
    }

    public String getTitle() {
        return mTabTitle.mContent;
    }

    public static class TabTitle {
        private int mColorSelected;
        private int mColorNormal;
        private int mTitleTextSize;
        private String mContent;

        public TabTitle(Context context) {
            this(context.getResources().getColor(R.color.colorPrimary),
            context.getResources().getColor(R.color.black),
            14, "title");
        }

        public TabTitle(int mColorSelected, int mColorNormal, int mTitleTextSize, String mContent) {
            this.mColorSelected = mColorSelected;
            this.mColorNormal = mColorNormal;
            this.mTitleTextSize = mTitleTextSize;
            this.mContent = mContent;
        }

        public TabTitle setTextColor(int colorSelected, int colorNormal) {
            mColorSelected = colorSelected;
            mColorNormal = colorNormal;
            return this;
        }

        public TabTitle setTextSize(int sizeSp) {
            mTitleTextSize = sizeSp;
            return this;
        }

        public TabTitle setContent(String content) {
            mContent = content;
            return this;
        }
    }
}