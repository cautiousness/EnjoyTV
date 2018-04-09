package com.fuj.enjoytv.adapter;

import android.content.Context;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.adapter.base.RVAdapter;
import com.fuj.enjoytv.adapter.base.RVHolder;
import com.fuj.enjoytv.model.main.Pic_title;

import java.util.List;

/**
 * Created by gang
 */
public class NaviAdapter extends RVAdapter<Pic_title> {
    public NaviAdapter(Context context, List<Pic_title> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(RVHolder holder, Pic_title item) {
        holder.setImageResource(R.id.item_logo, getResource(item.pic));
        holder.setText(R.id.item_title, item.title);
    }
}
