package com.fuj.enjoytv.activity.main.user;

/**
 * Created by gang
 */
public class UserPresenter implements IUserContract.Presenter {
    private IUserContract.View mView;

    public UserPresenter(IUserContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    public void start() {

    }
}
