package com.fuj.enjoytv.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fuj.enjoytv.model.main.Pic_title;
import com.fuj.enjoytv.utils.LogUtils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gang
 */
public class MainLoopAdapter extends PagerAdapter {
    private SoftReference<Context> mContext;
    private List<Pic_title> mList;

    public MainLoopAdapter(Context context) {
        mContext = new SoftReference<>(context);
        mList = new ArrayList<>();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView view = new ImageView(mContext.get());
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setImageResource(getResource(mList.get(position).pic));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    public void updateData(List<Pic_title> list) {
        mList = list;
        if (mList != null && mList.size() > 2) {
            mList.add(mList.size(), mList.get(0));
            mList.add(0, mList.get(mList.size() - 2));
        }
        notifyDataSetChanged();
    }

    private int getResource(String imageName) {
        return mContext.get().getResources().getIdentifier(imageName, "mipmap", mContext.get().getPackageName());
    }

    public List<Pic_title> getList() {
        return mList;
    }
}
