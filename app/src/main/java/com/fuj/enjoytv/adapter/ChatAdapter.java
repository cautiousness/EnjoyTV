package com.fuj.enjoytv.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fuj.enjoytv.R;
import com.fuj.enjoytv.adapter.base.RVAdapter;
import com.fuj.enjoytv.adapter.base.RVHolder;
import com.fuj.enjoytv.model.chat.Chat;
import com.fuj.enjoytv.model.main.Pic_title;
import com.fuj.enjoytv.widget.comm.GlideCircleTransform;
import com.fuj.enjoytv.widget.comm.SwipeItemLayout;

import java.lang.ref.SoftReference;
import java.util.List;

import static com.baidu.mapapi.BMapManager.getContext;

/**
 * Created by gang
 */
public class ChatAdapter extends RVAdapter<Chat> {
    private SoftReference<Context> mContext;

    public ChatAdapter(Context context, List<Chat> datas, int layoutId) {
        super(context, datas, layoutId);
        this.mContext = new SoftReference<>(context);
    }

    @Override
    public void convert(final RVHolder holder, final Chat chat) {
        ImageView imageView = holder.getView(R.id.item_headimg);
        RequestOptions options = new RequestOptions()
        .centerCrop()
        .transform(new GlideCircleTransform(getContext()))
        .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext.get())
        .load(getResource(chat.pic))
        .apply(options)
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
                mDatas.remove(holder.getLayoutPosition());
                notifyItemRemoved(holder.getLayoutPosition());
            }
        });
    }
}
