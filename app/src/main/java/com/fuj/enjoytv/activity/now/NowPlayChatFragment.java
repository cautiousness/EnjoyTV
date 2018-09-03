package com.fuj.enjoytv.activity.now;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.model.now.ChatEvent;
import com.fuj.enjoytv.model.now.NowChat;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gang
 */
public class NowPlayChatFragment extends Fragment {
    @Bind(R.id.now_chat)
    RecyclerView chatRV;

    @Bind(R.id.now_chatBTN)
    Button sendBTN;

    @Bind(R.id.now_chatET)
    EditText chatET;

    private NowPlayChatAdapter mAdapter;

    public static Fragment newInstance() {
        return new NowPlayChatFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new NowPlayChatAdapter(getContext().getApplicationContext(), new ArrayList<NowChat>(), R.layout.item_chat);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_nowchat, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        sendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg();
            }
        });

        chatRV.setLayoutManager(new LinearLayoutManager(getContext()));
        chatRV.setAdapter(mAdapter);
    }

    private void sendMsg() {
        if (chatET.getEditableText().toString().trim().length() < 1) {
            return;
        }

        String msg = chatET.getText().toString();
        addMsg("me", msg, 1);
        chatET.getEditableText().clear();
    }

    public void addMsg(String user, String msg, int level) {
        mAdapter.addData(mAdapter.getItemCount(), new NowChat(user, msg, level));
        chatRV.scrollToPosition(mAdapter.getItemCount() - 1);
        EventBus.getDefault().post(new ChatEvent(msg));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
