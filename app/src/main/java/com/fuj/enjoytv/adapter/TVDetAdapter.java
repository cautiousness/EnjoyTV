package com.fuj.enjoytv.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fuj.enjoytv.R;
import com.fuj.enjoytv.adapter.base.RVAdapter;
import com.fuj.enjoytv.adapter.base.RVHolder;
import com.fuj.enjoytv.model.tv.TVDet;
import com.fuj.enjoytv.utils.LogUtils;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * Created by gang
 */
public class TVDetAdapter extends RVAdapter<TVDet> {
    private SoftReference<Context> mContext;

    public TVDetAdapter(Context context, List<TVDet> datas, int layoutId) {
        super(context, datas, layoutId);
        mContext = new SoftReference<>(context);
    }

    @Override
    public void convert(RVHolder holder, TVDet tvDet) {
        Glide.with(mContext.get())
        .load(getResource(tvDet.thum))
        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
        .into((ImageView) holder.getView(R.id.item_thum));
        holder.setText(R.id.item_title, tvDet.title);
        holder.setText(R.id.item_content, tvDet.content);
        holder.setText(R.id.item_time, tvDet.startTime);
    }
}
