package com.fuj.enjoytv.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fuj.enjoytv.R;
import com.fuj.enjoytv.adapter.base.RVAdapter;
import com.fuj.enjoytv.adapter.base.RVHolder;
import com.fuj.enjoytv.model.chat.Chat;
import com.fuj.enjoytv.model.main.Pic_title;
import com.fuj.enjoytv.widget.comm.GlideCircleTransform;
import com.fuj.enjoytv.widget.comm.SwipeItemLayout;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * Created by gang
 */
public class ChatAdapter extends RVAdapter<Chat> {
    private int position;
    private SoftReference<Context> mContext;
    private List<Chat> mDatas;

    public ChatAdapter(Context context, List<Chat> datas, int layoutId) {
        super(context, datas, layoutId);
        this.mContext = new SoftReference<>(context);
        this.mDatas = datas;
    }

    @Override
    public void convert(final RVHolder holder, final Chat chat) {
        position = holder.getLayoutPosition();
        ImageView imageView = holder.getView(R.id.item_headimg);
        Glide.with(mContext.get())
        .load(getResource(chat.pic))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .transform(new GlideCircleTransform(mContext.get()))
        .into(imageView);
        holder.setText(R.id.item_nameTV, chat.name);
        holder.setText(R.id.item_contentTV, chat.content);
        holder.setText(R.id.item_timeTV, chat.time);
        holder.setVisible(R.id.item_count, chat.msgCount != 0);
        holder.setBubble(R.id.item_count, String.valueOf(chat.msgCount));
        holder.setOnClickListener(R.id.swipe_del, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeItemLayout.closeMenu();
                mDatas.remove(position);
                notifyItemRemoved(position);
            }
        });
    }
}