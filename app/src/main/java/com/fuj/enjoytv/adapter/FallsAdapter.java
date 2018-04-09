package com.fuj.enjoytv.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fuj.enjoytv.R;
import com.fuj.enjoytv.adapter.base.RVAdapter;
import com.fuj.enjoytv.adapter.base.RVHolder;
import com.fuj.enjoytv.model.main.Pic_title;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * Created by gang
 */
public class FallsAdapter extends RVAdapter<Pic_title> {
    private SoftReference<Context> mContext;

    public FallsAdapter(Context context, List<Pic_title> datas, int layoutId) {
        super(context, datas, layoutId);
        this.mContext = new SoftReference<>(context);
    }

    @Override
    public void convert(RVHolder holder, Pic_title item) {
        ImageView imageView = holder.getView(R.id.item_gif);
        Glide.with(mContext.get())
        .load(getResource(item.pic))
        .asGif()
        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
        .into(imageView);
        holder.setText(R.id.item_title, item.title);
    }
}
