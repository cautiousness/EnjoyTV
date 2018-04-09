package com.fuj.enjoytv.activity.now;

/**
 * Created by gang
 */
public class NowPlayPresenter implements INowPlayContact.Presenter {
    private INowPlayContact.View mView;

    public NowPlayPresenter(INowPlayContact.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    public void start() {

    }
}
