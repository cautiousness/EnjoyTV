package com.fuj.enjoytv.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.activity.chat_dtl.ChatDtlActivity;
import com.fuj.enjoytv.adapter.base.RVAdapter;
import com.fuj.enjoytv.adapter.base.RVHolder;
import com.fuj.enjoytv.listener.OnChatClickListener;
import com.fuj.enjoytv.model.chat.Chat;
import com.fuj.enjoytv.utils.AppManager;
import com.fuj.enjoytv.widget.comm.SwipeItemLayout;
import java.util.List;

/**
 * Created by gang
 */
public class ChatAdapter extends RVAdapter<Chat> {
    private Context mContext;

    public ChatAdapter(Context context, List<Chat> datas, int layoutId) {
        super(context, datas, layoutId);
        mContext = context;
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
                SwipeItemLayout.getINSTANCE().closeMenu();
                mDatas.remove(holder.getLayoutPosition());
                notifyItemRemoved(holder.getLayoutPosition());
            }
        });

        holder.setItemContentClickListener(R.id.swipe_layout, new OnChatClickListener() {
            @Override
            public void clickContent() {
                Intent intent = new Intent(mContext, ChatDtlActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppManager.BUNDLE_CHAT_DETAIL, chat);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }
}
