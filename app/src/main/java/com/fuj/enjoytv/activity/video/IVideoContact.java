package com.fuj.enjoytv.activity.video;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.fuj.enjoytv.base.IBasePresenter;
import com.fuj.enjoytv.base.IBaseView;

/**
 * Created by gang
 */
public interface IVideoContact {
    interface View extends IBaseView<Presenter> {
    }

    interface Presenter extends IBasePresenter {
    }
}
