package com.fuj.enjoytv.activity.qrcode;

/**
 * Created by gang
 */
public class QRCodePresenter implements IQRCodeContact.Presenter {
    private IQRCodeContact.View mView;

    public QRCodePresenter(IQRCodeContact.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    public void start() {

    }
}
