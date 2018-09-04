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
import com.fuj.enjoytv.model.chat.Message;
import com.fuj.enjoytv.utils.LogUtils;
import com.fuj.enjoytv.widget.comm.GlideCircleTransform;

import java.util.List;

public class MessageAdapter extends RVAdapter<Message> {
    private Chat mChat;

    public MessageAdapter(Context context, List<Message> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    public MessageAdapter(Context context, List<Message> datas, int layoutId, Chat chat) {
        this(context, datas, layoutId);
        mChat = chat;
    }

    @Override
    public void convert(RVHolder holder, Message message) {
        if (message.type == Message.TYPE_RECEIVE) {
            changeView(holder, true, false,
                holder.getView(R.id.chat_dtl_leftIV), mChat.pic);
            holder.setText(R.id.chat_dtl_leftTV, message.content);
        } else {
            changeView(holder, false, true,
                holder.getView(R.id.chat_dtl_rightIV), "ic_user_avatar");
            holder.setText(R.id.chat_dtl_rightTV, message.content);
        }
    }

    private void changeView(RVHolder holder, boolean isLeftShow, boolean isRightShow, View view, String resource) {
        holder.setVisible(R.id.chat_dtl_leftLL, isLeftShow);
        holder.setVisible(R.id.chat_dtl_rightLL, isRightShow);
        setHeadImg(view, getResource(resource));
    }

    private void setHeadImg(View view, Integer resouceId) {
        ImageView imageView = (ImageView) view;
        Glide.with(mContext.get())
        .load(resouceId)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .transform(new GlideCircleTransform(mContext.get()))
        .into(imageView);
    }
}
