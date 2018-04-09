package com.fuj.enjoytv.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fuj.enjoytv.R;
import com.fuj.enjoytv.adapter.base.RVAdapter;
import com.fuj.enjoytv.adapter.base.RVHolder;
import com.fuj.enjoytv.model.now.Now;
import com.fuj.enjoytv.utils.LogUtils;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * Created by gang
 */
public class NowAdapter extends RVAdapter<Now> {
    private SoftReference<Context> mContext;

    public NowAdapter(Context context, List<Now> datas, int layoutId) {
        super(context, datas, layoutId);
        mContext = new SoftReference<>(context);
    }

    @Override
    public void convert(RVHolder holder, Now now) {
        Glide.with(mContext.get())
        .load(getResource(now.cover))
        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
        .into((ImageView) holder.getView(R.id.item_cover));
        holder.setText(R.id.item_title, now.title);
        holder.setText(R.id.item_anchor, now.anchor);
        holder.setText(R.id.item_viewer, now.viewer);
    }
}
