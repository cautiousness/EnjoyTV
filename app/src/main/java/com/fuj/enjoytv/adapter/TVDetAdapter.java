package com.fuj.enjoytv.adapter;

import android.content.Context;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.adapter.base.RVAdapter;
import com.fuj.enjoytv.adapter.base.RVHolder;
import com.fuj.enjoytv.model.tv.TVDet;

import java.util.List;

/**
 * Created by gang
 */
public class TVDetAdapter extends RVAdapter<TVDet> {

    public TVDetAdapter(Context context, List<TVDet> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(RVHolder holder, TVDet tvDet) {
        holder.setImageByGlide(R.id.item_thum, getResource(tvDet.thum));
        holder.setText(R.id.item_title, tvDet.title);
        holder.setText(R.id.item_content, tvDet.content);
        holder.setText(R.id.item_time, tvDet.startTime);
    }
}
