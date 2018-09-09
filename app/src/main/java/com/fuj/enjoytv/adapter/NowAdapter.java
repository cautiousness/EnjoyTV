package com.fuj.enjoytv.adapter;

import android.content.Context;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.adapter.base.RVAdapter;
import com.fuj.enjoytv.adapter.base.RVHolder;
import com.fuj.enjoytv.model.now.Now;

import java.util.List;

/**
 * Created by gang
 */
public class NowAdapter extends RVAdapter<Now> {

    public NowAdapter(Context context, List<Now> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(RVHolder holder, Now now) {
        holder.setImageByGlide(R.id.item_cover, getResource(now.cover));
        holder.setText(R.id.item_title, now.title);
        holder.setText(R.id.item_anchor, now.anchor);
        holder.setText(R.id.item_viewer, now.viewer);
    }
}
