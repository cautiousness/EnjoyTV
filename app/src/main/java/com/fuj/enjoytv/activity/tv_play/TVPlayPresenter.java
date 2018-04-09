package com.fuj.enjoytv.activity.tv_play;

/**
 * Created by gang
 */
public class TVPlayPresenter implements ITVPlayContact.Presenter {
    private ITVPlayContact.View mView;

    public TVPlayPresenter(ITVPlayContact.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    public void start() {

    }
}
