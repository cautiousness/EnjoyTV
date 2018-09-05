package com.fuj.enjoytv.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fuj.enjoytv.R;
import com.fuj.enjoytv.adapter.base.RVAdapter;
import com.fuj.enjoytv.adapter.base.RVHolder;
import com.fuj.enjoytv.model.now.Now;
import com.fuj.enjoytv.utils.LogUtils;
import com.fuj.enjoytv.widget.comm.GlideCircleTransform;

import java.lang.ref.SoftReference;
import java.util.List;

import static com.baidu.mapapi.BMapManager.getContext;

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
        RequestOptions options = new RequestOptions()
        .centerCrop()
        .transform(new GlideCircleTransform(getContext()))
        .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext.get())
        .load(getResource(now.cover))
        .apply(options)
        .into((ImageView) holder.getView(R.id.item_cover));
        holder.setText(R.id.item_title, now.title);
        holder.setText(R.id.item_anchor, now.anchor);
        holder.setText(R.id.item_viewer, now.viewer);
    }
}
