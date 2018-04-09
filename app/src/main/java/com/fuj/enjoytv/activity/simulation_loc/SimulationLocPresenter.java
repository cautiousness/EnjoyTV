package com.fuj.enjoytv.activity.simulation_loc;

import android.os.Handler;
import android.os.Looper;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.fuj.enjoytv.R;
import com.fuj.enjoytv.utils.Constant;
import com.fuj.enjoytv.utils.LogUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by gang
 */
public class SimulationLocPresenter implements ISimulationLocContact.Presenter {
    private float mCurZoom = 10;

    private LatLng mSelfLatLng;
    private LocationClient mLocClient;
    private MarkerOptions mSelfMarker;
    private MyLocationListener myListener;

    private ISimulationLocContact.View mView;

    public SimulationLocPresenter(ISimulationLocContact.View view) {
        this.mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void setLocArg() {
        setLocationListener();
        startLocate();
    }

    @Override
    public void setCurZoom(float zoom) {
        mCurZoom = zoom;
    }

    @Override
    public void initLocClient(LocationClient mLocClient) {
        this.mLocClient = mLocClient;
        myListener = new MyLocationListener();
        mSelfMarker = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_loc_self));
        mView.addMarker(mSelfMarker.position(new LatLng(0.0, 0.0)));
    }

    private void setLocationListener() {
        if (myListener == null) {
            myListener = new MyLocationListener();
        } else {
            mLocClient.registerLocationListener(myListener);
        }
    }

    private void startLocate() {
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(10000);
        option.setAddrType("all");
        mLocClient.setLocOption(option);
        startLocateThread();
    }

    private void startLocateThread() {
        mLocClient.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mSelfLatLng == null) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                sendMapThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.i(" [latlng] " + mSelfLatLng);
                        mSelfMarker.position(mSelfLatLng);
                        mView.setMapCenterAndZoom(MapStatusUpdateFactory.newLatLngZoom(mSelfLatLng, mCurZoom));
                        mView.hook(mSelfLatLng);
                    }
                });
                mLocClient.stop();
            }
        }).start();
    }

    private void sendMapThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    @Override
    public void reLocate() {
        mSelfLatLng = null;
        startLocateThread();
    }

    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            if(location == null)
                return;
            mSelfLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        }
    }
}
