package com.fuj.enjoytv.activity.main.chat;

/**
 * Created by gang
 */
public class ChatPresenter implements IChatContract.Presenter {
    private IChatContract.View mView;

    public ChatPresenter(IChatContract.View mView) {
        this.mView = mView;
    }

    public void start() {

    }
}
