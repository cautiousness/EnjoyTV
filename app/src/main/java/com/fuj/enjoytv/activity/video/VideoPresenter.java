package com.fuj.enjoytv.activity.video;

/**
 * Created by gang
 */
public class VideoPresenter implements IVideoContact.Presenter {

    private IVideoContact.View mView;

    public VideoPresenter(IVideoContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
