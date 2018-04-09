package com.fuj.enjoytv.activity.main.tv;

/**
 * Created by gang
 */
public class TVPresenter implements ITVContract.Presenter {
    private ITVContract.View mView;

    public TVPresenter(ITVContract.View mView) {
        this.mView = mView;
    }

    public void start() {

    }
}
