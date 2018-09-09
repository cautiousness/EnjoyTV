package com.fuj.enjoytv.adapter;

import android.content.Context;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.adapter.base.RVAdapter;
import com.fuj.enjoytv.adapter.base.RVHolder;
import com.fuj.enjoytv.model.chat.Chat;
import com.fuj.enjoytv.model.chat.Message;

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
            changeView(holder, true, false, R.id.chat_dtl_leftIV, mChat.pic);
            holder.setText(R.id.chat_dtl_leftTV, message.content);
        } else {
            changeView(holder, false, true, R.id.chat_dtl_rightIV, "ic_user_avatar");
            holder.setText(R.id.chat_dtl_rightTV, message.content);
        }
    }

    private void changeView(RVHolder holder, boolean isLeftShow, boolean isRightShow, int viewId, String resource) {
        holder.setVisible(R.id.chat_dtl_leftLL, isLeftShow);
        holder.setVisible(R.id.chat_dtl_rightLL, isRightShow);
        holder.setImageCircleByGlide(viewId, getResource(resource));
    }
}
