package com.fuj.enjoytv.activity.tv_play;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.view.View;
import android.widget.CompoundButton;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.adapter.base.RVAdapter;
import com.fuj.enjoytv.adapter.base.RVHolder;
import com.fuj.enjoytv.utils.ToastUtils;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * Created by gang
 */
public class TVPlayPlaylistAdapter extends RVAdapter<TVProgram> {
    private SoftReference<Context> mContext;

    public TVPlayPlaylistAdapter(Context context, List<TVProgram> datas, int layoutId) {
        super(context, datas, layoutId);
        mContext = new SoftReference<>(context);
    }

    @Override
    public void convert(RVHolder holder, TVProgram tvProgram) {
        holder.setText(R.id.subscribe_playlist_time, tvProgram.time);
        holder.setText(R.id.subscribe_playlist_title, tvProgram.title);
        holder.setText(R.id.subscribe_playlist_status, tvProgram.status == 0 ? "预约" : "已预约");
        holder.setChecked(R.id.subscribe_playlist_status, tvProgram.status != 0);
        holder.setOnClickListener(R.id.subscribe_playlist_status, new View.OnClickListener() {
            @Override
            public void onClick(View checkView) {
                if(!((AppCompatCheckedTextView)checkView).isChecked()){
                    ((AppCompatCheckedTextView)checkView).setChecked(true);
                    ((AppCompatCheckedTextView) checkView).setText("已预约");
                    showYES("预约成功");
                } else {
                    ((AppCompatCheckedTextView)checkView).setChecked(false);
                    ((AppCompatCheckedTextView) checkView).setText("预约");
                    showYES("已取消预约");
                }
            }
        });
    }

    private void showYES(final String msg) {
        ToastUtils.showYESorNO(mContext.get(), R.layout.widget_toast_yes, msg);
    }
}
