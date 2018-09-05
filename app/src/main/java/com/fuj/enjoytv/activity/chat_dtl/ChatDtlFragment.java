package com.fuj.enjoytv.activity.chat_dtl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.adapter.MessageAdapter;
import com.fuj.enjoytv.base.BaseFragment;
import com.fuj.enjoytv.model.MsgResult;
import com.fuj.enjoytv.model.chat.Chat;
import com.fuj.enjoytv.model.chat.Message;
import com.fuj.enjoytv.utils.Constant;
import com.fuj.enjoytv.utils.JsonUtils;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatDtlFragment extends BaseFragment implements IChatDtlContact.View {

    @Bind(R.id.chat_dtl_msg)
    RecyclerView mRV;

    private Chat mChat;
    private MessageAdapter mMsgAdapter;
    private IChatDtlContact.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(Constant.BUNDLE_CHAT_DETAIL)) {
            mChat = getArguments().getParcelable(Constant.BUNDLE_CHAT_DETAIL);
        }
        initAdapter();
        getData();
    }

    @Override
    protected void findViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.view_chat_dtl, container, false);
        ButterKnife.bind(this, root);
        initView();
        setTitle(mChat.name);
    }

    @Override
    public void setPresenter(IChatDtlContact.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void initTitle(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.initTitle(inflater, container, savedInstanceState);
        title = inflater.inflate(R.layout.title_chat, container, false);
    }

    private void initAdapter() {
        mMsgAdapter = new MessageAdapter(getContext(), new ArrayList<Message>(), R.layout.item_chat_dtl, mChat);
    }

    private void initView() {
        mRV.setLayoutManager(new LinearLayoutManager(getContext()));
        setMarginTop(mRV);
        mRV.setAdapter(mMsgAdapter);
        mRV.scrollToPosition(mMsgAdapter.getItemCount() - 1);
    }

    private void getData() {
        String content = JsonUtils.readJsonFile(getContext(), "msglist");
        MsgResult result = new Gson().fromJson(content, MsgResult.class);
        mMsgAdapter.updateRecyclerView(result.datas);
    }
}
