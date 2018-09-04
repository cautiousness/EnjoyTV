package com.fuj.enjoytv.activity.chat_dtl;

public class ChatDtlPresenter implements IChatDtlContact.Presenter {
    private IChatDtlContact.View mChatDtlView;

    public ChatDtlPresenter(IChatDtlContact.View mChatDtlView) {
        this.mChatDtlView = mChatDtlView;
    }

    @Override
    public void start() {

    }
}
