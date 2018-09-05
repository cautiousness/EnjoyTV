package com.fuj.enjoytv.activity.simulation_loc;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.fuj.enjoytv.base.IBasePresenter;
import com.fuj.enjoytv.base.IBaseView;

/**
 * Created by gang
 */
public interface ISimulationLocContact {
    interface View extends IBaseView<Presenter> {

        void setMapCenterAndZoom(MapStatusUpdate mapStatusUpdate);

        void addMarker(OverlayOptions overlayOptions);

        void hook(LatLng mSelfLatLng);

        void clearMarkers();
    }

    interface Presenter extends IBasePresenter {

        void setCurZoom(float zoom);

        void initLocClient(LocationClient mLocClient);

        void reLocate();

        void setLocArg();

        void refreshSelfMarker();

        void onDestroy();
    }
}
