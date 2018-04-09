package com.fuj.enjoytv.activity.main.user;

/**
 * Created by dell on 2016/6/29
 */


import com.fuj.enjoytv.base.IBasePresenter;
import com.fuj.enjoytv.base.IBaseView;

public interface IUserContract {
    interface View extends IBaseView<Presenter> {

    }

    interface Presenter extends IBasePresenter {

    }
}
