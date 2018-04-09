package com.fuj.enjoytv.adapter.base;

import android.content.Context;
import android.view.ViewGroup;

import java.util.List;

public abstract class MulRVAdapter<T> extends RVAdapter<T> {
    protected ItemHelper<T> mItemHelper;

    public MulRVAdapter(Context context, List<T> datas, ItemHelper<T> itemHelper) {
        super(context, datas, -1);
        mItemHelper = itemHelper;

        if(mItemHelper == null)
            throw new IllegalArgumentException("the mItemHelper can not be null.");
    }

    @Override
    public int getItemViewType(int position) {
        if(mItemHelper != null)
            return mItemHelper.getItemViewType(position, mDatas.get(position));
        return super.getItemViewType(position);
    }

    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mItemHelper == null) return super.onCreateViewHolder(parent, viewType);

        int layoutId = mItemHelper.getLayoutId(viewType);
        RVHolder holder = RVHolder.get(mContext.get(), null, parent, layoutId, -1);
        setListener(parent, holder, viewType);
        return holder;
    }

    public interface ItemHelper<T> {
        int getLayoutId(int itemType);

        int getItemViewType(int position, T t);
    }
}