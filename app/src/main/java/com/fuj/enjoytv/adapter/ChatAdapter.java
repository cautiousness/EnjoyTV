package com.fuj.enjoytv.adapter;

import android.content.Context;
import android.view.View;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.adapter.base.RVAdapter;
import com.fuj.enjoytv.adapter.base.RVHolder;
import com.fuj.enjoytv.model.chat.Chat;
import com.fuj.enjoytv.widget.comm.SwipeItemLayout;

import java.util.List;

/**
 * Created by gang
 */
public class ChatAdapter extends RVAdapter<Chat> {

    public ChatAdapter(Context context, List<Chat> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(final RVHolder holder, final Chat chat) {
        holder.setImageCircleByGlide(R.id.item_headimg, getResource(chat.pic));
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
