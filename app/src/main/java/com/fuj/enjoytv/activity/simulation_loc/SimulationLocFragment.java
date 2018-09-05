package com.fuj.enjoytv.activity.simulation_loc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.fuj.enjoytv.R;
import com.fuj.enjoytv.base.BaseFragment;
import com.fuj.enjoytv.utils.Constant;
import com.fuj.enjoytv.utils.LogUtils;
import com.fuj.enjoytv.widget.map.MapControlButton;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gang
 */
public class SimulationLocFragment extends BaseFragment implements ISimulationLocContact.View {
    @Bind(R.id.loc_mapView)
    MapView mMapView;

    @Bind(R.id.loc_locationBTN)
    Button mLocationBTN;

    private BaiduMap mBaiduMap;
    private ISimulationLocContact.Presenter mPresenter;

    protected void findViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.view_loc, container, false);
        ButterKnife.bind(this, root);
        initView();
        setTitle("地图");
    }

    @Override
    public void setPresenter(ISimulationLocContact.Presenter presenter) {
        this.mPresenter = presenter;
        mPresenter.start();
    }

    private void initView() {
        setMarginTop(mMapView);

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15).build()));

        mMapView.showZoomControls(false);
        MapControlButton mapControlBTN = (MapControlButton) findViewById(R.id.map_mapControlBTN);
        mapControlBTN.setMapView(mMapView);
        mBaiduMap.setMyLocationEnabled(true);

        mPresenter.initLocClient(new LocationClient(getContext()));
        mPresenter.setLocArg();
    }

    @Override
    protected void setListeners() {
        super.setListeners();

        mLocationBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.reLocate();
            }
        });

        mBaiduMap.setOnMapStatusChangeListener(new MapChangeListener(this));
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {}

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void setMapCenterAndZoom(MapStatusUpdate mapStatusUpdate) {
        mBaiduMap.animateMapStatus(mapStatusUpdate);
        mPresenter.refreshSelfMarker();
    }

    @Override
    public void addMarker(OverlayOptions overlayOptions) {
        mBaiduMap.addOverlay(overlayOptions);
    }

    @Override
    public void hook(LatLng mSelfLatLng) {
        setPreDouble(Constant.LOC_LAT, mSelfLatLng.latitude);
        setPreDouble(Constant.LOC_LON, mSelfLatLng.longitude);
    }

    @Override
    public void clearMarkers() {
        mBaiduMap.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        mBaiduMap.clear();
        mBaiduMap.setOnMapStatusChangeListener(null);
        mBaiduMap = null;
        mMapView = null;
        ButterKnife.unbind(this);
    }

    private static class MapChangeListener implements BaiduMap.OnMapStatusChangeListener {
        private WeakReference<SimulationLocFragment> mFragment;


        public MapChangeListener(SimulationLocFragment fragment) {
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {
            mFragment.get().mPresenter.setCurZoom(mapStatus.zoom);
        }

        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {
        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
        }
    }
}
