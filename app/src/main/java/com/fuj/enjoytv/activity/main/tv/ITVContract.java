package com.fuj.enjoytv.activity.main.tv;

/**
 * Created by dell on 2016/6/29
 */


import com.fuj.enjoytv.base.IBasePresenter;
import com.fuj.enjoytv.base.IBaseView;

public interface ITVContract {
    interface View extends IBaseView<Presenter> {

    }

    interface Presenter extends IBasePresenter {

    }
}
