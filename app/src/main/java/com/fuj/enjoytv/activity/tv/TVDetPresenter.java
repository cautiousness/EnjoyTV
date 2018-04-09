package com.fuj.enjoytv.activity.tv;

/**
 * Created by gang
 */
public class TVDetPresenter implements ITVDetContact.Presenter {
    private ITVDetContact.View mView;

    public TVDetPresenter(ITVDetContact.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    public void start() {

    }
}
