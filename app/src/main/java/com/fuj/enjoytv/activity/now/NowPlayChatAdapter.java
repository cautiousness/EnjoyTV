package com.fuj.enjoytv.activity.now;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.adapter.base.RVAdapter;
import com.fuj.enjoytv.adapter.base.RVHolder;
import com.fuj.enjoytv.model.now.NowChat;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * Created by gang
 */
public class NowPlayChatAdapter extends RVAdapter<NowChat> {
    private SoftReference<Context> mContext;

    public NowPlayChatAdapter(Context context, List<NowChat> datas, int layoutId) {
        super(context, datas, layoutId);
        mContext = new SoftReference<>(context);
    }

    @Override
    public void convert(RVHolder holder, NowChat nowChat) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(nowChat.user + "：" + nowChat.content);
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor(getColor(nowChat.level))), 0, nowChat.user.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.setText(R.id.item_content, spannable);
    }

    public String getColor(int level) {
        String color = "#5e5d5d";
        switch (level) {
            case 1:
                color = "#FF03A9F4";
                break;
            case 2:
                color = "#FFE62A2A";
                break;
            case 3:
                color = "#FF5DF243";
                break;
            default:
                break;
        }
        return color;
    }
}
