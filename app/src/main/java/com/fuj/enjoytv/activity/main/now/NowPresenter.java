package com.fuj.enjoytv.activity.main.now;

/**
 * Created by gang
 */
public class NowPresenter implements INowContract.Presenter {
    private INowContract.View mView;

    public NowPresenter(INowContract.View mView) {
        this.mView = mView;
    }

    public void start() {

    }
}
