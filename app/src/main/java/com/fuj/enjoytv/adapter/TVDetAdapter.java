package com.fuj.enjoytv.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fuj.enjoytv.R;
import com.fuj.enjoytv.adapter.base.RVAdapter;
import com.fuj.enjoytv.adapter.base.RVHolder;
import com.fuj.enjoytv.model.tv.TVDet;
import com.fuj.enjoytv.utils.LogUtils;
import com.fuj.enjoytv.widget.comm.GlideCircleTransform;

import java.lang.ref.SoftReference;
import java.util.List;

import static com.baidu.mapapi.BMapManager.getContext;

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
        RequestOptions options = new RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext.get())
        .load(getResource(tvDet.thum))
        .apply(options)
        .into((ImageView) holder.getView(R.id.item_thum));
        holder.setText(R.id.item_title, tvDet.title);
        holder.setText(R.id.item_content, tvDet.content);
        holder.setText(R.id.item_time, tvDet.startTime);
    }
}
